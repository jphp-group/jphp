package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

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
}
