package org.develnext.jphp.core.tokenizer;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
import org.develnext.jphp.core.tokenizer.token.Token;

import java.util.Arrays;
import java.util.List;

public class TokenMeta {
    protected String word;

    protected final int startPosition;
    protected int endPosition;

    protected final int startLine;
    protected int endLine;

    protected int startIndex = -1;
    protected int endIndex = -1;

    public TokenMeta(String word, int startLine, int endLine, int startPosition, int endPosition) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.word = word;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public static TokenMeta of(List<? extends Token> tokens){
        int startPosition = 0, startLine = 0, endPosition = 0, endLine = 0, startIndex = -1, endIndex = -1;

        StringBuilder builder = new StringBuilder();
        int i = 0;
        int size = tokens.size();

        for(Token token : tokens){
            if (token == null) continue;

            if (i == 0){
                startIndex = token.getMeta().startIndex;
                startPosition = token.getMeta().startPosition;
                startLine = token.getMeta().startLine;
            }

            builder.append(token.getMeta().word);
            if (i == size - 1){
                endPosition = token.getMeta().endPosition;
                endLine = token.getMeta().endLine;
                endIndex = token.getMeta().endIndex;
            }

            i++;
        }

        TokenMeta meta = new TokenMeta(builder.toString(), startLine, endLine, startPosition, endPosition);
        meta.setStartIndex(startIndex);
        meta.setEndIndex(endIndex);

        return meta;
    }

    public static TokenMeta of(Token... tokens){
        return of(Arrays.asList(tokens));
    }

    public static TokenMeta of(String word, Token token){
        TokenMeta tokenMeta = new TokenMeta(
                word,
                token.getMeta().startLine,
                token.getMeta().endLine,
                token.getMeta().startPosition,
                token.getMeta().endPosition
        );
        tokenMeta.setStartIndex(token.getMeta().getStartIndex());
        tokenMeta.setEndIndex(token.getMeta().getEndIndex());
        return tokenMeta;
    }

    public static TokenMeta of(String word){
        return new TokenMeta(
                word,
                0, 0, 0, 0
        );
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
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

    public TraceInfo toTraceInfo(Context context){
        return new TraceInfo(context, startLine, endLine, startPosition, endPosition);
    }

    private static final TokenMeta empty = new TokenMeta("", 0, 0, 0, 0);

    public static TokenMeta empty(){
        return empty;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
