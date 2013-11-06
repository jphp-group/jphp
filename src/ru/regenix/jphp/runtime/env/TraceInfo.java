package ru.regenix.jphp.runtime.env;

public class TraceInfo {
    private Context context;

    private int startLine;
    private int endLine;

    private int startPosition;
    private int endPosition;

    public TraceInfo(Context context, int startLine, int endLine, int startPosition, int endPosition) {
        this.context = context;
        this.startLine = startLine;
        this.endLine = endLine;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public TraceInfo(Context context){
        this(context, 0, 0, 0, 0);
    }

    public Context getContext() {
        return context;
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
