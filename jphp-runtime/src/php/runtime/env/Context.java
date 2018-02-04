package php.runtime.env;

import php.runtime.common.Constants;

import java.io.*;
import java.nio.charset.Charset;

public class Context {
    public final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    protected final Charset charset;
    protected final File file;
    protected String content;
    private String moduleName;
    private InputStream inputStream;

    private long lastModified = 0;

    public Context(InputStream input, String fileName, Charset character){
        this.file = null;
        this.charset = character;
        this.moduleName = fileName;
        this.inputStream = input;

        if (fileName != null) {
            this.lastModified = new File(fileName).lastModified();
        }
    }

    public Context(InputStream input, Charset charset) {
        this(input, null, charset);
    }

    public Context(InputStream input) {
        this(input, null, DEFAULT_CHARSET);
    }

    public Context(File file, Charset charset) {
        this.file = file;
        this.moduleName = file.getPath();
        this.charset = charset;
    }

    public Context(File file) {
        this(file, DEFAULT_CHARSET);
    }

    public Context(String content){
        this(content, null);
    }

    public Context(String content, File file){
        this.file = file;
        this.content = content;
        this.charset = DEFAULT_CHARSET;
    }

    public long getLastModified() {
        return lastModified;
    }

    protected void readContent(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
            result.append("\n");
        }
        this.content = result.toString();
    }

    public boolean isLikeFile(){
        return file != null || inputStream != null;
    }

    public String getFileName(){
        return file != null ? file.getPath() : moduleName;
    }

    public String getModuleName() throws IOException {
        if (moduleName != null && file == null)
            return moduleName;

        if (file == null) {
            if (content == null)
                return null;
            return String.valueOf(content.hashCode()) + "~" + content.length();
        } else {
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

    public String getContent() throws IOException {
        if (inputStream != null){
            readContent(new InputStreamReader(inputStream, charset));
        } else if (content == null && file != null){
            readContent(new InputStreamReader(new FileInputStream(file), charset));
        }
        return content;
    }

    public InputStream getInputStream(Charset charset) throws IOException {
        if (inputStream != null)
            return inputStream;
        else if (file != null)
            return new FileInputStream(file);
        else
            return new ByteArrayInputStream(content.getBytes(charset));
    }
}
