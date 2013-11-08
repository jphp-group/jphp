package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.exceptions.CoreException;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.UserException;

import java.io.*;
import java.nio.charset.Charset;

public class Context {

    protected final Environment environment;
    protected final File file;
    protected String content;

    public Context(Environment environment, File file, Charset charset) {
        this.file = file;
        this.environment = environment;
        try {
            readContent(new InputStreamReader(new FileInputStream(file), charset));
        } catch (FileNotFoundException e) {
            environment.triggerError(new CoreException(e.getMessage(), this));
        }
    }

    public Context(Environment environment, File file) {
        this(environment, file, environment.getDefaultCharset());
    }

    public Context(Environment environment, String content){
        this.file = null;
        this.environment = environment;
        this.content = content;
    }

    protected void readContent(Reader reader){
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
                result.append("\n");
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

    public boolean isFile(){
        return file != null;
    }

    public String getContent() {
        return content;
    }

    public void triggerError(ErrorException error){
        environment.triggerError(error);
    }

    public void triggerException(UserException exception){
        environment.triggerException(exception);
    }
}
