package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.message.SystemMessage;
import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.lang.BaseException;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class ErrorHandler {
    public final Invoker invoker;
    public final Memory invokerMemory;
    public final int errorHandlerFlags;

    public ErrorHandler(Invoker invoker, Memory invokerMemory, int errorHandlerFlags) {
        this.invoker = invoker;
        this.invokerMemory = invokerMemory;
        this.errorHandlerFlags = errorHandlerFlags;
    }

    public boolean onError(Environment env, SystemMessage message) {
        if (ErrorType.check(errorHandlerFlags, message.getType())){
            TraceInfo trace = message.getTrace().trace;
            int argCount = invoker.getArgumentCount();
            if (argCount < 4) argCount = 4;
            else if (argCount > 5) argCount = 5;

            Memory[] args = new Memory[argCount];
            args[0] = LongMemory.valueOf(message.getType().value);
            args[1] = new StringMemory(message.getMessage());
            args[2] = new StringMemory(trace.getFileName());
            args[3] = LongMemory.valueOf(trace.getStartLine() + 1);
            if (argCount > 4)
                args[4] = new ArrayMemory(false, message.getTrace().args);

            try {
                return (invoker.call(args).toBoolean());
            } catch (ErrorException e){
                throw e;
            } catch (BaseException e){
                throw e;
            } catch (DieException e){
                throw e;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
        return false;
    }
}
