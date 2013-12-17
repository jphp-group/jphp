package ru.regenix.jphp.tokenizer;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.tokenizer.token.*;
import ru.regenix.jphp.tokenizer.token.expr.value.StringExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.EchoRawToken;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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

    public Tokenizer(Context context){
        this.context = context;
        this.currentPosition = -1;
        this.currentLine = 0;
        this.relativePosition = -1;
        this.code = context.getContent();
        this.codeLength = code.length();
        this.tokenFinder = new TokenFinder();
        this.rawMode = context.isFile();
    }

    public Tokenizer(String code, Context context){
        this.context = context;
        this.currentPosition = -1;
        this.currentLine = 0;
        this.relativePosition = -1;
        this.code = code;
        this.codeLength = code.length();
        this.tokenFinder = new TokenFinder();
        this.rawMode = false;
    }

    public String getCode() {
        return code;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Token> T buildToken(Class<T> clazz, TokenMeta meta){
        try {
            return (T) (prevToken = clazz.getConstructor(TokenMeta.class).newInstance(meta));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected TokenMeta buildMeta(int startPosition, int startLine){
        String word = getWord(startPosition, currentPosition);
        if (word == null)
            return null;
        return new TokenMeta(word, startLine, currentLine, startRelativePosition, relativePosition);
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
            if (GrammarUtils.isDelimiter(ch) && !GrammarUtils.isSpace(ch)){
                len = 1;
                if (currentPosition + 2 < codeLength){
                    ch = code.charAt(currentPosition + 2);
                    if (GrammarUtils.isDelimiter(ch) && !GrammarUtils.isSpace(ch)){
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
                if (GrammarUtils.isNewline(code.charAt(currentPosition)))
                    currentLine--;
            }
        }
    }

    protected boolean checkNewLine(char ch, boolean invert){
        if (GrammarUtils.isNewline(ch)){
            if (invert)
                currentLine--;
            else
                currentLine++;

            relativePosition = 0;
            startRelativePosition = 0;
            return true;
        }
        return false;
    }

    protected boolean checkNewLine(char ch){
        return checkNewLine(ch, false);
    }

    protected StringExprToken readString(StringExprToken.Quote quote, int startPosition, int startLine){
        int i = currentPosition + 1, pos = relativePosition + 1;
        StringExprToken.Quote ch_quote = null;
        boolean slash = false;
        StringBuilder sb = new StringBuilder();

        boolean isMagic = quote == StringExprToken.Quote.DOUBLE;
        String endString = null;

        if (quote == StringExprToken.Quote.DOC){
            StringBuilder tmp = new StringBuilder();
            StringExprToken.Quote docType = null;

            for(; i < codeLength; i++){
                char ch = code.charAt(i);
                pos++;

                if (docType == null && GrammarUtils.isQuote(ch) != null) {
                    docType = GrammarUtils.isQuote(ch);
                } else if (docType != null && docType == GrammarUtils.isQuote(ch)){
                    if (i + 1 >= codeLength || !GrammarUtils.isNewline(code.charAt(i + 1))){
                        throw new ParseException(
                                Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch(),
                                new TraceInfo(context, currentLine, currentLine, pos + 1, pos + 1)
                        );
                    }
                    i += 1;
                    break;
                    // nop
                } else if (GrammarUtils.isEngLetter(ch) || (tmp.length() != 0 && Character.isDigit(ch))){
                    tmp.append(ch);
                } else if (tmp.length() > 0 && checkNewLine(ch)){
                    pos = 0;
                    break;
                } else {
                    String error = Messages.ERR_PARSE_UNEXPECTED_X.fetch(ch);
                    if (GrammarUtils.isNewline(ch))
                        error = Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch();

                    throw new ParseException(
                            error,
                            new TraceInfo(context, currentLine, currentLine, pos, pos)
                    );
                }
            }

            currentPosition = i;
            i += 1; // skip \n

            isMagic = (docType == null || docType == StringExprToken.Quote.DOUBLE);
            endString = tmp.toString();
        }

        List<StringExprToken.Segment> segments = new ArrayList<StringExprToken.Segment>();


        for(; i < codeLength; i++){
            char ch = code.charAt(i);

            pos++;
            ch_quote = GrammarUtils.isQuote(ch);
            if (endString == null && (ch_quote == quote && !slash)){
                currentPosition  = i;
                relativePosition = pos;
                break;
            }

            if (checkNewLine(ch)) {
                pos = 0;
                if (endString != null){
                    int end = i + 1 + endString.length() + 1;
                    if (end < codeLength){
                        String tmp = code.substring(i + 1, end - 1);
                        if (tmp.equals(endString)
                                && code.charAt(end - 1) == ';'
                                && GrammarUtils.isNewline(code.charAt(end))){
                            currentPosition = i + endString.length();
                            relativePosition = endString.length();
                            ch_quote = StringExprToken.Quote.DOC;
                            break;
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
                if (ch == '$' && (i + 1 < codeLength && code.charAt(i + 1) == '{') ) {
                    dynamic = 2;
                }
                if (ch == '{')
                    dynamic = 1;

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
                                i - currentPosition - 1, j - currentPosition, dynamic == 2
                        ));
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
                                if (GrammarUtils.isEngLetter(first)){
                                    k++;
                                    done = true;
                                    for(; i < codeLength; k++){
                                        if (k < codeLength){
                                            first = code.charAt(k);
                                            if (Character.isDigit(first) || GrammarUtils.isEngLetter(first)){
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

                                segments.add(new StringExprToken.Segment(i - currentPosition - 1, k - currentPosition - 1, true));
                                sb.append(code.substring(i, k));
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
        meta.setWord(sb.toString());

        StringExprToken expr = new StringExprToken(meta, quote);
        expr.setSegments(segments);
        return expr;
    }

    protected Token readComment(CommentToken.Kind kind, int startPosition, int startLine){
        int i, pos = relativePosition;
        for(i = currentPosition + 1; i < codeLength; i++){
            char ch = code.charAt(i);
            pos++;

            if (checkNewLine(ch))
                pos = 0;

            char prev_ch = i > 0 ? code.charAt(i - 1) : '\0';

            boolean closed = false;
            switch (kind){
                case SIMPLE:
                    closed = (GrammarUtils.isNewline(ch)); break;
                case DOCTYPE:
                case BLOCK:
                    closed = (GrammarUtils.isCloseComment(String.valueOf(new char[]{prev_ch, ch}))); break;
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
                currentPosition = i;
                relativePosition = pos;
                return buildToken(CommentToken.class, meta);
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
        boolean is_hex = code.charAt(i) == '0'
                && (i < codeLength && Character.toLowerCase(code.charAt(i + 1)) == 'x');

        if (is_hex)
            i += 2;

        for(; i < codeLength; i++){
            char ch = code.charAt(i);

            if (!is_hex && GrammarUtils.isFloatDot(ch)){
                if (dot)
                    break;
                dot = true;
            } else if (!is_hex && (ch == 'e' || ch == 'E')){
                if (e_char)
                    break;

                if (i + 1 >= codeLength){
                    break;
                } else if (code.charAt(i + 1) == '-' || code.charAt(i + 1) == '+' ||
                        Character.isDigit(code.charAt(i + 2))) {
                    if (i + 2 >= codeLength || !Character.isDigit(code.charAt(i + 2))) {
                        break;
                    } else
                        i++;
                }
                e_char = true;
            } else if (is_hex && ((ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))){
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

        if (codeLength == 0)
            return null;

        while (currentPosition < codeLength){
            currentPosition++;
            relativePosition++;
            if (currentPosition == codeLength)
                break;

            ch = code.charAt(currentPosition);
            if (currentPosition > 0 && init)
                prev_ch = code.charAt(currentPosition - 1);

            checkNewLine(ch);


            if (rawMode){
                if (GrammarUtils.isOpenTag(String.valueOf(new char[]{prev_ch, ch}))){
                    TokenMeta meta = new TokenMeta(
                            code.substring(startPosition, currentPosition - 1), startLine, currentLine,
                            startRelativePosition, relativePosition
                    );
                    rawMode = false;
                    startLine = currentLine;
                    startRelativePosition = relativePosition;
                    EchoRawToken token = buildToken(EchoRawToken.class, meta);

                    if (code.substring(currentPosition + 1, currentPosition + 4).equals("php")){
                        relativePosition += 4;
                        currentPosition += 3;
                        token.setShort(false);
                    } else
                        token.setShort(true);

                    return token;
                } else {
                    init = true;
                    continue;
                }
            }

            if (ch == '=' && prevToken != null && prevToken instanceof EchoRawToken && ((EchoRawToken) prevToken).isShort()){
                return buildToken(OpenEchoTagToken.class, buildMeta(startPosition, startLine));
            }

            if (!init || prevToken == null){
                // numbers: integers, doubles, hex
                if (Character.isDigit(ch)){
                    return readNumber(startPosition, startLine);
                }

                // comments
                comment = CommentToken.Kind.isComment(ch, prev_ch);
                if (comment != null) {
                    return readComment(comment, startPosition, startLine);
                }

                // strings, herdoc, etc.
                string = GrammarUtils.isQuote(ch);
                if (string != null) {
                    return readString(string, startPosition, startLine);
                }
            }
            init = true;

            if (GrammarUtils.isDelimiter(ch)){
                if (startPosition == currentPosition && GrammarUtils.isSpace(ch)){
                    startPosition = currentPosition + 1;
                    startLine = currentLine;
                    startRelativePosition = relativePosition;
                    prevToken = null;
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

                    if (token != null)
                        return token;
                }

                break;
            } else if (GrammarUtils.isVariableChar(ch)){
                if (GrammarUtils.isVariableChar(prev_ch)){
                    currentPosition -= 1;
                    break;
                }
            }
        }

        TokenMeta meta = buildMeta(startPosition, startLine);
        if (currentPosition != startPosition && GrammarUtils.isDelimiter(ch)){
            currentPosition -= 1;
            relativePosition -= 1;
        }

        if (meta == null)
            return null;

        //currentPosition -= 1;
        Class<? extends Token> tokenClazz = tokenFinder.find(meta);
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

    public void reset(){
        this.currentPosition = -1;
        this.currentLine = 0;
    }

    public Context getContext() {
        return context;
    }

    public File getFile(){
        return context.getFile();
    }
}
