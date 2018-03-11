package org.develnext.jphp.ext.javafx.bind;

import javafx.print.Paper;
import javafx.print.PrintResolution;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;

public class PrintResolutionMemoryOperation extends MemoryOperation<PrintResolution> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { PrintResolution.class };
    }

    @Override
    public PrintResolution convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        throw new IllegalStateException("Unable to convert memory to " + PrintResolution.class.getName());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, PrintResolution resolution) throws Throwable {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("crossFeedResolution").assign(resolution.getCrossFeedResolution());
        r.refOfIndex("feedResolution").assign(resolution.getFeedResolution());
        return r;
    }
}
