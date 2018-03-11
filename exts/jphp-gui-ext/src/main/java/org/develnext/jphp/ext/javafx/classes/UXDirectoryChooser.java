package org.develnext.jphp.ext.javafx.classes;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.File;

@Name(JavaFXExtension.NS + "UXDirectoryChooser")
public class UXDirectoryChooser extends BaseWrapper<DirectoryChooser> {
    interface WrappedInterface {
        @Property String title();
    }

    public UXDirectoryChooser(Environment env, DirectoryChooser wrappedObject) {
        super(env, wrappedObject);
    }

    public UXDirectoryChooser(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new DirectoryChooser();
    }

    @Getter
    public File getInitialDirectory() {
        return getWrappedObject().getInitialDirectory();
    }

    @Setter
    public void setInitialDirectory(Memory value) {
        if (value.isNull()) {
            getWrappedObject().setInitialDirectory(null);
        } else {
            getWrappedObject().setInitialDirectory(new File(value.toString()));
        }
    }

    @Signature
    public File execute() {
        return getWrappedObject().showDialog(null);
    }

    @Signature
    public File showDialog(@Reflection.Nullable Window window) {
        return getWrappedObject().showDialog(window);
    }

    @Signature
    public File showDialog() {
        return showDialog(null);
    }

}
