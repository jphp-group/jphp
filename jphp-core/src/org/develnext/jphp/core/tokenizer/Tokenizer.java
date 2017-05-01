package org.develnext.jphp.core.tokenizer;

import org.develnext.jphp.core.common.TokenizeGrammarUtils;
import org.develnext.jphp.core.tokenizer.token.*;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.EchoRawToken;
import php.runtime.common.Directive;
import php.runtime.common.Messages;
import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.ParseException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tokenizer {
    protected Context context;
    protected TokenFinder tokenFinder;

    private int currentPosition;
    private int startRelativePosition;
    private int relativePosition;
    private int currentLine;

    private final int codeLength;
    protected final String code;

    protected Token prevToken;

    protected boolean rawMode;

    protected Map<String, Directive> directives = new HashMap<String, Directive>();

    public Tokenizer(Context context) throws IOException {
        this.context = context;
        this.code = context.getContent();
        this.codeLength = code.length();
        this.rawMode = context.isLikeFile();
        reset();
    }

    public Tokenizer(String code, Context context){
        this.context = context;
        this.currentPosition = -1;
        this.currentLine = 0;
        this.relativePosition = 0;
        this.code = code;
        this.codeLength = code.length();
        this.tokenFinder = new TokenFinder();
        this.rawMode = false;
    }

    public void reset(){
        this.currentPosition = -1;
        this.currentLine = 0;
        this.relativePosition = -1;
        this.tokenFinder = new TokenFinder();
        if (!rawMode)
            this.relativePosition = 0;

        this.directives.clear();
    }

    public boolean hasDirective(String name){
        return directives.get(name) != null;
    }

    public Directive getDirective(String name){
        Directive value = directives.get(name);
        if (value == null)
            return null;
        else
            return value;
    }

    public String getCode() {
        return code;
    }

    protected final static Map<Class<?>, Constructor> tokenConstructors = new HashMap<Class<?>, Constructor>();

    @SuppressWarnings("unchecked")
    protected <T extends Token> T buildToken(Class<T> clazz, TokenMeta meta){
        Constructor<T> constructor = tokenConstructors.get(clazz);
        if (constructor == null){
            try {
                tokenConstructors.put(clazz, constructor = clazz.getConstructor(TokenMeta.class));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return (T) (prevToken = constructor.newInstance(meta));
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable build " + clazz.getSimpleName() + " token: " + e.getTargetException());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected TokenMeta buildMeta(int startPosition, int startLine){
        String word = getWord(startPosition, currentPosition);
        if (word == null)
            return null;

        TokenMeta meta = new TokenMeta(word, startLine, currentLine, startRelativePosition, relativePosition);
        int length = word.length();
        meta.setStartIndex(currentPosition - length);
        meta.setEndIndex(currentPosition);

        if (length == 1 && TokenizeGrammarUtils.isDelimiter(word.charAt(0))) {
            meta.setStartIndex(currentPosition);
            meta.setEndIndex(currentPosition + 1);
        }

        return meta;
    }

    protected String getWord(int startPosition, int endPosition){
        if (endPosition < startPosition)
            return null;

        if (endPosition == startPosition){
            if (startPosition >= codeLength)
                return null;
            return code.substring(startPosition, startPosition + 1);
        }

        if (endPosition > codeLength)
            endPosition = codeLength - 1;

        return code.substring(startPosition, endPosition);
    }

    protected Token tryNextToken(){
        int len = 0;
        char ch;
        if (currentPosition + 1 < codeLength){
            ch = code.charAt(currentPosition + 1);
            if (TokenizeGrammarUtils.isDelimiter(ch) && !TokenizeGrammarUtils.isSpace(ch)){
                len = 1;
                if (currentPosition + 2 < codeLength){
                    ch = code.charAt(currentPosition + 2);
                    if (TokenizeGrammarUtils.isDelimiter(ch) && !TokenizeGrammarUtils.isSpace(ch)){
                        len = 2;
                    }
                }
            }
        }

        if (len == 0)
            return null;

        String word = getWord(currentPosition, currentPosition + len + 1);
        Class<? extends Token> tokenClass = tokenFinder.find(word);
        if (len == 2 && tokenClass == null){
            len -= 1;
            word = getWord(currentPosition, currentPosition + len + 1);
            tokenClass = tokenFinder.find(word);
        }

        if (tokenClass != null){
            int startPosition = currentPosition;
            currentPosition += len + 1;
            Token token = buildToken(tokenClass, buildMeta(startPosition, currentLine));
            currentPosition -= 1;
            return token;
        }
        return null;
    }

    private void incCurrentPosition(int value){
        if (value < 0){
            while (currentPosition > 0 && value < 0){
                currentPosition--;
                value++;
                if (TokenizeGrammarUtils.isNewline(code.charAt(currentPosition)))
                    currentLine--;
            }
        }
    }

    protected boolean checkNewLine(char ch, boolean invert){
        if (TokenizeGrammarUtils.isNewline(ch)){
            if (invert) {
                currentLine--;
            } else {
                currentLine++;
            }

            relativePosition = 0;
            startRelativePosition = 0;
            return true;
        }
        
        return false;
    }

    protected boolean checkNewLine(char ch){
        return checkNewLine(ch, false);
    }

    protected ValueExprToken readString(StringExprToken.Quote quote, int startPosition, int startLine){
        int i = currentPosition + 1, pos = relativePosition + 1;
        StringExprToken.Quote ch_quote = null;
        boolean slash = false;
        StringBuilder sb = new StringBuilder();

        boolean isMagic = quote != null && quote.isMagic();
        String endString = null;

        int startIndex = currentPosition + 1;

        if (quote == StringExprToken.Quote.DOC){
            StringBuilder tmp = new StringBuilder();
            StringExprToken.Quote docType = null;

            for(; i < codeLength; i++){
                char ch = code.charAt(i);
                pos++;

                if (docType == null && TokenizeGrammarUtils.isQuote(ch) != null) {
                    docType = TokenizeGrammarUtils.isQuote(ch);
                } else if (docType != null && docType == TokenizeGrammarUtils.isQuote(ch)) {
                    if (i + 1 >= codeLength || !TokenizeGrammarUtils.isNewline(code.charAt(i + 1))) {
                        throw new ParseException(
                                Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch(),
                                new TraceInfo(context, currentLine, currentLine, pos + 1, pos + 1)
                        );
                    }
                    i += 1;
                    break;
                    // nop
                } else if (tmp.length() == 0 && (ch == ' ' || ch == '\t')) {
                    //nop
                } else if (TokenizeGrammarUtils.isEngLetter(ch) || ch == '_' || (tmp.length() != 0 && Character.isDigit(ch))){
                    tmp.append(ch);
                } else if (tmp.length() > 0 && checkNewLine(ch)){
                    pos = 0;
                    break;
                } else {
                    String error = Messages.ERR_PARSE_UNEXPECTED_X.fetch(ch);
                    if (TokenizeGrammarUtils.isNewline(ch))
                        error = Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch();

                    throw new ParseException(
                            error,
                            new TraceInfo(context, currentLine, currentLine, pos, pos)
                    );
                }
            }

            currentPosition = i;
            i += 1; // skip \n

            isMagic = (docType == null || docType.isMagic());
            endString = tmp.toString();
        }

        List<StringExprToken.Segment> segments = new ArrayList<StringExprToken.Segment>();


        for(; i < codeLength; i++){
            char ch = code.charAt(i);

            pos++;
            ch_quote = TokenizeGrammarUtils.isQuote(ch);
            if (endString == null && (ch_quote == quote && !slash)){
                currentPosition  = i;
                relativePosition = pos;
                break;
            }

            if (checkNewLine(ch)) {
                pos = 0;
                if (endString != null){
                    int end = i + 1 + endString.length();
                    if (end < codeLength){
                        if (code.substring(i + 1, end).equals(endString)) {
                            if ((code.charAt(end) == ';' && TokenizeGrammarUtils.isNewline(code.charAt(end + 1))) || TokenizeGrammarUtils.isNewline(code.charAt(end))) {
                                currentPosition = i + endString.length();
                                relativePosition = endString.length();
                                ch_quote = StringExprToken.Quote.DOC;
                                break;
                            }
                        }
                    }
                }
            }

            if (!isMagic){
                switch (ch) {
                    case '\\':
                        if (!slash || endString != null)
                            sb.append(ch);
                        slash = !slash;
                        break;
                    case '\'':
                        if (endString == null)
                            sb.deleteCharAt(sb.length() - 1); // remove slash
                    default:
                        sb.append(ch);
                        slash = false;
                }
            } else {
                int dynamic = 0;
                if (!slash){
                    if (ch == '$' && (i + 1 < codeLength && code.charAt(i + 1) == '{') ) {
                        dynamic = 2;
                    }
                    if (ch == '{' && (i + 1 < codeLength && code.charAt(i + 1) == '$') )
                        dynamic = 1;
                }

                if (dynamic > 0) {
                    if (dynamic == 2 ||  i + 1 < codeLength && code.charAt(i + 1) == '$') {
                        slash = false;
                        int opened = dynamic == 2 ? 0 : 1;
                        int j;
                        for(j = i + 1; j < codeLength; j++){
                            switch (code.charAt(j)){
                                case '{': opened++; break;
                                case '}': opened--; break;
                            }
                            checkNewLine(code.charAt(j));

                            if (opened == 0)
                                break;
                        }

                        if (opened != 0)
                            throw new ParseException(
                                    Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch(),
                                    new TraceInfo(context, startLine, 0, relativePosition, 0)
                            );

                        String sub = code.substring(i, j + 1);
                        segments.add(new StringExprToken.Segment(
                                sb.length(), sb.length() + sub.length(), dynamic == 2
                        ));
                        /*segments.add(new StringExprToken.Segment(
                                i - currentPosition - 1, j - currentPosition, dynamic == 2
                        ));*/
                        sb.append(sub);
                        i = j;
                        continue;
                    }
                }

                if (slash){
                    switch (ch){
                        case 'r': sb.append('\r'); slash = false; break;
                        case 'n': sb.append('\n'); slash = false; break;
                        case 't': sb.append('\t'); slash = false; break;
                        case 'e': sb.append((char)0x1B); slash = false; break;
                        case 'v': sb.append((char)0x0B); slash = false; break;
                        case 'f': sb.append('\f'); slash = false; break;
                        case '\\':
                            sb.append(ch);
                            slash = !slash;
                            break;
                        case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':
                            // \[0-7]{1,3}
                            int k = i + 1;
                            for(int j = 1; j < 3; j++){
                                k = i + j;
                                if (k < codeLength){
                                    char digit = code.charAt(k);
                                    if (digit >= '0' && digit <= '7'){
                                       // nop
                                    } else
                                        break;
                                } else
                                    break;
                            }

                            String s = code.substring(i, k);
                            if (s.isEmpty()){
                                sb.append(ch);
                            } else {
                                int val = Integer.parseInt(s, 8);
                                sb.append((char)val);
                            }

                            i = k - 1;
                            slash = false;
                            break;
                        case 'x':
                            int t = i + 1;
                            for(int j = 1; j < 5; j++){
                                t = i + j;
                                if (t < codeLength){
                                    char digit = code.charAt(t);
                                    if (Character.isDigit(digit) || (digit >= 'A' && digit <= 'F') || (digit >= 'a' && digit <= 'f')){
                                        // nop;
                                    } else {
                                        break;
                                    }
                                } else
                                    break;
                            }

                            String s16 = code.substring(i + 1, t);
                            if (s16.isEmpty()){
                                sb.append(ch);
                            } else  {
                                int val16 = Integer.parseInt(s16, 16);
                                sb.append((char)val16);
                            }
                            i = t - 1;
                            slash = false;
                            break;
                        case '$':
                        case '"':
                        default:
                            slash = false;
                            sb.append(ch); break;
                    }
                } else {
                    switch (ch){
                        case '\\':
                            slash = true;
                        break;
                        case '$':
                            int k = i + 1;
                            boolean done = false;
                            int opened = 0;
                            int complex = 0;
                            if (k < codeLength) {
                                char first = code.charAt(k);
                                if (TokenizeGrammarUtils.isEngLetter(first) || first == '_'){
                                    k++;
                                    done = true;
                                    for(; i < codeLength; k++){
                                        if (k < codeLength){
                                            first = code.charAt(k);
                                            if (Character.isDigit(first) || TokenizeGrammarUtils.isEngLetter(first) || first == '_') {
                                                // nop
                                            } else if (complex == 1 && TokenizeGrammarUtils.isVariableChar(first) && code.charAt(k - 1) == '[') {
                                                // nop
                                            } else if (complex == 0 && first == '[') {
                                                opened++;
                                                complex = 1;
                                            } else if (complex == 1 && opened != 0 && first == ']') {
                                                opened--;
                                                if (opened <= 0) {
                                                    k++;
                                                    break;
                                                }
                                            } else if (complex == 0 && first == '-'){
                                                if (k + 1 < codeLength && code.charAt(k + 1) == '>'){
                                                    k++;
                                                    complex = 2;
                                                } else
                                                    break;
                                            } else
                                                break;
                                        } else
                                            break;
                                    }
                                }
                            }

                            if (done){
                                if (opened != 0)
                                    throw new ParseException(
                                            Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch(),
                                            new TraceInfo(context, startLine, 0, pos, 0)
                                    );

                                String s = code.substring(i, k);
                                segments.add(new StringExprToken.Segment(sb.length(), sb.length() + s.length(), true));
                                sb.append(s);
                            } else
                                sb.append(ch);

                            i = k - 1;
                            break;
                        default:
                            sb.append(ch);
                    }
                }
            }
        }

        if (ch_quote != quote || slash){
            throw new ParseException(
                    Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch(),
                    new TraceInfo(context, currentLine, currentLine, pos, pos)
            );
        }

        TokenMeta meta = buildMeta(startPosition + 1, startLine);
        meta.setStartIndex(startIndex - 1);

        if (quote == StringExprToken.Quote.DOC) {
            meta.setEndIndex(currentPosition + 3);
        } else {
            meta.setEndIndex(currentPosition + 1);
        }

        meta.setWord(sb.toString());

        StringExprToken expr = new StringExprToken(meta, quote);
        expr.setSegments(segments);
        return expr;
    }

    protected Token readComment(CommentToken.Kind kind, int startPosition, int startLine){
        int i, pos = relativePosition, k = 0;

        boolean isOldComment = code.charAt(currentPosition) == '#';

        for(i = currentPosition + 1; i < codeLength; i++, k++){
            char ch = code.charAt(i);
            pos++;

            if (checkNewLine(ch))
                pos = 0;

            char prev_ch = i > 0 ? code.charAt(i - 1) : '\0';

            boolean closed = false;
            switch (kind){
                case SIMPLE:
                    closed = (TokenizeGrammarUtils.isNewline(ch));
                    if (TokenizeGrammarUtils.isCloseTag(prev_ch, ch)) {
                        i -= 2;
                        closed = true;
                    }

                    break;
                case DOCTYPE:
                case BLOCK:
                    closed = k != 0 && (TokenizeGrammarUtils.isCloseComment(String.valueOf(new char[]{prev_ch, ch}))); break;
            }

            closed = closed || i == codeLength - 1;
            if (closed){
                String text = code.substring(
                        startPosition,
                        kind == CommentToken.Kind.SIMPLE ? i : i - 1
                );

                TokenMeta meta = new TokenMeta(
                        text,
                        startLine, currentLine, startRelativePosition, relativePosition
                );

                meta.setStartIndex(currentPosition - 1);

                if (isOldComment || kind == CommentToken.Kind.DOCTYPE) {
                    meta.setStartIndex(currentPosition);
                }

                if (kind == CommentToken.Kind.BLOCK || kind == CommentToken.Kind.DOCTYPE) {
                    meta.setEndIndex(i + 1);
                } else {
                    meta.setEndIndex(i);
                }

                currentPosition = i;
                relativePosition = pos;
                Token result = buildToken(CommentToken.class, meta);

                if (kind == CommentToken.Kind.SIMPLE && text.startsWith("//")){
                    String directive = text.substring(2).trim();
                    if (directive.startsWith("@@")){
                        int p = directive.indexOf(' ');
                        if (p != -1){
                            String name = directive.substring(2, p);
                            String value = p + 1 < directive.length() ? directive.substring(p + 1) : "";
                            if (!directives.containsKey(name.toLowerCase()))
                                directives.put(name.toLowerCase(), new Directive(value, result.toTraceInfo(context)));
                        }
                    }
                }

                return result;
            }
        }
        assert false;
        return null;
    }

    protected Token readNumber(int startPosition, int startLine){
        int i;
        boolean dot = false;
        boolean e_char = false;

        i = currentPosition;
        boolean isHex = code.charAt(i) == '0'
                && (i < codeLength && Character.toLowerCase(code.charAt(i + 1)) == 'x');
        boolean isBinary = code.charAt(i) == '0' && (i < codeLength && code.charAt(i + 1) == 'b');
        if (isHex || isBinary)
            i += 2;

        for(; i < codeLength; i++){
            char ch = code.charAt(i);

            if (!isHex && TokenizeGrammarUtils.isFloatDot(ch)){
                if (dot)
                    break;
                dot = true;
            } else if (!isHex && (ch == 'e' || ch == 'E')){
                if (e_char)
                    break;

                if (i + 1 >= codeLength){
                    break;
                } else {
                    if (code.charAt(i + 1) == '-' || code.charAt(i + 1) == '+' ||
                            (i + 2 >= codeLength || Character.isDigit(code.charAt(i + 2)))) {
                        if (i + 2 >= codeLength || !Character.isDigit(code.charAt(i + 2))) {
                            break;
                        } else {
                            i++;
                        }
                    }
                }

                e_char = true;
            } else if (isHex && ((ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))) {
                // nop
            } else if (isBinary && (ch == '0' || ch == '1')) {
                // nop
            } else if (!Character.isDigit(ch))
                break;
        }

        currentPosition = i;
        TokenMeta meta = buildMeta(startPosition, startLine);
        Class<? extends Token> tokenClazz = tokenFinder.find(meta);

        currentPosition -= 1;
        return buildToken(tokenClazz, meta);
    }

    public Token nextToken(){
        boolean init = false;
        char ch = '\0';
        char prev_ch = '\0';
        int startPosition = currentPosition + 1;
        startRelativePosition = relativePosition;
        int startLine = currentLine;

        StringExprToken.Quote string = null;
        CommentToken.Kind comment = null;

        if (codeLength == 0) {
            return null;
        }

        boolean first = true;
        while (currentPosition < codeLength){
            currentPosition++;
            relativePosition++;

            if (currentPosition == codeLength) {
                break;
            }

            ch = code.charAt(currentPosition);
            
            if (currentPosition > 0 && init) {
                prev_ch = code.charAt(currentPosition - 1);
            }

            checkNewLine(ch);

            if (rawMode){
                if (TokenizeGrammarUtils.isOpenTag(prev_ch, ch)){
                    TokenMeta meta = new TokenMeta(
                            code.substring(startPosition, currentPosition - 1), startLine, currentLine,
                            startRelativePosition, relativePosition
                    );
                    rawMode = false;
                    startLine = currentLine;
                    startRelativePosition = relativePosition;
                    EchoRawToken token = buildToken(EchoRawToken.class, meta);

                    if (codeLength >= currentPosition + 4 &&
                            code.substring(currentPosition + 1, currentPosition + 4).equals("php")){
                        relativePosition += 4;
                        currentPosition += 3;
                        token.setShort(false);
                    } else {
                        token.setShort(true);
                    }
                    return token;
                } else {
                    init = true;
                    first = true;
                    continue;
                }
            }

            if (ch == '=' && prevToken != null && prevToken instanceof EchoRawToken && ((EchoRawToken) prevToken).isShort()){
                return buildToken(OpenEchoTagToken.class, buildMeta(startPosition, startLine));
            }

            if (first && (!init || prevToken == null)){
                // numbers: integers, doubles, hex
                if (Character.isDigit(ch)
                        || (ch == '.' && prevToken == null
                                && currentPosition + 1 < codeLength
                                && Character.isDigit(code.charAt(currentPosition + 1)))){
                    return readNumber(startPosition, startLine);
                }

                // comments
                comment = CommentToken.Kind.isComment(ch, prev_ch);
                if (comment != null) {
                    return readComment(comment, startPosition, startLine);
                }

                // strings, herdoc, etc.
                string = TokenizeGrammarUtils.isQuote(ch);
                if (string != null) {
                    return readString(string, startPosition, startLine);
                }
            }
            
            init = true;
            first = false;

            if (TokenizeGrammarUtils.isDelimiter(ch)) {
                if (startPosition == currentPosition && TokenizeGrammarUtils.isSpace(ch)){
                    startPosition = currentPosition + 1;
                    startLine = currentLine;
                    startRelativePosition = relativePosition;
                    prevToken = null;
                    first = true;
                    continue;
                }

                if (startPosition == currentPosition){
                    Token token = tryNextToken();
                    if (token instanceof BreakToken){
                        rawMode = true;
                    }

                    if (token instanceof CommentToken){
                        comment = ((CommentToken)token).getKind();
                        return readComment(comment, startPosition, startLine);
                        //continue;
                    }

                    if (token instanceof StringStartDocToken){
                        string = StringExprToken.Quote.DOC;
                        return readString(string, startPosition, startLine);
                    }

                    if (token != null) {
                        return token;
                    }
                }

                break;
            } else if (TokenizeGrammarUtils.isVariableChar(ch)){
                if (TokenizeGrammarUtils.isVariableChar(prev_ch)){
                    currentPosition -= 1;
                    break;
                }
            }
        }

        TokenMeta meta = buildMeta(startPosition, startLine);
        
        if (currentPosition != startPosition && TokenizeGrammarUtils.isDelimiter(ch)) {
            checkNewLine(ch, true);
            currentPosition -= 1;
            relativePosition -= 1;
        }

        if (meta == null)
            return null;

        //currentPosition -= 1;
        Class<? extends Token> tokenClazz = rawMode ? EchoRawToken.class : tokenFinder.find(meta);
        if (tokenClazz == null){
            return prevToken = new Token(meta, TokenType.T_J_CUSTOM);
        } else {
            return buildToken(tokenClazz, meta);
        }
    }

    public List<Token> fetchAll(){
        List<Token> result = new ArrayList<Token>();
        Token token;
        while ((token = nextToken()) != null)
            result.add(token);

        return result;
    }

    public Context getContext() {
        return context;
    }
}
