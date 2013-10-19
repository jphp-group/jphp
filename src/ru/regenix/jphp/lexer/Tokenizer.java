package ru.regenix.jphp.lexer;

import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.StringExprToken;

public class Tokenizer {

    private int currentPosition;
    private int currentLine;

    private final int codeLength;
    protected final String code;

    public Tokenizer(String code){
        this.currentPosition = -1;
        this.currentLine = 0;
        this.code = code;
        this.codeLength = code.length();
    }

    public String getCode() {
        return code;
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

        if (endPosition == startPosition)
            return code.substring(startPosition, startPosition + 1);

        return code.substring(startPosition, endPosition);
    }

    public Token nextToken(){
        char ch;
        int startPosition = currentPosition + 1;
        int startLine = currentLine;

        StringExprToken.Quote string = null;
        boolean prevBackslash = false;

        while (currentPosition < codeLength - 1){
            currentPosition++;
            ch = code.charAt(currentPosition);

            if (GrammarUtils.isNewline(ch))
                currentLine++;

            if (string == null){
                string = GrammarUtils.isQuote(ch);
                if (string != null)
                    continue;

                if (GrammarUtils.isDelimiter(ch)){
                    if (startPosition == currentPosition && GrammarUtils.isSpace(ch)){
                        startPosition = currentPosition + 1;
                        continue;
                    }
                    break;
                }
            } else {
                if (GrammarUtils.isBackslash(ch)){
                    prevBackslash = !prevBackslash;
                    continue;
                }

                if (!prevBackslash){
                    if (GrammarUtils.isQuote(ch) == string){
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
        return meta == null ? null : new Token(meta);
    }

}
