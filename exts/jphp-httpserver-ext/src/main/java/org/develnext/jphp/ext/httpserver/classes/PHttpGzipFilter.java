package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import javax.servlet.ServletException;
import java.io.IOException;

@Name("HttpGzipFilter")
@Namespace(HttpServerExtension.NS)
public class PHttpGzipFilter extends PHttpAbstractHandler {

    private GzipHandler gzipHandler;

    public PHttpGzipFilter(Environment env) {
        super(env);
    }

    public PHttpGzipFilter(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        gzipHandler = new GzipHandler();
    }

    @Signature
    public void includeMethods(String[] methods) {
        gzipHandler.setExcludedMethods(methods);
    }

    @Signature
    public void excludeMethods(String[] methods) {
        gzipHandler.setExcludedMethods(methods);
    }

    @Signature
    public void includeMimeTypes(String[] mimeTypes) {
        gzipHandler.setIncludedMimeTypes(mimeTypes);
    }

    @Signature
    public void excludeMimeTypes(String[] mimeTypes) {
        gzipHandler.setExcludedMimeTypes(mimeTypes);
    }

    @Signature
    public void minGzipSize(int size) {
        gzipHandler.setMinGzipSize(size);
    }

    @Signature
    public void compressLevel(int level) {
        gzipHandler.setCompressionLevel(level);
    }

    @Signature
    public void __invoke(PHttpServerRequest request, PHttpServerResponse response) throws IOException, ServletException {
        Request baseRequest = Request.getBaseRequest(request.getRequest());

        gzipHandler.handle(request.getRequest().getRequestURI(), baseRequest, request.getRequest(), response.getResponse());
    }
}
