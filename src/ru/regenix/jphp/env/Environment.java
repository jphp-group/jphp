package ru.regenix.jphp.env;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.UserException;

import static ru.regenix.jphp.exceptions.support.ErrorException.Type.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Environment {
    private Set<String> includePaths;

    private int errorFlags;
    private ErrorException lastError;

    private ErrorHandler previousErrorHandler;
    private ErrorHandler errorHandler;

    private ExceptionHandler previousExceptionHandler;
    private ExceptionHandler exceptionHandler;

    public Environment() {
        this.includePaths = new HashSet<String>();
        this.setErrorFlags(E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value));
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
}
