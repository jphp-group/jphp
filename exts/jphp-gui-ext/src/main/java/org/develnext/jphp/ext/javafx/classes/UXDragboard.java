package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Reflection.Name(JavaFXExtension.NS + "UXDragboard")
public class UXDragboard extends BaseWrapper<Dragboard> {
    interface WrappedInterface {
        @Property @Nullable Image dragView();

        @Property double dragViewOffsetX();
        @Property double dragViewOffsetY();

        @Property Set<TransferMode> transferModes();
    }

    public UXDragboard(Environment env, Dragboard wrappedObject) {
        super(env, wrappedObject);
    }

    public UXDragboard(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Dragboard dragboard) {
        __wrappedObject = dragboard;
    }

    @Getter
    public String getString() {
        return getWrappedObject().hasString() ? getWrappedObject().getString() : null;
    }

    @Getter
    public String getUrl() {
        return getWrappedObject().hasUrl() ? getWrappedObject().getUrl() : null;
    }

    @Getter
    public Image getImage() {
        return getWrappedObject().hasImage() ? getWrappedObject().getImage() : null;
    }

    @Getter
    public List<File> getFiles() {
        return getWrappedObject().hasFiles() ? getWrappedObject().getFiles() : Collections.emptyList();
    }

    @Setter
    public void setString(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);

        getWrappedObject().setContent(content);
    }

    @Setter
    public void setImage(Image image) {
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);

        getWrappedObject().setContent(content);
    }

    @Setter
    public void setFiles(List<File> files) {
        ClipboardContent content = new ClipboardContent();
        content.putFiles(files);

        getWrappedObject().setContent(content);
    }

    @Setter
    public void setUrl(String url) {
        ClipboardContent content = new ClipboardContent();
        content.putUrl(url);

        getWrappedObject().setContent(content);
    }
}
