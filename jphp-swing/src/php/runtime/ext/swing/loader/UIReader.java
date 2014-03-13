package php.runtime.ext.swing.loader;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;
import php.runtime.ext.swing.loader.support.propertyreaders.PropertyReaders;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class UIReader {
    protected final DocumentBuilderFactory builderFactory;
    protected final Map<Class<? extends Component>, PropertyReaders> readers;
    protected final Map<String, BaseTag> tags;

    protected TranslateHandler translateHandler;
    protected ReadHandler readHandler;

    public UIReader() {
        builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setIgnoringComments(true);
        builderFactory.setIgnoringElementContentWhitespace(true);

        readers = SwingExtension.readers;
        tags = SwingExtension.readerTags;
    }

    public TranslateHandler getTranslateHandler() {
        return translateHandler;
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

    @SuppressWarnings("unchecked")
    protected Component readElement(Node element) {
        String name = element.getNodeName();
        BaseTag tag = tags.get(name);
        if (tag != null) {
            Component component = tag.create(new ElementItem(element), this);
            if (tag.isNeedRegister())
                SwingExtension.registerComponent(component);

            ElementItem item = new ElementItem(element);
            tag.read(item, component, element);

            NamedNodeMap attrs = element.getAttributes();
            String var = null;
            for(int i = 0; i < attrs.getLength(); i++){
                Node attr = attrs.item(i);
                if (attr.getNodeName().equals("var")) {
                    var = attr.getNodeValue();
                    continue;
                }

                PropertyReader reader = null;
                Class cls = component.getClass();

                do {
                    PropertyReaders readers = this.readers.get(cls);
                    if (readers != null) {
                        reader = readers.getReader(attr.getNodeName());
                    }
                    if (cls == Component.class)
                        break;
                    if (reader != null)
                        break;
                    cls = cls.getSuperclass();
                } while (true);

                if (reader != null){
                    Value value = new Value(attr.getNodeValue());
                    if (reader.isTranslatable() && translateHandler != null) {
                        value = translateHandler.onTranslate(component, value);
                    }

                    reader.read(tag.applyProperty(attr.getNodeName(), component), value);
                }
            }
            tag.afterRead(item, component, element);

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
                        tag.addUnknown(component, node);
                    }
                }
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
}
