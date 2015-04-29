package php.runtime.env;

import php.runtime.Memory;
import php.runtime.lang.IObject;

public class CallStack {
    // call stack
    protected final static int CALL_STACK_INIT_SIZE = 255;

    protected int callStackTop = 0;
    protected int maxCallStackTop = -1;
    protected CallStackItem[] callStack = new CallStackItem[CALL_STACK_INIT_SIZE];

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

    public void push(TraceInfo trace, IObject self, Memory[] args, String function, String clazz, String staticClazz) {
        if (callStackTop >= callStack.length){
            CallStackItem[] newCallStack = new CallStackItem[callStack.length * 2];
            System.arraycopy(callStack, 0, newCallStack, 0, callStack.length);
            callStack = newCallStack;
        }

        if (callStackTop < maxCallStackTop)
            callStack[callStackTop++].setParameters(trace, self, args, function, clazz, staticClazz);
        else
            callStack[callStackTop++] = new CallStackItem(trace, self, args, function, clazz, staticClazz);

        maxCallStackTop = callStackTop;
    }

    public void push(IObject self, String method, Memory... args) {
        push(null, self, args, method, self.getReflection().getName(), null);
    }

    public void push(TraceInfo trace, IObject self, String method, Memory... args) {
        push(trace, self, args, method, self.getReflection().getName(), null);
    }

    public void pop() {
        try {
            callStack[--callStackTop].clear(); // clear for GC
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

    public TraceInfo trace(int systemOffsetStackTrace){
        return new TraceInfo(Thread.currentThread().getStackTrace()[systemOffsetStackTrace]);
    }
}
