package php.runtime.ext.swing.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.invoke.Invoker;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "Timer")
public class WrapTimer extends RootObject {
    protected Timer timer;
    protected Invoker callback;

    public WrapTimer(Environment env) {
        super(env);
    }

    public WrapTimer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({@Arg("delay"), @Arg(value = "callback", type = HintType.CALLABLE)})
    public Memory __construct(final Environment env, Memory... args){
        final WrapTimer self = this;
        callback = Invoker.valueOf(env, null, args[1]);

        timer = new Timer(args[0].toInteger(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (callback != null){
                    Memory[] args = new Memory[]{ new ObjectMemory(self) };
                    callback.pushCall(TraceInfo.UNKNOWN, args);
                    try {
                        callback.callNoThrow(args);
                    } finally {
                        callback.popCall();
                    }
                }
            }
        });
        return Memory.NULL;
    }

    @Signature
    public Memory start(Environment env, Memory... args){
        timer.start();
        return Memory.NULL;
    }

    @Signature
    public Memory restart(Environment env, Memory... args){
        timer.restart();
        return Memory.NULL;
    }

    @Signature
    public Memory stop(Environment env, Memory... args){
        timer.stop();
        return Memory.NULL;
    }

    @Signature
    protected Memory __getActionCommand(Environment env, Memory... args){
        return StringMemory.valueOf(timer.getActionCommand());
    }

    @Signature(@Arg("command"))
    protected Memory __setActionCommand(Environment env, Memory... args){
        timer.setActionCommand(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getDelay(Environment env, Memory... args){
        return LongMemory.valueOf(timer.getDelay());
    }

    @Signature(@Arg("delay"))
    protected Memory __setDelay(Environment env, Memory... args){
        timer.setDelay(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getInitDelay(Environment env, Memory... args){
        return LongMemory.valueOf(timer.getInitialDelay());
    }

    @Signature(@Arg("delay"))
    protected Memory __setInitDelay(Environment env, Memory... args){
        timer.setInitialDelay(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRepeat(Environment env, Memory... args){
        return TrueMemory.valueOf(timer.isRepeats());
    }

    @Signature(@Arg("value"))
    protected Memory __setRepeat(Environment env, Memory... args){
        timer.setRepeats(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    public Memory isRunning(Environment env, Memory... args){
        return TrueMemory.valueOf(timer.isRunning());
    }
}
