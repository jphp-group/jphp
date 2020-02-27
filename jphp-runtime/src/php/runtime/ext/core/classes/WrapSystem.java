package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.common.l10n.L10NMessages;
import php.runtime.common.l10n.Messages_RU;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.loader.RuntimeClassLoader;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\System")
final public class WrapSystem extends BaseObject {
    public WrapSystem(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Signature(@Arg("status"))
    public static Memory halt(Environment env, Memory... args) {
        System.exit(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({
            @Arg("name"),
            @Arg(value = "def", optional = @Optional(""))
    })
    public static Memory getProperty(Environment env, Memory... args) {
        return StringMemory.valueOf(System.getProperty(args[0].toString(), args[1].toString()));
    }

    @Signature
    public static String setProperty(String name, String value) {
        return System.setProperty(name, value);
    }

    @Signature
    @Name("getProperties")
    public static Properties getProperties1() {
        return System.getProperties();
    }

    @Signature
    public static void setProperties(Properties properties) {
        System.setProperties(properties);
    }

    @Signature
    public static Map<String, String> getEnv()  {
        return System.getenv();
    }

    @Signature
    public static Memory gc(Environment env, Memory... args) {
        System.gc();
        return Memory.NULL;
    }

    @Signature
    public static InputStream in() {
        return System.in;
    }

    @Signature
    public static OutputStream err() {
        return System.err;
    }

    @Signature
    public static OutputStream out() {
        return System.out;
    }

    @Signature
    public static void setIn(Environment env, @Arg(typeClass = "php\\io\\Stream", nullable = true) Memory stream) {
        if (stream.isNull()) {
            System.setIn(null);
        } else {
            System.setIn(Stream.getInputStream(env, stream));
        }
    }

    @Signature
    public static void setOut(Environment env, @Arg(typeClass = "php\\io\\Stream", nullable = true) Memory stream)
            throws UnsupportedEncodingException {
        setOut(env, stream, Memory.NULL);
    }

    @Signature
    public static void setOut(Environment env, @Arg(typeClass = "php\\io\\Stream", nullable = true) Memory stream, Memory encoding)
            throws UnsupportedEncodingException {
        if (stream.isNull()) {
            System.setOut(null);
        } else {
            if (encoding.isNotNull()) {
                System.setOut(new PrintStream(Stream.getOutputStream(env, stream), true, encoding.toString()));
            } else {
                System.setOut(new PrintStream(Stream.getOutputStream(env, stream), true));
            }
        }
    }

    @Signature
    public static void setErr(Environment env, @Arg(typeClass = "php\\io\\Stream", nullable = true) Memory stream)
            throws UnsupportedEncodingException {
        setOut(env, stream, Memory.NULL);
    }

    @Signature
    public static void setErr(Environment env, @Arg(typeClass = "php\\io\\Stream", nullable = true) Memory stream, Memory encoding)
            throws UnsupportedEncodingException {
        if (stream.isNull()) {
            System.setErr(null);
        } else {
            if (encoding.isNotNull()) {
                System.setErr(new PrintStream(Stream.getOutputStream(env, stream), true, encoding.toString()));
            } else {
                System.setErr(new PrintStream(Stream.getOutputStream(env, stream), true));
            }
        }
    }

    @Signature
    public static String tempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    @Signature
    public static String userHome() {
        return System.getProperty("user.home");
    }

    @Signature
    public static String userDirectory() {
        return System.getProperty("user.dir");
    }

    @Signature
    public static String userName() {
        return System.getProperty("user.name");
    }

    @Signature
    public static String osName() {
        return System.getProperty("os.name");
    }

    @Signature
    public static String osVersion() {
        return System.getProperty("os.version");
    }

    @Signature
    public static void addClassPath(Environment env, File file) throws IOException {
        try {
            env.getScope().getClassLoader().addLibrary(file.toURI().toURL());
        } catch (Throwable t) {
            throw new IOException("Error, could not add URL to system classloader, " + t.getMessage());
        }
    }

    @Signature
    public static void setEngineLanguage(Memory nameOrClass) throws Throwable {
        Messages.ERR_FILE_NOT_FOUND.fetch(); // fix loading when static

        if (nameOrClass.isNull() || "en".equals(nameOrClass.toString())) {
            Messages.localize(null);
        } else {
            L10NMessages messages = L10NMessages.get(nameOrClass.toString());
            if (messages == null) {
                throw new IllegalArgumentException(nameOrClass + " language is not found in engine");
            }
            Messages.localize(messages);
        }
    }
}
