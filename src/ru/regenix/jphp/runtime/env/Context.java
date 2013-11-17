package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.common.Constants;
import ru.regenix.jphp.exceptions.CoreException;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.UserException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class Context {

    protected final Charset charset;
    protected final Environment environment;
    protected final File file;
    protected String content;

    public Context(Environment environment, File file, Charset charset) {
        this.file = file;
        this.environment = environment;
        this.charset = charset;
    }

    public Context(Environment environment, File file) {
        this(environment, file, environment.getDefaultCharset());
    }

    public Context(Environment environment, String content){
        this.file = null;
        this.environment = environment;
        this.content = content;
        this.charset = environment.getDefaultCharset();
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

    public String getModuleName() throws IOException {
        if (file == null)
            return String.valueOf(content.hashCode()) + "~" + content.length();
        else {
            String name = file.getCanonicalPath();
            if (Constants.PATH_NAME_CASE_INSENSITIVE)
                name = name.toLowerCase();

            return name;
        }
    }

    public String getModuleNameNoThrow(){
        try {
           return getModuleName();
        } catch (IOException e){
           return file.getAbsolutePath();
        }
    }

    public String getContent() {
        if (content == null && file != null){
            try {
                readContent(new InputStreamReader(new FileInputStream(file), charset));
            } catch (FileNotFoundException e) {
                environment.triggerError(new CoreException(e.getMessage(), this));
            }
        }
        return content;
    }

    public void triggerError(ErrorException error){
        environment.triggerError(error);
    }

    public void triggerException(UserException exception){
        environment.triggerException(exception);
    }
}
