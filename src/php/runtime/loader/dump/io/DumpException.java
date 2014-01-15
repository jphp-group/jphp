package php.runtime.loader.dump.io;

import java.io.IOException;

public class DumpException extends IOException {

    public DumpException() {
    }

    public DumpException(String message) {
        super(message);
    }
}
