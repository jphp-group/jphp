package php.runtime.ext.core.reflection;

import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.support.ReflectionUtils;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionExtension")
@Signature(
        @Arg(value = "name", type = HintType.STRING)
)
public class ReflectionExtension extends Reflection {
    private Extension extension;

    public ReflectionExtension(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public ReflectionExtension(Environment env) {
        super(env);
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
        getProperties().put("name", new StringMemory(extension.getName()));
    }

    @Signature(@Arg("name"))
    public Memory __construct(Environment env, Memory... args){
        String name = args[0].toString();
        extension = env.scope.getExtension(name);
        if (extension == null){
            exception(env, "Extension %s does not exist", name);
        }
        setExtension(extension);
        return Memory.NULL;
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return new StringMemory(extension.getName());
    }

    @Signature
    public Memory getVersion(Environment env, Memory... args){
        String ver = extension.getVersion();
        if ("~".equals(ver))
            ver = Information.CORE_VERSION;

        return new StringMemory(ver);
    }

    @Signature
    public Memory getClassNames(Environment env, Memory... args){
        ArrayMemory result = new ArrayMemory();

        for(Class<?> e : extension.getClasses()){
            result.add(ReflectionUtils.getClassName(e));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getConstants(Environment env, Memory... args){
        ArrayMemory result = new ArrayMemory();
        for(CompileConstant e : extension.getConstants().values()){
            result.put(e.name, e.value);
        }
        return result.toConstant();
    }

    @Signature
    public Memory getDependencies(Environment env, Memory... args){
        ArrayMemory result = new ArrayMemory();

        for(String name : extension.getRequiredExtensions()){
            result.put(name, new StringMemory("Required"));
        }

        for(String name : extension.getOptionalExtensions()){
            result.put(name, new StringMemory("Optional"));
        }

        for(String name : extension.getConflictExtensions()){
            result.put(name, new StringMemory("Conflicts"));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getINIEntries(Environment env, Memory... args){
        return ArrayMemory.ofStringMap(extension.getINIEntries());
    }

    @Signature
    public Memory isPersistent(Environment env, Memory... args){
        return Memory.TRUE;
    }

    @Signature
    public Memory isTemporary(Environment env, Memory... args){
        return Memory.FALSE;
    }

    @Signature
    public Memory info(Environment env, Memory... args){
        exception(env, "Method '%s' not supported in JPHP", "info");
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "reflector", type = HintType.OBJECT),
            @Arg(value = "return", type = HintType.BOOLEAN, optional = @Optional(value = "", type = HintType.BOOLEAN))
    })
    public static Memory export(Environment env, Memory... args){
        ReflectionExtension e = new ReflectionExtension(env, env.fetchClass("ReflectionExtension"));
        if (args[1].toBoolean())
            return e.__toString(env);
        else
            env.echo(e.__toString(env));
        return Memory.NULL;
    }
}
