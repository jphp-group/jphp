package org.develnext.jphp.ext.xml;

import org.develnext.jphp.ext.xml.classes.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.support.Extension;

public class XmlExtension extends Extension {
    public final static String NAMESPACE = "php\\xml\\";

    @Override
    public Status getStatus() {
        return Status.STABLE;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "std", "xml" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, Node.class, WrapDomNode.class);
        registerWrapperClass(scope, Element.class, WrapDomElement.class);
        registerWrapperClass(scope, NodeList.class, WrapDomNodeList.class);
        registerWrapperClass(scope, Document.class, WrapDomDocument.class);

        registerClass(scope, WrapXmlProcessor.class);
    }

    @Override
    public void onLoad(Environment env) {
        WrapProcessor.registerCode(env, "xml", WrapXmlProcessor.class);
    }
}
