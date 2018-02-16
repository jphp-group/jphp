package php.runtime.ext.core.classes.format;

import php.runtime.Memory;
import php.runtime.common.GrammarUtils;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseException;
import php.runtime.lang.BaseObject;
import php.runtime.lang.IObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.support.ReflectionUtils;

import static php.runtime.annotation.Reflection.*;

@Name("php\\format\\Processor")
abstract public class WrapProcessor extends BaseObject {
    public WrapProcessor(Environment env) {
        super(env);
    }

    public WrapProcessor(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(@Arg("value"))
    abstract public Memory parse(Environment env, Memory... args);

    @Signature(@Arg("string"))
    abstract public Memory format(Environment env, Memory... args);

    @Signature({@Arg("string"), @Arg(value = "output", nativeType = Stream.class)})
    abstract public Memory formatTo(Environment env, Memory... args);

    @Signature({
            @Arg("code")
    })
    public static Memory isRegistered(Environment env, Memory... args) {
        String code = args[0].toString();
        String className = env.getUserValue(ProcessorException.class.getName() + "#" + code, String.class);

        return TrueMemory.valueOf(className != null);
    }

    @Signature({
            @Arg("code"), @Arg("processorClass")
    })
    public static Memory register(Environment env, Memory... args) {
        String code = args[0].toString();
        String className = args[1].toString();

        if (code.isEmpty()) {
            throw new IllegalArgumentException("Argument 1 (code) must be not empty");
        }

        if (!code.matches("^[A-Za-z0-9]+$")) {
            throw new IllegalArgumentException("Invalid Argument 1 (code)");
        }

        ClassEntity classEntity = env.fetchClass(className, true);
        if (classEntity == null) {
            throw new IllegalArgumentException(Messages.ERR_CLASS_NOT_FOUND.fetch(className));
        }

        if (!classEntity.isInstanceOf(WrapProcessor.class)) {
            throw new IllegalArgumentException("Class " + className + " is not processor class");
        }

        env.setUserValue(WrapProcessor.class.getName() + "#" + args[1].toString(), classEntity.getName());
        return Memory.NULL;
    }

    @Signature({@Arg("code")})
    public static Memory unregister(Environment env, Memory... args) {
        String protocol = args[0].toString();

        if (protocol.isEmpty()) {
            return Memory.FALSE;
        }

        env.removeUserValue(WrapProcessor.class.getName() + "#" + protocol + ".flags");
        return env.removeUserValue(WrapProcessor.class.getName() + "#" + protocol) ? Memory.TRUE : Memory.FALSE;
    }

    public static void registerCode(Environment env, String protocol, Class<? extends WrapProcessor> clazz) {
        registerCode(env, protocol, clazz, 0);
    }

    public static void registerCode(Environment env, String protocol, Class<? extends WrapProcessor> clazz, int flags) {
        env.setUserValue(ProcessorException.class.getName() + "#" + protocol, ReflectionUtils.getClassName(clazz));
        env.setUserValue(ProcessorException.class.getName() + "#" + protocol + ".flags", flags);
    }

    public static WrapProcessor createByCode(Environment env, String code, int flags) throws Throwable {
        if (code.isEmpty() || !GrammarUtils.isValidName(code)) {
            throw new IllegalArgumentException("format is invalid");
        }

        String className = env.getUserValue(ProcessorException.class.getName() + "#" + code, String.class);

        if (flags == -1) {
            flags = env.getUserValue(ProcessorException.class.getName() + "#" + code + ".flags", Integer.class);
        }

        if (className == null) {
            throw new IllegalArgumentException(
                    "Processor with format '" + code + "' is not registered"
            );
        } else {
            ClassEntity classEntity = env.fetchClass(className);
            IObject iObject = classEntity.newObject(env, env.trace(), true, LongMemory.valueOf(flags));

            if (!classEntity.isInstanceOf(WrapProcessor.class)) {
                throw new IllegalArgumentException("Class " + className + " is not processor class");
            }

            return (WrapProcessor) iObject;
        }
    }

    @Name("php\\format\\ProcessorException")
    public static class ProcessorException extends BaseException {
        public ProcessorException(Environment env) {
            super(env);
        }

        public ProcessorException(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }
    }
}
