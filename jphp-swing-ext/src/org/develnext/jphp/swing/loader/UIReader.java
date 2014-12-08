package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.Scope;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;
import org.develnext.jphp.swing.loader.support.propertyreaders.PropertyReaders;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class UIReader {
    protected final DocumentBuilderFactory builderFactory;
    protected final Map<Class<? extends Component>, PropertyReaders> readers;
    protected final Map<String, BaseTag> tags;

    protected final Map<String, Style> styles;

    protected TranslateHandler translateHandler;
    protected ReadHandler readHandler;

    protected boolean useInternalForms;

    protected Map<String, Scope> scopes;

    public UIReader() {
        builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setIgnoringComments(true);
        builderFactory.setIgnoringElementContentWhitespace(true);

        styles = new HashMap<String, Style>();

        readers = SwingExtension.readers;
        tags = SwingExtension.readerTags;

        scopes = new HashMap<String, Scope>();
    }

    public TranslateHandler getTranslateHandler() {
        return translateHandler;
    }

    public String translate(Component component, String value) {
        if (translateHandler == null)
            return value;
        return translateHandler.onTranslate(component, new Value(value)).asString();
    }

    public void setTranslateHandler(TranslateHandler translateHandler) {
        this.translateHandler = translateHandler;
    }

    public ReadHandler getReadHandler() {
        return readHandler;
    }

    public void setReadHandler(ReadHandler readHandler) {
        this.readHandler = readHandler;
    }

    public boolean isUseInternalForms() {
        return useInternalForms;
    }

    public void setUseInternalForms(boolean useInternalForms) {
        this.useInternalForms = useInternalForms;
    }

    public void registerStyle(Style style) {
        styles.put(style.getAttributes().get("name").asString().toLowerCase(), style);
    }

    @SuppressWarnings("unchecked")
    protected Component readElement(Node element) {
        String name = element.getNodeName();
        BaseTag tag = tags.get(name);
        if (tag != null) {
            java.util.List<Object[]> postRead = new ArrayList<Object[]>();
            Component component = tag.create(new ElementItem(element), this);
            if (tag.isNeedRegister())
                SwingExtension.registerComponent(component);

            ElementItem item = new ElementItem(element);
            tag.read(item, component, element, this);

            NamedNodeMap attrs = element.getAttributes();
            String var = null;

            Map<String, Value> attributes = new LinkedHashMap<String, Value>();
            for(int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                if (attr.getNodeName().equalsIgnoreCase("var")) {
                    var = attr.getNodeValue();
                    continue;
                }

                Value value = new Value(attr.getNodeValue());
                if (attr.getNodeName().equalsIgnoreCase("style")) {
                    for(String styleName : value.asArray(true)) {
                        Style style = styles.get(styleName.toLowerCase());
                        if (style != null) {
                            attributes.putAll(style.getAttributes());
                        }
                    }
                    continue;
                }

                attributes.put(attr.getNodeName().toLowerCase(), value);
            }

            for(Map.Entry<String, Value> attr : attributes.entrySet()){
                PropertyReader reader = null;
                Class cls = component.getClass();

                tag.onReadAttribute(item, attr.getKey(), attr.getValue(), component, this);
                do {
                    PropertyReaders readers = this.readers.get(cls);
                    if (readers != null) {
                        reader = readers.getReader(attr.getKey());
                    }
                    if (cls == Component.class)
                        break;
                    if (reader != null)
                        break;
                    cls = cls.getSuperclass();
                } while (true);

                if (reader != null){
                    Value value = attr.getValue();
                    if (reader.isTranslatable() && translateHandler != null) {
                        if (reader.isArrayed()) {
                            StringBuilder sb = new StringBuilder();
                            int x = 0;
                            for(String el : value.asArray(reader.isTrimArrayed())) {
                                if (x != 0)
                                    sb.append(",");

                                sb.append(translateHandler.onTranslate(component, new Value(el)).asString());
                                x++;
                            }
                            value = new Value(sb.toString());
                        } else
                            value = translateHandler.onTranslate(component, value);
                    }

                    if (reader.isPostRead())
                        postRead.add(new Object[]{ reader, attr.getKey(), value });
                    else {
                        reader.read(tag.applyProperty(attr.getKey(), component), value);
                    }
                }
            }
            tag.afterRead(item, component, element, this);

            if (readHandler != null && var != null) {
                readHandler.onRead(component, var);
            }

            if (tag.isAllowsChildren() && component instanceof Container) {
                NodeList list = element.getChildNodes();
                if (list != null)
                for(int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);
                    Component el = readElement(node);
                    if (el != null) {
                        BaseTag childTag = tags.get(node.getNodeName());
                        tag.addChildren(component, childTag.getContentPane(el));
                    } else {
                        tag.addUnknown(component, node, this);
                    }
                }
            }

            for(Object[] el : postRead) {
                PropertyReader reader = (PropertyReader) el[0];
                String property = (String) el[1];
                Value value = (Value) el[2];

                reader.read(tag.applyProperty(property, component), value);
            }

            return component;
        }
        return null;
    }

    public Component read(InputStream inputStream){
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element root = document.getDocumentElement();

            return readElement(root);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface ReadHandler {
        void onRead(Component component, String var);
    }

    public interface TranslateHandler {
        Value onTranslate(Component component, Value value);
    }

    public static class Style extends Component {
        protected Map<String, Value> attributes;

        public Style() {
            attributes = new LinkedHashMap<String, Value>();
            setVisible(false);
        }

        public Style(Map<String, Value> attributes) {
            if (attributes == null)
                throw new IllegalArgumentException();

            this.attributes = attributes;
            setVisible(false);
        }

        public void set(String attrName, Value value) {
            if (value == null)
                throw new IllegalArgumentException();

            attributes.put(attrName, value);
        }

        public Map<String, Value> getAttributes() {
            return attributes;
        }
    }
}
