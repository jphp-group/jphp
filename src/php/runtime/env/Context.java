package php.runtime.env;

import ru.regenix.jphp.common.Constants;

import java.io.*;
import java.nio.charset.Charset;

public class Context {
    protected final Charset charset;
    protected final File file;
    protected String content;

    public Context(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    public Context(File file) {
        this(file, Charset.defaultCharset());
    }

    public Context(String content){
        this(content, null);
    }

    public Context(String content, File file){
        this.file = file;
        this.content = content;
        this.charset = Charset.defaultCharset();
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

    public String getContent() throws IOException {
        if (content == null && file != null){
            readContent(new InputStreamReader(new FileInputStream(file), charset));
        }
        return content;
    }
}
