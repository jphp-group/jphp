package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Name(JavaFXExtension.NS + "UXClipboard")
public class UXClipboard extends BaseObject {
    private static final Clipboard clipboard = Clipboard.getSystemClipboard();

    public UXClipboard(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    synchronized public static void clear() {
        clipboard.clear();
    }

    @Signature
    synchronized public static boolean hasFiles() {
        return clipboard.hasFiles();
    }

    @Signature
    synchronized public static boolean hasText() {
        return clipboard.hasString();
    }

    @Signature
    synchronized public static boolean hasHtml() {
        return clipboard.hasHtml();
    }

    @Signature
    synchronized public static boolean hasUrl() {
        return clipboard.hasUrl();
    }

    @Signature
    synchronized public static boolean hasImage() {
        return clipboard.hasImage();
    }

    @Signature
    synchronized public static String getUrl() {
        return clipboard.getUrl();
    }

    @Signature
    synchronized public static String getHtml() {
        return clipboard.getHtml();
    }

    @Signature
    synchronized public static String getText() {
        return clipboard.getString();
    }

    @Signature
    synchronized public static List<File> getFiles() {
        return clipboard.getFiles();
    }

    @Signature
    synchronized public static Image getImage() {
        return clipboard.getImage();
    }

    @Signature
    synchronized public static void setContent(Environment env, ArrayMemory _content) {
        ClipboardContent content = new ClipboardContent();

        if (_content.containsKey("text")) {
            content.putString(_content.valueOfIndex("text").toString());
        }

        if (_content.containsKey("url")) {
            content.putUrl(_content.valueOfIndex("url").toString());
        }

        if (_content.containsKey("html")) {
            content.putHtml(_content.valueOfIndex("html").toString());
        }

        if (_content.containsKey("image")) {
            Memory image = _content.valueOfIndex("image");

            if (image.instanceOf(UXImage.class)) {
                content.putImage(image.toObject(UXImage.class).getWrappedObject());
            } else {
                throw new IllegalArgumentException("The 'image' key should contain an instance of the image class");
            }
        }

        if (_content.containsKey("files")) {
            Memory files = _content.valueOfIndex("files");

            if (files.isTraversable()) {
                ForeachIterator iterator = files.getNewIterator(env);

                List<String> paths = new ArrayList<>();
                while (iterator.next()) {
                    paths.add(iterator.getValue().toString());
                }

                content.putFilesByPath(paths);
            } else {
                throw new IllegalArgumentException("The 'files' key should contain a traversable value");
            }
        }

        clipboard.setContent(content);
    }

    @Signature
    synchronized public static void setText(String value) {
        ClipboardContent content = new ClipboardContent();
        content.putString(value);

        clipboard.setContent(content);
    }
}
