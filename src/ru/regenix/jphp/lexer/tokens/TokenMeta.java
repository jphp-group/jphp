package ru.regenix.jphp.lexer.tokens;


import ru.regenix.jphp.env.TraceInfo;

import java.io.File;
import java.util.List;

public class TokenMeta {
    protected final String word;

    protected final int startPosition;
    protected final int endPosition;

    protected final int startLine;
    protected final int endLine;

    public TokenMeta(String word, int startLine, int endLine, int startPosition, int endPosition) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.word = word;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public static TokenMeta of(List<? extends Token> tokens){
        int startPosition = 0, startLine = 0, endPosition = 0, endLine = 0;

        StringBuilder builder = new StringBuilder();
        int i = 0;
        int size = tokens.size();
        for(Token token : tokens){
            if (i == 0){
                startPosition = token.getMeta().startPosition;
                startLine = token.getMeta().startLine;
            }
            builder.append(token.getMeta().word);
            if (i == size - 1){
                endPosition = token.getMeta().endPosition;
                endLine = token.getMeta().endLine;
            }
            i++;
        }

        return new TokenMeta(builder.toString(), startLine, endLine, startPosition, endPosition);
    }

    public String getWord() {
        return word;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public TraceInfo toTraceInfo(File file){
        return new TraceInfo(file, startLine, endLine, startPosition, endPosition);
    }
}
