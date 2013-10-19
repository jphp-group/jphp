package ru.regenix.jphp.lexer.tokens;


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
}
