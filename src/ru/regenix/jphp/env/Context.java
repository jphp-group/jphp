package ru.regenix.jphp.env;

import ru.regenix.jphp.exceptions.CoreException;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.UserException;

import java.io.*;

public class Context {
    public enum Mode { PHP, J_PHP }

    protected final Environment environment;
    protected final File file;
    protected final Mode mode;
    protected String content;

    public Context(Environment environment, File file, Mode mode) {
        this.file = file;
        this.environment = environment;
        this.mode = mode;
        try {
            readContent(new FileReader(file));
        } catch (FileNotFoundException e) {
            environment.triggerError(new CoreException(e.getMessage(), this));
        }
    }

    public Context(Environment environment, File file) {
        this(environment, file, Mode.PHP);
    }

    public Context(Environment environment, String content, Mode mode){
        this.file = null;
        this.environment = environment;
        this.mode = mode;
        this.content = content;
    }

    public Context(Environment environment, String content){
        this(environment, content, Mode.PHP);
    }

    protected void readContent(Reader reader){
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            environment.triggerError(new CoreException("Cannot read content", this));
        }
        this.content = result.toString();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public File getFile() {
        return file;
    }

    public String getContent() {
        return content;
    }

    public Mode getMode() {
        return mode;
    }

    public void triggerError(ErrorException error){
        environment.triggerError(error);
    }

    public void triggerException(UserException exception){
        environment.triggerException(exception);
    }
}
