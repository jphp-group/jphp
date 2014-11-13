package php.runtime.env;

import php.runtime.Memory;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.IObject;
import php.runtime.output.OutputBuffer;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ModuleEntity;

import java.io.OutputStream;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;

import static php.runtime.exceptions.support.ErrorType.*;

public class ConcurrentEnvironment extends Environment {

    private ThreadLocal<Stack<OutputBuffer>> outputBuffers;

    private ThreadLocal<Stack<Integer>> silentFlags = new ThreadLocal<Stack<Integer>>(){
        @Override
        protected Stack<Integer> initialValue() {
            return new Stack<Integer>();
        }
    };

    private ThreadLocal<Integer> errorFlags = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value);
        }
    };

    //private int callStackTop = 0;
    private ThreadLocal<Integer> callStackTop = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    //private int maxCallStackTop = -1;
    private ThreadLocal<Integer> maxCallStackTop = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    private ThreadLocal<CallStackItem[]> callStack = new ThreadLocal<CallStackItem[]>(){
        @Override
        protected CallStackItem[] initialValue() {
            return new CallStackItem[CALL_STACK_INIT_SIZE];
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

    public void pushCall(TraceInfo trace, IObject self, Memory[] args, String function, String clazz, String staticClazz){
        CallStackItem[] cs = callStack.get();
        int top = callStackTop.get();
        if (top >= cs.length){
            CallStackItem[] newCallStack = new CallStackItem[cs.length * 2];
            System.arraycopy(cs, 0, newCallStack, 0, cs.length);
            callStack.set(cs = newCallStack);
        }

        if (top < maxCallStackTop.get())
            cs[top++].setParameters(trace, self, args, function, clazz, staticClazz);
        else
            cs[top++] = new CallStackItem(trace, self, args, function, clazz, staticClazz);

        maxCallStackTop.set(top);
        callStackTop.set(top);
    }


    public void popCall(){
        int top = callStackTop.get();
        callStack.get()[--top].clear(); // clear for GC
        callStackTop.set(top);
    }

    public CallStackItem peekCall(int depth){
        if (callStackTop.get() - depth > 0) {
            return callStack.get()[callStackTop.get() - depth - 1];
        } else {
            return null;
        }
    }

    public TraceInfo trace(){
        if (callStackTop.get() == 0)
            return TraceInfo.UNKNOWN;
        return peekCall(0).trace;
    }

    public int getCallStackTop(){
        return callStackTop.get();
    }

    public CallStackItem[] getCallStackSnapshot(){
        int top = callStackTop.get();
        CallStackItem[] result = new CallStackItem[top];
        int i = 0;
        for(CallStackItem el : callStack.get()){
            if (i == top)
                break;

            result[i] = new CallStackItem(el);
            i++;
        }

        return result;
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
        this.errorFlags.set(errorFlags);
    }

    @Override
    public int getErrorFlags() {
        return this.errorFlags.get();
    }

    @Override
    public boolean isHandleErrors(ErrorType type) {
        return ErrorType.check(errorFlags.get(), type);
    }

    public void __pushSilent(){
        silentFlags.get().push(errorFlags.get());
        setErrorFlags(0);
    }

    public void __popSilent(){
        Integer flags = silentFlags.get().pop();
        setErrorFlags(flags);
    }

    public void __clearSilent(){
        Stack<Integer> silents = silentFlags.get();
        Integer flags = 0;
        while (!silents.empty())
            flags = silents.pop();

        setErrorFlags(flags);
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
            super.registerModule(module);
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
}
