package php.runtime.env;

import php.runtime.Memory;
import php.runtime.exceptions.CriticalException;
import php.runtime.output.OutputBuffer;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ModuleEntity;

import java.io.OutputStream;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;

public class ConcurrentEnvironment extends Environment {
    private ThreadLocal<Stack<OutputBuffer>> outputBuffers;

    private ThreadLocal<Stack<Integer>> silentFlags = new ThreadLocal<Stack<Integer>>(){
        @Override
        protected Stack<Integer> initialValue() {
            return new Stack<Integer>();
        }
    };

    private ThreadLocal<CallStack> callStack = new ThreadLocal<CallStack>() {
        @Override
        protected CallStack initialValue() {
            return new CallStack(ConcurrentEnvironment.this);
        }
    };

    public ConcurrentEnvironment(Environment parent) {
        super(parent);
    }

    public ConcurrentEnvironment(CompileScope scope, OutputStream output) {
        super(scope, output);
    }

    public ConcurrentEnvironment(OutputStream output) {
        super(output);
    }

    public ConcurrentEnvironment(CompileScope scope) {
        super(scope);
    }

    @Override
    public Stack<OutputBuffer> getOutputBuffers() {
        if (outputBuffers == null) {
            synchronized (this) {
                outputBuffers = new ThreadLocal<Stack<OutputBuffer>>(){
                    @Override
                    protected Stack<OutputBuffer> initialValue() {
                        Stack stack = new Stack<OutputBuffer>();
                        stack.push(getDefaultBuffer());
                        return stack;
                    }
                };
            }
        }
        return outputBuffers.get();
    }

    @Override
    public void __replaceCallStack(CallStack stack) {
        callStack.set(stack);
    }

    @Override
    public CallStack getCallStack() {
        return callStack.get();
    }

    @Override
    public void addIncludePath(String value) {
        synchronized (this){
            super.addIncludePath(value);
        }
    }

    @Override
    public ClassEntity autoloadCall(String name, String lowerName) {
        synchronized (this) {
            return super.autoloadCall(name, lowerName);
        }
    }

    @Override
    public void setIncludePaths(Set<String> includePaths) {
        synchronized (this) {
            super.setIncludePaths(includePaths);
        }
    }

    @Override
    public boolean defineConstant(String name, Memory value, boolean caseSensitise) {
        synchronized (this) {
            return super.defineConstant(name, value, caseSensitise);
        }
    }

    @Override
    public Memory setConfigValue(String name, Memory value) {
        synchronized (this) {
            return super.setConfigValue(name, value);
        }
    }

    @Override
    public void setLocale(Locale locale) {
        synchronized (this) {
            super.setLocale(locale);
        }
    }

    @Override
    public void setUserValue(String name, Object value) {
        synchronized (this) {
            super.setUserValue(name, value);
        }
    }

    @Override
    public void setErrorFlags(int errorFlags) {
        synchronized (this) {
            super.setErrorFlags(errorFlags);
        }
    }

    public void __pushSilent(){
        silentFlags.get().push(getErrorFlags());
        setErrorFlags(0);
    }

    public void __popSilent(){
        Integer flags = silentFlags.get().pop();
        setErrorFlags(flags);
    }

    public void __clearSilent(){
        Stack<Integer> silents = silentFlags.get();
        Integer flags = null;
        while (!silents.empty())
            flags = silents.pop();

        if (flags != null) {
            setErrorFlags(flags);
        }
    }

    @Override
    public void __defineFunction(TraceInfo trace, String moduleIndex, int index) {
        synchronized (this) {
            super.__defineFunction(trace, moduleIndex, index);
        }
    }

    @Override
    public void registerModule(ModuleEntity module) {
        synchronized (this) {
            super.registerModule(module, true);
        }
    }

    @Override
    public ModuleEntity importCompiledModule(Context context, boolean debugInformation) throws Throwable {
        synchronized (this) {
            return super.importCompiledModule(context, debugInformation);
        }
    }

    @Override
    public ModuleEntity importModule(Context context) throws Throwable {
        synchronized (this) {
            return super.importModule(context);
        }
    }

    @Override
    public void registerAutoloader(SplClassLoader classLoader, boolean prepend) {
        synchronized (classLoaders){
            super.registerAutoloader(classLoader, prepend);
        }
    }

    @Override
    public boolean unRegisterAutoloader(SplClassLoader classLoader) {
        synchronized (classLoaders){
            return super.unRegisterAutoloader(classLoader);
        }
    }

    @Override
    public Memory getOrCreateGlobal(String name) {
        synchronized (this.globals) {
            return super.getOrCreateGlobal(name);
        }
    }

    @Override
    public Memory getOrCreateStatic(String name, Memory initValue) {
        synchronized (this.statics) {
            return super.getOrCreateStatic(name, initValue);
        }
    }

    @Override
    public void echo(byte[] bytes, int length) {
        synchronized (this) {
            super.echo(bytes, length);
        }
    }

    @Override
    public void echo(Memory value) {
        synchronized (this) {
            super.echo(value);
        }
    }

    @Override
    public void echo(String value) {
        synchronized (this) {
            super.echo(value);
        }
    }

    @Override
    public void flushAll() throws Throwable {
        synchronized (this) {
            super.flushAll();
        }
    }
}
