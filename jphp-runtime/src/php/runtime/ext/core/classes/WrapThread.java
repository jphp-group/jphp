package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\Thread")
public class WrapThread extends BaseObject implements IComparableObject<WrapThread> {
    public static final int MAX_PRIORITY = Thread.MAX_PRIORITY;
    public static final int MIN_PRIORITY = Thread.MIN_PRIORITY;
    public static final int NORM_PRIORITY = Thread.NORM_PRIORITY;

    protected Environment customEnv;
    protected Thread thread;
    protected Invoker invoker;

    public WrapThread(Environment env) {
        super(env);
    }

    public WrapThread(Environment env, Thread thread) {
        super(env);
        setThread(thread, env);
    }

    public WrapThread(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Environment getCustomEnv() {
        return customEnv;
    }

    public void setCustomEnv(Environment customEnv) {
        this.customEnv = customEnv;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread, Environment env) {
        this.thread = thread;

        if (thread != null) {
            Environment.addThreadSupport(thread, env);
        }
    }

    @Signature({
            @Arg(value = "runnable", type = HintType.CALLABLE),
            @Arg(value = "env", typeClass = "php\\lang\\Environment", optional = @Optional("NULL")),
            @Arg(value = "group", typeClass = "php\\lang\\ThreadGroup", optional = @Optional("NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        if (!args[1].isNull())
            setCustomEnv(args[1].toObject(WrapEnvironment.class).getWrapEnvironment());
        else
            setCustomEnv(env);

        final Invoker invoker = Invoker.valueOf(getCustomEnv(), null, args[0]);

        ThreadGroup group = null;

        if (!args[2].isNull()) {
            group = args[2].toObject(WrapThreadGroup.class).getGroup();
        }

        this.invoker = invoker;

        if (args[2].isNull()) {
            setThread(new Thread(group, new Runnable() {
                @Override
                public void run() {
                    invoker.callNoThrow();
                }
            }), env);
        } else {
            setThread(new Thread(group, new Runnable() {
                @Override
                public void run() {
                    invoker.callNoThrow();
                }
            }, args[2].toString()), env);
        }

        return Memory.NULL;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*id").assign(thread.getId());
        r.refOfIndex("*name").assign(thread.getName());
        if (thread.getThreadGroup() != null)
            r.refOfIndex("*group").assign(thread.getThreadGroup().getName());
        else
            r.refOfIndex("*group");

        r.refOfIndex("*priority").assign(thread.getPriority());
        return r.toConstant();
    }

    @Signature
    public Memory getId(Environment env, Memory... args) {
        return LongMemory.valueOf(thread.getId());
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return new StringMemory(thread.getName());
    }

    @Signature
    public Memory getPriority(Environment env, Memory... args){
        return LongMemory.valueOf(thread.getPriority());
    }

    @Signature(@Arg("value"))
    public Memory setPriority(Environment env, Memory... args){
        thread.setPriority(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory getState(Environment env, Memory... args){
        return new StringMemory(thread.getState().name());
    }

    @Signature
    public Memory setDaemon(Environment env, Memory... args){
        thread.setDaemon(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    public Memory isDaemon(Environment env, Memory... args){
        return thread.isDaemon() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAlive(Environment env, Memory... args){
        return thread.isAlive() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isInterrupted(Environment env, Memory... args){
        return thread.isInterrupted() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("name"))
    public Memory setName(Environment env, Memory... args){
        thread.setName(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    public Memory interrupt(Environment env, Memory... args){
        thread.interrupt();
        return Memory.NULL;
    }

    @Signature
    public Memory start(Environment env, Memory... args){
        invoker.setTrace(env.trace());
        thread.start();
        return Memory.NULL;
    }

    @Signature
    public Memory run(Environment env, Memory... args){
        invoker.setTrace(env.trace());
        thread.run();
        return Memory.NULL;
    }

    @Signature
    public Memory getGroup(Environment env, Memory... args){
        if (thread.getThreadGroup() == null)
            return Memory.NULL;
        return new ObjectMemory(new WrapThreadGroup(env, thread.getThreadGroup()));
    }

    @Signature({
            @Arg(value = "millis", optional = @Optional(value = "0", type = HintType.INT)),
            @Arg(value = "nanos", optional = @Optional(value = "0", type = HintType.INT))
    })
    public Memory join(Environment env, Memory... args) throws InterruptedException {
        thread.join(args[0].toLong(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature
    public static Memory doYield(Environment env, Memory... args){
        Thread.yield();
        return Memory.NULL;
    }

    @Signature
    public static Memory getActiveCount(Environment env, Memory... args){
        return LongMemory.valueOf(Thread.activeCount());
    }

    @Signature
    public static Memory current(Environment env, Memory... args){
        return new ObjectMemory(new WrapThread(env, Thread.currentThread()));
    }

    @Signature({@Arg("millis"), @Arg(value = "nanos", optional = @Optional(value = "0", type = HintType.INT))})
    public static Memory sleep(Environment env, Memory... args) throws InterruptedException {
        Thread.sleep(args[0].toLong(), args[1].toInteger());
        return Memory.NULL;
    }

    @Override
    public boolean __equal(WrapThread iObject) {
        return thread == iObject.thread;
    }

    @Override
    public boolean __identical(WrapThread iObject) {
        return thread == iObject.thread;
    }

    @Override
    public boolean __greater(WrapThread iObject) {
        return false;
    }

    @Override
    public boolean __greaterEq(WrapThread iObject) {
        return false;
    }

    @Override
    public boolean __smaller(WrapThread iObject) {
        return false;
    }

    @Override
    public boolean __smallerEq(WrapThread iObject) {
        return false;
    }
}
