package org.develnext.jphp.debug.impl.command.support;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.Memory;

public class ContextValueProvider {
    protected final Document document;

    public ContextValueProvider(Document document) {
        this.document = document;
    }

    public Element getProperty(String name, Memory value) {
        Element property = document.createElement("property");

        property.setAttribute("name", "$" + name);
        property.setAttribute("fullname", "$" + name);
        property.setAttribute("type", value.type.toString());
        property.setAttribute("constant", "0");
        property.setAttribute("children", "0");
        property.setAttribute("size", "1");
        property.setAttribute("address", String.valueOf(value.getPointer()));
        property.setAttribute("encoding", "base64");

        property.appendChild(document.createCDATASection(Base64.encodeBase64String(value.toString().getBytes())));

        return property;
    }
}
