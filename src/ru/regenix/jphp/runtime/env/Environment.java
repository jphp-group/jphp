package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.UserException;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.ob.OutputBuffer;

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
    private ErrorException lastError;

    private ErrorHandler previousErrorHandler;
    private ErrorHandler errorHandler;

    private ExceptionHandler previousExceptionHandler;
    private ExceptionHandler exceptionHandler;

    private OutputBuffer defaultBuffer;
    private Stack<OutputBuffer> outputBuffers;

    private final CompileScope scope;
    private Charset defaultCharset = Charset.forName("UTF-8");

    public Environment(CompileScope scope, OutputStream output) {
        this.scope = scope;
        this.defaultBuffer = new OutputBuffer(this);
        this.defaultBuffer.setOutput(output);

        this.includePaths = new HashSet<String>();
        this.outputBuffers = new Stack<OutputBuffer>();
        this.setErrorFlags(E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value));
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

    public ErrorException getLastError() {
        return lastError;
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
        lastError = err;
        if (errorHandler != null)
            errorHandler.onError(err);

        ErrorException.Type type = err.getType();
        if (ErrorException.Type.check(errorFlags, type)){
            throw err;
        }
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

    public void pushOutputBuffer(Memory callback, int chunkSize, boolean erase){
        outputBuffers.push(new OutputBuffer(this, callback, chunkSize, erase));
    }

    public OutputBuffer popOutputBuffer(){
        return outputBuffers.pop();
    }

    public OutputBuffer peekOutputBuffer(){
        return outputBuffers.empty() ? null : outputBuffers.peek();
    }

    public void echo(String value){
        OutputBuffer buffer = peekOutputBuffer();
        if (buffer == null)
            defaultBuffer.write(value);
        else
            buffer.write(value);
    }

    public void echo(InputStream input) throws IOException {
        OutputBuffer buffer = peekOutputBuffer();
        if (buffer == null)
            defaultBuffer.write(input);
        else
            buffer.write(input);
    }

    public void flushAll() throws IOException {
        while (peekOutputBuffer() != null){
            popOutputBuffer().flush();
        }
    }
}
