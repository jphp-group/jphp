package org.develnext.jphp.ext.jsoup;

import org.develnext.jphp.ext.jsoup.bind.BinaryMemoryOperation;
import org.develnext.jphp.ext.jsoup.bind.UrlMemoryOperation;
import org.develnext.jphp.ext.jsoup.classes.*;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.memory.support.MemoryOperation;

public class JsoupExtension extends Extension {
    public static final String NS = "php\\jsoup";

    @Override
    public Status getStatus() {
        return Status.STABLE;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "jsoup" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, WrapJsoup.class);

        registerWrapperClass(scope, Connection.class, WrapConnection.class);
        registerWrapperClass(scope, Connection.Response.class, WrapConnectionResponse.class);
        registerWrapperClass(scope, Connection.Request.class, WrapConnectionRequest.class);
        registerWrapperClass(scope, Elements.class, WrapElements.class);
        registerWrapperClass(scope, Document.class, WrapDocument.class);
        registerWrapperClass(scope, Element.class, WrapElement.class);

        MemoryOperation.register(new UrlMemoryOperation());
        //MemoryOperation.register(new BinaryMemoryOperation());
    }
}
