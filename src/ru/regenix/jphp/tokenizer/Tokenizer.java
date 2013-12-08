package ru.regenix.jphp.tokenizer;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.tokenizer.token.*;
import ru.regenix.jphp.tokenizer.token.stmt.EchoRawToken;
import ru.regenix.jphp.tokenizer.token.expr.value.StringExprToken;

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

    protected Token buildToken(Class<? extends Token> clazz, TokenMeta meta){
        try {
            return prevToken = clazz.getConstructor(TokenMeta.class).newInstance(meta);
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

    protected StringExprToken readString(StringExprToken.Quote quote, int startPosition, int startLine){
        int i;
        StringExprToken.Quote ch_quote = null;
        boolean slash = false;
        StringBuilder sb = new StringBuilder();

        boolean isMagic = quote == StringExprToken.Quote.DOUBLE;

        List<StringExprToken.Segment> segments = new ArrayList<StringExprToken.Segment>();

        for(i = currentPosition + 1; i < codeLength; i++){
            char ch = code.charAt(i);

            ch_quote = GrammarUtils.isQuote(ch);
            if (ch_quote == quote && !slash){
                currentPosition = i;
                break;
            }

            if (GrammarUtils.isNewline(ch))
                currentLine++;

            if (!isMagic){
                switch (ch){
                    case '\\':
                        if (slash)
                            sb.append(ch);
                        slash = !slash;
                        break;
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
                            if (GrammarUtils.isNewline(code.charAt(j)))
                                currentLine++;

                            if (opened == 0)
                                break;
                        }

                        if (opened != 0)
                            throw new ParseException(
                                    Messages.ERR_PARSE_UNEXPECTED_END_OF_STRING.fetch(),
                                    new TraceInfo(context, startLine, 0, startPosition, 0)
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
                                            new TraceInfo(context, startLine, 0, startPosition, 0)
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
                    new TraceInfo(context, currentLine, currentLine, currentLine, currentLine)
            );
        }

        TokenMeta meta = buildMeta(startPosition + 1, startLine);
        meta.setWord(sb.toString());

        StringExprToken expr = new StringExprToken(meta, quote);
        expr.setSegments(segments);
        return expr;
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
        boolean prevBackslash = false;

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

            if (GrammarUtils.isNewline(ch)){
                currentLine++;
                relativePosition = 0;
                startRelativePosition = 0;
            }

            init = true;
            if (string == null && comment == null) {
                if (rawMode){
                    if (GrammarUtils.isOpenTag(String.valueOf(new char[]{prev_ch, ch}))){
                        TokenMeta meta = new TokenMeta(
                                code.substring(startPosition, currentPosition - 1), startLine, currentLine,
                                startRelativePosition, relativePosition
                        );
                        rawMode = false;
                        startLine = currentLine;
                        startRelativePosition = relativePosition;
                        return buildToken(EchoRawToken.class, meta);
                    } else {
                        continue;
                    }
                }

                if (ch == '=' && prevToken instanceof EchoRawToken){
                    return buildToken(OpenEchoTagToken.class, buildMeta(startPosition, startLine));
                }

                comment = CommentToken.Kind.isComment(ch, prev_ch);
                if (comment != null)
                    continue;

                string = GrammarUtils.isQuote(ch);
                if (string != null) {
                    return readString(string, startPosition, startLine);
                    //continue;
                }

                if (GrammarUtils.isDelimiter(ch)){
                    if (GrammarUtils.isFloatDot(ch) && (prev_ch == '\0' || GrammarUtils.isNumeric(prev_ch))) {
                         // double value
                    } else {
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
                                continue;
                            }
                            if (token != null)
                                return token;
                        }

                        break;
                    }
                } else if (GrammarUtils.isVariableChar(ch)){
                    if (GrammarUtils.isVariableChar(prev_ch)){
                        currentPosition -= 1;
                        break;
                    }
                }
            } else if (comment != null){
                boolean closed = false;
                switch (comment){
                    case SIMPLE:
                        closed = (GrammarUtils.isNewline(ch)); break;
                    case DOCTYPE:
                    case BLOCK:
                        closed = (GrammarUtils.isCloseComment(String.valueOf(new char[]{prev_ch, ch}))); break;
                }
                if (closed){
                    String text = code.substring(
                            startPosition,
                            comment == CommentToken.Kind.SIMPLE ? currentPosition : currentPosition - 1
                    );

                    TokenMeta meta = new TokenMeta(
                            text,
                            startLine, currentLine, startRelativePosition, relativePosition
                    );
                    return buildToken(CommentToken.class, meta);
                }
            } else {
                if (GrammarUtils.isBackslash(ch)){
                    prevBackslash = !prevBackslash;
                    continue;
                }

                if (!prevBackslash){
                    if (GrammarUtils.isQuote(ch) == string){
                        if (GrammarUtils.isQuote(prev_ch) == string){
                            return new StringExprToken(
                                    new TokenMeta("", startLine, startLine, startPosition, startPosition),
                                    string
                            );
                        } else
                        return new StringExprToken(
                                buildMeta(startPosition + 1, startLine),
                                string
                        );
                    }
                }
            }

            prevBackslash = false;
        }

        TokenMeta meta = buildMeta(startPosition, startLine);
        if (currentPosition != startPosition && GrammarUtils.isDelimiter(ch)){
            currentPosition -= 1;
            relativePosition -= 1;
        }

        if (meta == null)
            return null;

        //currentPosition -= 1;

        if (string != null)
            context.triggerError(new ParseException(
                    Messages.ERR_PARSE_UNEXPECTED_END_OF_FILE.fetch(), meta.toTraceInfo(context)
            ));

        Class<? extends Token> tokenClazz = tokenFinder.find(meta);
        if (tokenClazz == null){
            return prevToken = new Token(meta, TokenType.T_J_CUSTOM);
        } else {
            if (prevToken instanceof EchoRawToken && tokenClazz == NameToken.class && "php".equals(meta.getWord())){
                prevToken = null;
                return nextToken();
            }
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
