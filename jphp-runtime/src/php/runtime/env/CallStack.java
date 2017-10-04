package php.runtime.env;

import php.runtime.Memory;
import php.runtime.lang.IObject;
import php.runtime.reflection.ModuleEntity;

public class CallStack {
    // call stack
    protected final static int CALL_STACK_INIT_SIZE = 255;

    protected int callStackTop = 0;
    protected int maxCallStackTop = -1;
    protected CallStackItem[] callStack = new CallStackItem[CALL_STACK_INIT_SIZE];

    protected final Environment env;

    public CallStack(Environment env) {
        this.env = env;
    }

    public int getTop(){
        return callStackTop;
    }

    public void push(CallStackItem stackItem) {
        if (callStackTop >= callStack.length){
            CallStackItem[] newCallStack = new CallStackItem[callStack.length * 2];
            System.arraycopy(callStack, 0, newCallStack, 0, callStack.length);
            callStack = newCallStack;
        }

        callStack[callStackTop++] = stackItem;
        maxCallStackTop = callStackTop;
    }

    public CallStackItem push(TraceInfo trace, IObject self, Memory[] args, String function, String clazz, String staticClazz) {
        if (callStackTop >= callStack.length){
            CallStackItem[] newCallStack = new CallStackItem[callStack.length * 2];
            System.arraycopy(callStack, 0, newCallStack, 0, callStack.length);
            callStack = newCallStack;
        }

        CallStackItem result;

        if (callStackTop < maxCallStackTop) {
            result = callStack[callStackTop++];
            result.setParameters(trace, self, args, function, clazz, staticClazz);
        } else {
            callStack[callStackTop++] = result = new CallStackItem(trace, self, args, function, clazz, staticClazz);
        }

        maxCallStackTop = callStackTop;

        return result;
    }

    public CallStackItem push(IObject self, String method, Memory... args) {
        return push(null, self, args, method, self.getReflection().getName(), null);
    }

    public CallStackItem push(TraceInfo trace, IObject self, String method, Memory... args) {
        return push(trace, self, args, method, self.getReflection().getName(), null);
    }

    public CallStackItem pop() {
        try {
            CallStackItem result = callStack[--callStackTop];
            result.clear(); // clear for GC
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalThreadStateException();
        }
    }

    public CallStackItem peekCall(int depth) {
        if (callStackTop - depth > 0) {
            return callStack[callStackTop - depth - 1];
        } else {
            return null;
        }
    }

    public CallStack getSnapshotAsCallStack() {
        CallStack stack = new CallStack(env);

        stack.callStack = new CallStackItem[callStack.length];

        System.arraycopy(getSnapshot(), 0, stack.callStack, 0, callStackTop);

        stack.callStackTop = callStackTop;
        stack.maxCallStackTop = maxCallStackTop;

        return stack;
    }

    public CallStackItem[] getSnapshot(){
        if (callStackTop < 0) {
            return new CallStackItem[] { };
        }

        CallStackItem[] result = new CallStackItem[callStackTop];
        int i = 0;
        for(CallStackItem el : callStack){
            if (i == callStackTop)
                break;

            result[i] = new CallStackItem(el);
            i++;
        }

        return result;
    }

    public TraceInfo trace(){
        if (callStackTop <= 0)
            return TraceInfo.UNKNOWN;

        return peekCall(0).trace;
    }

    public ModuleEntity module() {
        if (callStackTop <= 0) {
            return null;
        }

        return env.getModuleManager().findModule(peekCall(0).trace);
    }

    public ModuleEntity module(int depth) {
        CallStackItem stackItem = peekCall(depth);

        if (stackItem != null) {
            return env.getModuleManager().findModule(stackItem.trace);
        }

        return null;
    }

    public TraceInfo trace(int systemOffsetStackTrace){
        return new TraceInfo(Thread.currentThread().getStackTrace()[systemOffsetStackTrace]);
    }
}
