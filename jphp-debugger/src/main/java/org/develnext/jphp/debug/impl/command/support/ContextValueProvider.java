package org.develnext.jphp.debug.impl.command.support;

import org.apache.commons.codec.binary.Base64;
import org.develnext.jphp.debug.impl.Debugger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.Memory;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ContextValueProvider {
    protected final Debugger debugger;
    protected final Document document;

    protected int maxData;
    protected int maxChildren;

    public ContextValueProvider(Debugger debugger, Document document) {
        this.debugger = debugger;
        this.document = document;

        this.maxData = 99;
        this.maxChildren = 30;

        if (debugger != null && debugger.ideFeatures.has("max_data")) {
            this.maxData = debugger.ideFeatures.getInteger("max_data");
        }

        if (debugger != null && debugger.ideFeatures.has("max_children")) {
            this.maxChildren = debugger.ideFeatures.getInteger("max_children");
        }
    }

    public int getMaxData() {
        return maxData;
    }

    public void setMaxData(int maxData) {
        this.maxData = maxData;
    }

    public int getMaxChildren() {
        return maxChildren;
    }

    public void setMaxChildren(int maxChildren) {
        this.maxChildren = maxChildren;
    }

    public Element getProperty(String name, Memory value) {
        return getProperty(name, value, new HashSet<Integer>());
    }

    protected Element getProperty(String name, Memory value, Set<Integer> used) {
        Element property = document.createElement("property");

        if (name != null) {
            property.setAttribute("name", "$" + name);
            property.setAttribute("fullname", "$" + name);
        }

        property.setAttribute("type", value.type.toString().toLowerCase());
        property.setAttribute("constant", "0");
        property.setAttribute("children", "0");
        property.setAttribute("size", "1");

        //property.setAttribute("address", String.valueOf(value.getPointer()));
        property.setAttribute("encoding", "base64");

        switch (value.getRealType()) {
            case ARRAY:
                if (used.add(value.getPointer())) {
                    processArray(property, value.toValue(ArrayMemory.class), used, false);
                    used.remove(value.getPointer());
                } else {
                    property.appendChild(document.createCDATASection(Base64.encodeBase64String("**RECURSION**".getBytes())));
                }
                break;
            case OBJECT:
                if (used.add(value.getPointer())) {
                    processObject(property, value.toValue(ObjectMemory.class), used);
                    used.remove(value.getPointer());
                } else {
                    property.appendChild(document.createCDATASection(Base64.encodeBase64String("**RECURSION**".getBytes())));
                }
                break;
            case BOOL:
                property.setAttribute("type", "bool");
                property.setAttribute("value", value.toBoolean() ? "1" : "0");
                break;
            case NULL:
                break;
            case STRING:
                processString(property, value.toValue(StringMemory.class));
            default:
                byte[] bytes = value.toString().getBytes();

                if (maxData > 0 && bytes.length > maxData) {
                    property.setAttribute("size", String.valueOf(bytes.length));
                    bytes = Arrays.copyOf(bytes, maxData);
                }

                property.appendChild(document.createCDATASection(Base64.encodeBase64String(bytes)));
                break;
        }

        return property;
    }

    protected void processObject(Element property, ObjectMemory objectMemory, Set<Integer> used) {
        IObject value = objectMemory.value;
        ArrayMemory properties = value.getProperties();

        property.setAttribute("classname", value.getReflection().getName());

        if (properties != null) {
            processArray(property, properties, used, true);
        }
    }

    protected void processString(Element property, StringMemory stringMemory) {
        property.setAttribute("size", String.valueOf(stringMemory.toString().length()));
    }

    protected void processArray(Element property, ArrayMemory array, Set<Integer> used, boolean forObjects) {
        if (array.size() != 0) {
            property.setAttribute("children", "1");
        }

        property.setAttribute("numchildren", String.valueOf(array.size()));

        ForeachIterator iterator = array.foreachIterator(false, false);

        int count = 0;

        while (iterator.next()) {
            if (iterator.getValue().isUndefined()) {
                continue;
            }

            Element value = getProperty(null, iterator.getValue().toValue(), used);

            String key = iterator.getKey().toString();

            if (forObjects) {
                key = key.replace('\0', '*');

                if (key.startsWith("***")) {
                    key = key.substring(3);
                }
            }

            value.setAttribute("name", key);

            property.appendChild(value);
            count++;

            if (maxChildren > 0 && count >= maxChildren) {
                break;
            }
        }
    }
}
