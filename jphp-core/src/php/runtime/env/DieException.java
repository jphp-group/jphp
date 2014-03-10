package php.runtime.env;

import php.runtime.Memory;
import php.runtime.exceptions.JPHPException;

public class DieException extends RuntimeException implements JPHPException {
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
