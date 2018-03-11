package org.develnext.jphp.ext.javafx.bind;

import javafx.print.Paper;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;

public class PaperMemoryOperation extends MemoryOperation<Paper> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] {Paper.class};
    }

    @Override
    public Paper convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        throw new IllegalStateException("Unable to convert memory to " + Paper.class.getName());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Paper arg) throws Throwable {
        ArrayMemory p = new ArrayMemory();
        p.refOfIndex("name").assign(arg.getName());
        p.refOfIndex("width").assign(arg.getWidth());
        p.refOfIndex("height").assign(arg.getHeight());
        return p;
    }
}
