package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

@Reflection.Name("HttpDownloadFileHandler")
@Reflection.Namespace(HttpServerExtension.NS)
public class PHttpDownloadFileHandler extends PHttpAbstractHandler {
    private File file;
    private String fileName;
    private String contentType;

    public PHttpDownloadFileHandler(Environment env) {
        super(env);
    }

    public PHttpDownloadFileHandler(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(File file) {
        __construct(file, Memory.NULL, Memory.NULL);
    }


    @Signature
    public void __construct(File file, Memory fileName) {
        __construct(file, fileName, Memory.NULL);
    }

    @Signature
    public void __construct(File file, Memory fileName, Memory contentType) {
        reset(file, fileName, contentType);
    }

    @Signature
    public void reset(File file) {
        reset(file, Memory.NULL, Memory.NULL);
    }

    @Signature
    public void reset(File file, Memory fileName) {
        reset(file, fileName, Memory.NULL);
    }

    @Signature
    public void reset(File file, Memory fileName, Memory contentType) {
        this.file = file;
        this.fileName = fileName.isNull() ? file.getName() : fileName.toString();
        this.contentType = contentType.isNotNull() ? "application/octet-stream" : contentType.toString();
    }

    @Signature
    public File file() {
        return file;
    }

    @Signature
    public String fileName() {
        return fileName;
    }

    @Signature
    public String contentType() {
        return contentType;
    }

    @Signature
    public boolean __invoke(Environment env, PHttpServerRequest request, PHttpServerResponse response) throws Throwable {
        HttpServletResponse res = response.getResponse();

        if (!file.isFile()) {
            response.write(StringMemory.valueOf("File ("+fileName+") not found."));
            res.setStatus(404);
            return false;
        }

        res.setStatus(200);
        res.setContentType(contentType);
        res.setContentLengthLong(file.length());

        if (fileName.isEmpty()) {
            res.addHeader("Content-Disposition", "attachment");
        } else {
            res.addHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, env.getDefaultCharset().name()) + "\"");
        }

        ServletOutputStream outputStream = res.getOutputStream();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[128 * 1024];

        while(true) {
            int bytesRead = inputStream.read(buffer);

            if (bytesRead < 0) {
                break;
            }

            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
        request.end();

        return true;
    }
}
