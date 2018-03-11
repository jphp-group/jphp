package org.develnext.jphp.ext.javafx.bind;

import javafx.geometry.Bounds;
import javafx.scene.control.IndexRange;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;

public class IndexRangeMemoryOperation extends MemoryOperation<IndexRange> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { IndexRange.class };
    }

    @Override
    public IndexRange convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        throw new IllegalStateException("Unable to convert memory to IndexRange");
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, IndexRange arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("start").assign(arg.getStart());
        r.refOfIndex("end").assign(arg.getEnd());
        r.refOfIndex("length").assign(arg.getLength());

        return r.toConstant();
    }
}
