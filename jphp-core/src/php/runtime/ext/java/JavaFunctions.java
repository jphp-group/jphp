package php.runtime.ext.java;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ObjectMemory;

public class JavaFunctions extends FunctionsContainer {

    public static Memory java_create_runnable(Environment env, final TraceInfo trace, Memory value){
        final Invoker invoker = expectingCallback(env, trace, 1, value);
        if (invoker == null)
            return Memory.NULL;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                invoker.pushCall(trace, null);
                try {
                    invoker.call();
                } catch (RuntimeException e){
                    throw e;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                } finally {
                    invoker.popCall();
                }
            }
        };

        return new ObjectMemory(JavaObject.of(env, runnable));
    }
}
