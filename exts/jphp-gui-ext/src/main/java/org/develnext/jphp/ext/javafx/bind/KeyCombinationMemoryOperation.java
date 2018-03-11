package org.develnext.jphp.ext.javafx.bind;

import javafx.scene.input.KeyCombination;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

public class KeyCombinationMemoryOperation extends MemoryOperation<KeyCombination> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { KeyCombination.class };
    }

    @Override
    public KeyCombination convert(Environment environment, TraceInfo traceInfo, Memory memory) {
        return memory.isNull() ? null : KeyCombination.valueOf(memory.toString());
    }

    @Override
    public Memory unconvert(Environment environment, TraceInfo traceInfo, KeyCombination keyCombination) {
        return keyCombination == null ? Memory.NULL : StringMemory.valueOf(keyCombination.toString());
    }
}
