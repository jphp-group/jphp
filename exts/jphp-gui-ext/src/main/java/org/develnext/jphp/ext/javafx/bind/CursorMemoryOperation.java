package org.develnext.jphp.ext.javafx.bind;

import javafx.scene.Cursor;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

public class CursorMemoryOperation extends MemoryOperation<Cursor> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Cursor.class };
    }

    @Override
    public Cursor convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? null : Cursor.cursor(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Cursor arg) throws Throwable {
        return arg == null ? Memory.NULL : StringMemory.valueOf(arg.toString());
    }
}
