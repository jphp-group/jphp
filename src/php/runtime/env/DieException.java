package php.runtime.env;

import php.runtime.Memory;

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
