package ru.regenix.jphp.env;

import java.io.File;

public class Context {
    public enum Mode { PHP, J_PHP }

    protected final Environment environment;
    protected final File file;
    protected final Mode mode;

    public Context(Environment environment, File file, Mode mode) {
        this.file = file;
        this.environment = environment;
        this.mode = mode;
    }

    public Context(Environment environment, File file) {
        this(environment, file, Mode.PHP);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public File getFile() {
        return file;
    }

    public Mode getMode() {
        return mode;
    }
}
