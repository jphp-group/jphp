package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.UserException;
import ru.regenix.jphp.runtime.env.message.NoticeMessage;
import ru.regenix.jphp.runtime.env.message.SystemMessage;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.ob.OutputBuffer;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.ParameterEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static ru.regenix.jphp.exceptions.support.ErrorException.Type.*;

public class Environment {
    private Set<String> includePaths;

    private int errorFlags;
    private SystemMessage lastMessage;

    private ErrorHandler previousErrorHandler;
    private ErrorHandler errorHandler;

    private ExceptionHandler previousExceptionHandler;
    private ExceptionHandler exceptionHandler;

    private OutputBuffer defaultBuffer;
    private Stack<OutputBuffer> outputBuffers;

    private final CompileScope scope;
    private Charset defaultCharset = Charset.forName("UTF-8");

    private final Stack<TraceInfo> stackTrace = new Stack<TraceInfo>();

    public Environment(CompileScope scope, OutputStream output) {
        this.scope = scope;
        this.outputBuffers = new Stack<OutputBuffer>();

        this.defaultBuffer = new OutputBuffer(this, null);
        this.outputBuffers.push(defaultBuffer);
        this.defaultBuffer.setOutput(output);

        this.includePaths = new HashSet<String>();
        this.setErrorFlags(E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value));

        this.setErrorHandler(new ErrorHandler() {
            @Override
            public boolean onError(SystemMessage error) {
                Environment.this.echo(error.getDebugMessage());
                Environment.this.echo("\n");
                return false;
            }
        });
    }

    public Environment(OutputStream output){
        this(new CompileScope(), output);
    }

    public Environment(CompileScope scope){
        this(scope, null);
    }

    public Environment(){
        this((OutputStream) null);
    }

    public CompileScope getScope() {
        return scope;
    }

    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    public Set<String> getIncludePaths() {
        return includePaths;
    }

    public void addIncludePath(String value){
        includePaths.add(value);
    }

    public void setIncludePaths(Set<String> includePaths) {
        this.includePaths = includePaths;
    }

    public int getErrorFlags() {
        return errorFlags;
    }

    public void setErrorFlags(int errorFlags) {
        this.errorFlags = errorFlags;
    }

    public SystemMessage getLastMessage() {
        return lastMessage;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.previousExceptionHandler = this.exceptionHandler;
        this.exceptionHandler = exceptionHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.previousErrorHandler = this.errorHandler;
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getPreviousErrorHandler() {
        return previousErrorHandler;
    }

    public ExceptionHandler getPreviousExceptionHandler() {
        return previousExceptionHandler;
    }

    public void triggerError(ErrorException err){
        ErrorException.Type type = err.getType();
        if (ErrorException.Type.check(errorFlags, type)){
            throw err;
        }
    }

    public void triggerMessage(SystemMessage message){
        ErrorException.Type type = message.getType();
        if (isHandleErrors(type)){
            lastMessage = message;
            if (errorHandler != null)
                if (errorHandler.onError(message))
                    return;
        }
    }

    public boolean isHandleErrors(ErrorException.Type type){
        return ErrorException.Type.check(errorFlags, type);
    }

    public void triggerException(UserException exception){
        if (exceptionHandler != null)
            exceptionHandler.onException(exception);

        throw exception;
    }

    public Context createContext(String code){
        return new Context(this, code);
    }

    public Context createContext(File file){
        return new Context(this, file);
    }

    public Context createContext(File file, Charset charset){
        return new Context(this, file, charset);
    }

    public OutputBuffer getDefaultBuffer() {
        return defaultBuffer;
    }

    public OutputBuffer pushOutputBuffer(Memory callback, int chunkSize, boolean erase){
        OutputBuffer buffer = new OutputBuffer(this, peekOutputBuffer(), callback, chunkSize, erase);
        outputBuffers.push(buffer);
        return buffer;
    }

    public OutputBuffer popOutputBuffer(){
        return outputBuffers.pop();
    }

    public OutputBuffer peekOutputBuffer(){
        return outputBuffers.empty() ? null : outputBuffers.peek();
    }

    public void echo(String value){
        OutputBuffer buffer = peekOutputBuffer();
        buffer.write(value);
    }

    public void echo(InputStream input) throws IOException {
        OutputBuffer buffer = peekOutputBuffer();
        buffer.write(input);
    }

    public void flushAll() throws IOException {
        while (peekOutputBuffer() != null){
            popOutputBuffer().flush();
        }
    }

    /***** UTILS *****/
    public Memory getConstant(String name, TraceInfo trace){
        ConstantEntity entity = scope.findUserConstant(name);

        if (entity == null){
            if (isHandleErrors(E_NOTICE))
                triggerMessage(new NoticeMessage(trace, Messages.ERR_NOTICE_USE_UNDEFINED_CONSTANT, name, name));

            return StringMemory.valueOf(name);
        } else if (entity.caseSensitise && !entity.name.equals(name))
            return StringMemory.valueOf(name);

        return entity.getValue();
    }

    public Memory callStaticMethod(TraceInfo trace, String className, String methodName, Memory[] args){
        ClassEntity entity = scope.findUserClass(className);
        if (entity != null){
            MethodEntity method = entity.findMethod(methodName);
            int i = 0;
            Memory[] passed = args;
            if (args.length < method.parameters.length){
                passed = new Memory[method.parameters.length];
                if (args.length > 0)
                    System.arraycopy(args, 0, passed, 0, args.length);
            }

            for(ParameterEntity param : method.parameters){
                if (passed[i] == null){
                    passed[i] = param.getDefaultValue();
                    if (passed[i] == null)
                        triggerError(new CompileException("Argument '" + param.getName() + "' is not passed", trace));

                } else if (!param.isReference())
                    passed[i] = passed[i].toImmutable();

                switch (param.getType()){
                    case ARRAY: {
                        if (!passed[i].isArray())
                            triggerError(new CompileException("Argument " + i + " must be array", trace));
                    }
                }
                i++;
            }
            return method.invoke(null, this, passed);
        }
        return Memory.CONST_DOUBLE_0;
    }
}
