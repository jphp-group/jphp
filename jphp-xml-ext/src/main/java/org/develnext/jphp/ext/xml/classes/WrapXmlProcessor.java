package org.develnext.jphp.ext.xml.classes;

import org.develnext.jphp.ext.xml.XmlExtension;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.java.JavaException;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.support.ReflectionUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

@Name(XmlExtension.NAMESPACE + "XmlProcessor")
public class WrapXmlProcessor extends WrapProcessor {
    protected DocumentBuilderFactory builderFactory;
    protected DocumentBuilder builder;

    protected TransformerFactory transformerFactory;
    protected Transformer transformer;

    protected Invoker onWarning;
    protected Invoker onError;
    protected Invoker onFatalError;

    public WrapXmlProcessor(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(@Arg(value = "flags", optional = @Optional("0")))
    public Memory __construct(final Environment env, Memory... args) throws ParserConfigurationException, TransformerConfigurationException {
        transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        builderFactory = DocumentBuilderFactory.newInstance();
        builder = builderFactory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                if (onWarning != null) {
                    onWarning.callAny(new JavaException(env, exception));
                }
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                if (onError != null) {
                    onError.callAny(new JavaException(env, exception));
                }
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                if (onFatalError != null) {
                    onFatalError.callAny(new JavaException(env, exception));
                } else {
                    throw exception;
                }
            }
        });

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory parse(Environment environment, Memory... args) {
        if (onWarning != null) onWarning.setTrace(environment.trace());
        if (onError != null) onError.setTrace(environment.trace());
        if (onFatalError != null) onFatalError.setTrace(environment.trace());

        InputStream stream = null;
        try {
            if (args[0].instanceOf(Stream.class)) {
                stream = Stream.getInputStream(environment, args[0]);
            } else {
                stream = new ByteArrayInputStream(args[0].getBinaryBytes(environment.getDefaultCharset()));
            }

            Document document = builder.parse(stream);
            return new ObjectMemory(new WrapDomDocument(environment, document));
        } catch (SAXException | IOException e) {
            environment.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        } finally {
            if (stream != null) Stream.closeStream(environment, stream);
        }
    }

    @Override
    @Signature
    public Memory format(Environment environment, Memory... args) {
        if (!args[0].instanceOf(WrapDomDocument.class)) {
            throw new IllegalArgumentException(
                    "Argument #1 must be instance of " + ReflectionUtils.getClassName(WrapDomDocument.class)
            );
        }

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        try {
            transformer.transform(new DOMSource(args[0].toObject(WrapDomDocument.class).getWrappedObject()), result);
            return StringMemory.valueOf(writer.toString());
        } catch (TransformerException e) {
            environment.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        }
    }

    @Override
    @Signature
    public Memory formatTo(Environment environment, Memory... args) {
        if (!args[0].instanceOf(WrapDomDocument.class)) {
            throw new IllegalArgumentException(
                    "Argument #1 must be instance of " + ReflectionUtils.getClassName(WrapDomDocument.class)
            );
        }

        OutputStream output = Stream.getOutputStream(environment, args[1]);
        StreamResult result = new StreamResult(output);

        try {
            transformer.transform(new DOMSource(args[0].toObject(WrapDomDocument.class).getWrappedObject()), result);
        } catch (TransformerException e) {
            environment.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        } finally {
            Stream.closeStream(environment, output);
        }

        return Memory.NULL;
    }

    @Signature
    public Document createDocument() {
        return builder.newDocument();
    }

    @Signature
    public void onWarning(@Nullable Invoker invoker) {
        onWarning = invoker;
    }

    @Signature
    public void onError(@Nullable Invoker invoker) {
        onError = invoker;
    }

    @Signature
    public void onFatalError(@Nullable Invoker invoker) {
        onFatalError = invoker;
    }
}
