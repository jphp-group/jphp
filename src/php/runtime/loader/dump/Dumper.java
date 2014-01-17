package php.runtime.loader.dump;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.reflection.support.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract public class Dumper<T extends Entity> {
    protected final Context context;
    protected final boolean debugInformation;
    protected final Environment env;

    protected Dumper(Context context, Environment env, boolean debugInformation) {
        this.context = context;
        this.env = env;
        this.debugInformation = debugInformation;
    }

    private final static int MAX_LENGTH_OF_RAW_DATA = 1024 * 200; // 200 kb

    abstract public int getType();

    abstract public void save(T entity, OutputStream output) throws IOException;
    abstract public T load(InputStream input) throws IOException;
}
