package php.runtime.env;

import java.io.File;

public class SourceMappedTraceInfo extends TraceInfo {
    public SourceMappedTraceInfo(StackTraceElement element) {
        super(element);
    }

    public SourceMappedTraceInfo(String fileName, int startLine, int startPosition) {
        super(fileName, startLine, startPosition);
    }

    public SourceMappedTraceInfo(Context context, int startLine, int endLine, int startPosition, int endPosition) {
        super(context, startLine, endLine, startPosition, endPosition);
    }

    public SourceMappedTraceInfo(Context context) {
        super(context);
    }
}
