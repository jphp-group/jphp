package org.develnext.jphp.ext.httpserver.classes;


import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;

@Name("HttpResourceHandler")
@Namespace(HttpServerExtension.NS)
public class PHttpResourceHandler extends PHttpAbstractHandler {
    private ResourceHandler resourceHandler;
    private String file;
    private String cacheControl;

    public PHttpResourceHandler(Environment env) {
        super(env);
    }

    public PHttpResourceHandler(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String directory) throws Exception {
        resourceHandler = new ResourceHandler();
        file(directory);
    }

    @Signature
    public Memory __debugInfo() {
        ArrayMemory arr = ArrayMemory.createHashed();
        arr.refOfIndex("*file").assign(file);
        arr.refOfIndex("*dirAllowed").assign(dirAllowed());
        arr.refOfIndex("*directoriesListed").assign(this.directoriesListed());
        arr.refOfIndex("*cacheControl").assign(this.cacheControl());
        arr.refOfIndex("*pathInfoOnly").assign(this.pathInfoOnly());
        arr.refOfIndex("*acceptRanges").assign(this.acceptRanges());
        arr.refOfIndex("*etags").assign(this.etags());
        arr.refOfIndex("*redirectWelcome").assign(this.redirectWelcome());
        return arr;
    }

    @Signature
    public boolean etags() {
        return resourceHandler.isEtags();
    }

    @Signature
    synchronized public void etags(boolean value) {
        resourceHandler.setEtags(value);
    }

    @Signature
    public boolean acceptRanges() {
        return resourceHandler.isAcceptRanges();
    }

    @Signature
    synchronized public void acceptRanges(boolean value) {
        resourceHandler.setAcceptRanges(value);
    }

    @Signature
    public boolean dirAllowed() {
        return resourceHandler.isDirAllowed();
    }

    @Signature
    synchronized public void dirAllowed(boolean value) {
        resourceHandler.setDirAllowed(value);
    }

    @Signature
    public boolean directoriesListed() {
        return resourceHandler.isDirectoriesListed();
    }

    @Signature
    synchronized public void directoriesListed(boolean value) {
        resourceHandler.setDirectoriesListed(value);
    }

    @Signature
    public boolean pathInfoOnly() {
        return resourceHandler.isPathInfoOnly();
    }

    @Signature
    synchronized public void pathInfoOnly(boolean value) {
        resourceHandler.setPathInfoOnly(value);
    }

    @Signature
    public boolean redirectWelcome() {
        return resourceHandler.isRedirectWelcome();
    }

    @Signature
    synchronized public void redirectWelcome(boolean value) {
        resourceHandler.setRedirectWelcome(value);
    }

    @Signature
    public String cacheControl() {
        return cacheControl;
    }

    @Signature
    synchronized public void cacheControl(String value) {
        this.cacheControl = value;
    }

    @Signature
    public String file() {
        return file;
    }

    @Signature
    synchronized public void file(String value) {
        this.file = value;
    }

    @Signature
    public void __invoke(PHttpServerRequest request, PHttpServerResponse response) throws Exception {
        final boolean[] once = {false};

        ResourceHandler resourceHandler = new ResourceHandler() {
            @Override
            public Resource getResource(String path) {
                if (!once[0]) {
                    once[0] = true;
                    Object attr = request.getRequest().getAttribute("**");
                    File file;

                    if (attr != null) {
                        file = new File(PHttpResourceHandler.this.file, "/" + attr);
                    } else {
                        file = new File(PHttpResourceHandler.this.file);
                    }

                    if (file.exists()) {
                        return new PathResource(file.getAbsoluteFile());
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        };

        resourceHandler.doStart();

        resourceHandler.setResourceBase(file());

        if (cacheControl() != null) {
            resourceHandler.setCacheControl(cacheControl());
        }

        resourceHandler.setRedirectWelcome(redirectWelcome());
        resourceHandler.setPathInfoOnly(pathInfoOnly());
        resourceHandler.setDirectoriesListed(directoriesListed());
        resourceHandler.setDirAllowed(dirAllowed());
        resourceHandler.setAcceptRanges(acceptRanges());
        resourceHandler.setEtags(etags());

        Request baseRequest = Request.getBaseRequest(request.getRequest());

        resourceHandler.handle(
                request.getRequest().getRequestURI(), baseRequest, request.getRequest(), response.getResponse()
        );
    }
}
