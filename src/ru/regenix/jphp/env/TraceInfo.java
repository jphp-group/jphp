package ru.regenix.jphp.env;

import java.io.File;

public class TraceInfo {

    private File file;

    private int startLine;
    private int endLine;

    private int startPosition;
    private int endPosition;

    public TraceInfo(File file, int startLine, int endLine, int startPosition, int endPosition) {
        this.file = file;
        this.startLine = startLine;
        this.endLine = endLine;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public File getFile() {
        return file;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }
}
