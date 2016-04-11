package org.develnext.jphp.ext.httpclient.classes;

import org.develnext.jphp.ext.httpclient.HttpClientExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.net.WrapURLConnection;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;

@Name("HttpMultipartFormBody")
@Namespace(HttpClientExtension.NS)
public class PHttpMultipartFormBody extends PHttpBody {
    public final static String CRLF = "\r\n";

    protected ArrayMemory fields;
    protected ArrayMemory files;

    protected String boundary;
    protected String encoding;

    {
        boundary = "===" + UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis() + "===";
    }

    public PHttpMultipartFormBody(Environment env) {
        super(env);
    }

    public PHttpMultipartFormBody(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        this.fields = new ArrayMemory();
    }

    @Signature
    public void __construct(ArrayMemory data) {
        __construct(data, "UTF-8");
    }

    @Signature
    public void __construct(ArrayMemory data, String encoding) {
        this.fields = (ArrayMemory) data.toImmutable();
    }

    @Signature
    public String getEncoding() {
        return encoding;
    }

    @Signature
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Signature
    public ArrayMemory getFiles() {
        return files;
    }

    @Signature
    public void setFiles(ArrayMemory files) {
        this.files = files;
    }

    @Signature
    public void addFile(String name, Memory file) {
        this.files.add(file.toImmutable());
    }

    @Signature
    public ArrayMemory getFields() {
        return fields;
    }

    @Signature
    public void setFields(ArrayMemory fields) {
        this.fields = fields;
    }

    @Override
    @Signature
    public Memory apply(Environment env, Memory... args) throws IOException {
        WrapURLConnection connection = args[0].toObject(WrapURLConnection.class);
        URLConnection conn = connection.getWrappedObject();

        OutputStream out = connection.getOutputStream();

        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

        ForeachIterator iterator = fields.foreachIterator(false, false);
        while (iterator.next()) {
            StringBuilder sb = new StringBuilder();

            String name = iterator.getStringKey();
            Memory value = iterator.getValue();

            sb.append("--").append(boundary).append(CRLF);
            sb.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(CRLF);
            sb.append("Content-Type: text/plain; charset=").append(encoding).append(CRLF);
            sb.append(CRLF).append(value).append(CRLF);

            out.write(sb.toString().getBytes());
        }

        iterator = files.foreachIterator(false, false);
        while (iterator.next()) {
            StringBuilder sb = new StringBuilder();

            String name = iterator.getStringKey();
            String fileName = name;

            Memory value = iterator.getValue();

            InputStream inputStream = Stream.getInputStream(env, value);

            if (inputStream == null) {
                throw new IllegalStateException("File '" + name + "' is not valid file or stream");
            }

            if (value.instanceOf(Stream.class)) {
                Stream stream = value.toObject(Stream.class);
                fileName = new File(stream.getPath()).getName();
            } else {
                fileName = new File(value.toString()).getName();
            }

            sb.append("--").append(boundary)
                    .append(CRLF)
                    .append("Content-Disposition: form-data; name=\"")
                    .append(URLEncoder.encode(name, encoding)).append("\"; filename=\"").append(URLEncoder.encode(fileName, encoding)).append("\"")
                    .append(CRLF);

            sb.append("Content-Type: ")
                    .append(URLConnection.guessContentTypeFromName(fileName))
                    .append(CRLF);

            sb.append("Content-Transfer-Encoding: binary").append(CRLF).append(CRLF);

            out.write(sb.toString().getBytes());

            byte[] buff = new byte[4096];
            int len;

            while ((len = inputStream.read(buff)) > 0) {
                out.write(buff, 0, len);
            }

            Stream.closeStream(env, inputStream);

            sb.append(CRLF);
        }

        out.write(("--" + boundary + "--").getBytes());
        out.write(CRLF.getBytes());

        return Memory.NULL;
    }
}
