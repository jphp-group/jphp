package ru.regenix.jphp.lexer;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenFinder;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.StringExprToken;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    protected Context context;
    protected TokenFinder tokenFinder;

    private int currentPosition;
    private int currentLine;

    private final int codeLength;
    protected final String code;

    protected Token prevToken;

    public Tokenizer(Context context){
        this.context = context;
        this.currentPosition = -1;
        this.currentLine = 0;
        this.code = context.getContent();
        this.codeLength = code.length();
        this.tokenFinder = new TokenFinder();
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
        return new TokenMeta(word, startLine, currentLine, startPosition, currentPosition);
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
        if (tokenClass != null){
            int startPosition = currentPosition;
            currentPosition += len + 1;
            Token token = buildToken(tokenClass, buildMeta(startPosition, currentLine));
            currentPosition -= 1;
            return token;
        }
        return null;
    }

    public Token nextToken(){
        boolean init = false;
        char ch = '\0';
        char prev_ch = '\0';
        int startPosition = currentPosition + 1;
        int startLine = currentLine;

        StringExprToken.Quote string = null;
        boolean prevBackslash = false;

        if (codeLength == 0)
            return null;

        while (currentPosition < codeLength){
            currentPosition++;
            if (currentPosition == codeLength)
                break;

            ch = code.charAt(currentPosition);
            if (currentPosition > 0 && init)
                prev_ch = code.charAt(currentPosition - 1);

            if (GrammarUtils.isNewline(ch))
                currentLine++;

            init = true;
            if (string == null) {
                string = GrammarUtils.isQuote(ch);
                if (string != null)
                    continue;

                if (GrammarUtils.isDelimiter(ch)){
                    if (GrammarUtils.isFloatDot(ch) && (prev_ch == '\0' || GrammarUtils.isNumeric(prev_ch))) {
                         // double value
                    } else {
                        if (startPosition == currentPosition && GrammarUtils.isSpace(ch)){
                            startPosition = currentPosition + 1;
                            continue;
                        }
                        if (startPosition == currentPosition){
                            Token token = tryNextToken();
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
        if (currentPosition != startPosition && GrammarUtils.isDelimiter(ch))
            currentPosition -= 1;

        if (meta == null)
            return null;

        //currentPosition -= 1;

        if (string != null)
            context.triggerError(new ParseException(
                    Messages.ERR_PARSE_UNEXPECTED_END_OF_FILE.fetch(), meta.toTraceInfo(context)
            ));

        Class<? extends Token> tokenClazz = tokenFinder.find(meta);
        if (tokenClazz == null)
            return prevToken = new Token(meta, TokenType.T_J_CUSTOM);
        else {
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
