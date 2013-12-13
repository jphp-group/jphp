package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.runtime.memory.support.Memory;

public class DieException extends RuntimeException {
    protected int exitCode = 0;

    public DieException(Memory value){
        super(value.toString());
        if (value.isNumber())
            exitCode = value.toInteger();
    }

    public int getExitCode() {
        return exitCode;
    }
}
