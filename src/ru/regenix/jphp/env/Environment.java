package ru.regenix.jphp.env;

import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.compiler.jvm.runtime.ob.OutBuffer;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.UserException;

import java.io.*;
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

    private Stack<OutBuffer> outBuffers;
    private final OutputStream output;
    private StringBuilder header;
    private StringBuilder body;

    private Charset defaultCharset = Charset.forName("UTF-8");

    public Environment(OutputStream output) {
        this.output = output;
        this.header = new StringBuilder();
        this.body = new StringBuilder();

        this.includePaths = new HashSet<String>();
        this.outBuffers = new Stack<OutBuffer>();
        this.setErrorFlags(E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value));
    }

    public Environment(){
        this(new ByteArrayOutputStream());
    }

    public void setDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
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

    public Context createContext(String code, Context.Mode mode){
        return new Context(this, code, mode);
    }

    public Context createContext(String code){
        return new Context(this, code);
    }

    public Context createContext(File file, Context.Mode mode){
        return new Context(this, file, mode);
    }

    public Context createContext(File file){
        return new Context(this, file);
    }

    public void pushOutBuffer(Memory callback, int chunkSize, boolean erase){
        outBuffers.push(new OutBuffer(this, callback, chunkSize, erase));
    }

    public OutBuffer popOutBuffer(){
        return outBuffers.pop();
    }

    public OutBuffer peekOutBuffer(){
        return outBuffers.empty() ? null : outBuffers.peek();
    }

    public void echo(String value){
        OutBuffer buffer = peekOutBuffer();
        if (buffer == null)
            body.append(value);
        else
            buffer.write(value);
    }

    public boolean header(String value){
        if (header == null)
            return false;

        header.append(value);
        return true;
    }

    public void flush(){
        OutBuffer buffer = peekOutBuffer();
        if (buffer != null){
            echo(buffer.getContent());
            buffer.flush();
            popOutBuffer();
        } else {
            try {
                output.write(header.toString().getBytes(defaultCharset));
                output.write(body.toString().getBytes(defaultCharset));

                body = new StringBuilder();
                header = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public OutputStream getOutput() {
        return output;
    }

    public String getOutputAsString(Charset charset) {
        if (output instanceof ByteArrayOutputStream){
            try {
                return ((ByteArrayOutputStream) output).toString(charset.name());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else
            throw new RuntimeException("Cannot fetch output as string");
    }

    public String getOutputAsString() {
        return getOutputAsString(defaultCharset);
    }
}
