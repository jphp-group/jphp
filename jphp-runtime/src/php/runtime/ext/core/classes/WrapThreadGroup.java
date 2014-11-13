package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\ThreadGroup")
public class WrapThreadGroup extends BaseObject {
    protected ThreadGroup group;

    public WrapThreadGroup(Environment env, ThreadGroup group) {
        super(env);
        this.group = group;
    }

    public WrapThreadGroup(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public ThreadGroup getGroup() {
        return group;
    }

    public void setGroup(ThreadGroup group) {
        this.group = group;
    }

    public WrapThreadGroup(Environment env) {
        super(env);
    }

    @Signature({
            @Arg("name"),
            @Arg(value = "parent", typeClass = "php\\lang\\ThreadGroup", optional = @Optional("NULL"))
    })
    public Memory __construct(Environment env, Memory... args){
        if (args[1].isNull())
            setGroup(new ThreadGroup(args[0].toString()));
        else
            setGroup(new ThreadGroup(args[1].toObject(WrapThreadGroup.class).getGroup(), args[0].toString()));

        return Memory.NULL;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*name").assign(group.getName());
        r.refOfIndex("*maxPriority").assign(group.getMaxPriority());

        if (group.getParent() != null)
            r.refOfIndex("*parentName").assign(group.getParent().getName());

        return r.toConstant();
    }

    @Signature
    public Memory getActiveCount(Environment env, Memory... args){
        return LongMemory.valueOf(group.activeCount());
    }

    @Signature
    public Memory getActiveGroupCount(Environment env, Memory... args){
        return LongMemory.valueOf(group.activeGroupCount());
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return StringMemory.valueOf(group.getName());
    }

    @Signature
    public Memory isDaemon(Environment env, Memory... args){
        return TrueMemory.valueOf(group.isDaemon());
    }

    @Signature
    public Memory isDestroyed(Environment env, Memory... args){
        return TrueMemory.valueOf(group.isDestroyed());
    }

    @Signature
    public Memory getMaxPriority(Environment env, Memory... args){
        return LongMemory.valueOf(group.getMaxPriority());
    }

    @Signature(@Arg("value"))
    public Memory setDaemon(Environment env, Memory... args){
        group.setDaemon(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    public Memory setMaxPriority(Environment env, Memory... args){
        group.setMaxPriority(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory destroy(Environment env, Memory... args){
        group.destroy();
        return Memory.NULL;
    }

    @Signature
    public Memory checkAccess(Environment env, Memory... args){
        group.checkAccess();
        return Memory.NULL;
    }

    @Signature
    public Memory interrupt(Environment env, Memory... args){
        group.interrupt();
        return Memory.NULL;
    }

    @Signature
    public Memory getParent(Environment env, Memory... args){
        if (group.getParent() == null)
            return Memory.NULL;
        else
            return new ObjectMemory(new WrapThreadGroup(env, group.getParent()));
    }
}
