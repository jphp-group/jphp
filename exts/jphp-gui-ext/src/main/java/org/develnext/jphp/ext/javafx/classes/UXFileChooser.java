package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Name(JavaFXExtension.NS + "UXFileChooser")
public class UXFileChooser extends BaseWrapper<FileChooser> {
    interface WrappedInterface {
        @Property String title();
        @Property String initialFileName();
    }

    public UXFileChooser(Environment env, FileChooser wrappedObject) {
        super(env, wrappedObject);
    }

    public UXFileChooser(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new FileChooser();
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

    @Getter
    public int getSelectedExtensionFilter() {
        FileChooser.ExtensionFilter filter = getWrappedObject().getSelectedExtensionFilter();

        if (filter == null) {
            return - 1;
        }

        int i = 0;

        for (FileChooser.ExtensionFilter extensionFilter : getWrappedObject().getExtensionFilters()) {
            if (extensionFilter == filter) {
                return i;
            }

            i++;
        }

        return -1;
    }

    @Setter
    public void setSelectedExtensionFilter(int index) {
        ObservableList<FileChooser.ExtensionFilter> extensionFilters = getWrappedObject().getExtensionFilters();

        if (index < 0 || index > extensionFilters.size() - 1) {
            getWrappedObject().setSelectedExtensionFilter(null);
            return;
        }

        getWrappedObject().setSelectedExtensionFilter(extensionFilters.get(index));
    }

    @Getter
    public ArrayMemory getExtensionFilters() {
        ArrayMemory result = new ArrayMemory();

        for (FileChooser.ExtensionFilter filter : getWrappedObject().getExtensionFilters()) {
            result.refOfIndex("description").assign(filter.getDescription());
            result.refOfIndex("extensions").assign(ArrayMemory.ofStringCollection(filter.getExtensions()));
        }

        return result.toConstant();
    }

    @Setter
    public void setExtensionFilters(ForeachIterator filters) {
        getWrappedObject().getExtensionFilters().clear();

        while (filters.next()) {
            Memory value = filters.getValue();

            if (!value.isArray()) {
                throw new IllegalArgumentException();
            }

            Memory extensions = value.valueOfIndex("extensions");

            if (!extensions.isArray()) {
                throw new IllegalArgumentException();
            }

            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                    value.valueOfIndex("description").toString(),
                    Arrays.asList(extensions.toValue(ArrayMemory.class).toStringArray())
            );

            getWrappedObject().getExtensionFilters().add(filter);
        }
    }

    @Signature
    public File execute() {
        return showOpenDialog();
    }

    @Signature
    public File showOpenDialog(@Nullable Window window) {
        if (getWrappedObject().getInitialDirectory() != null && !getWrappedObject().getInitialDirectory().isDirectory()) {
            getWrappedObject().setInitialDirectory(null);
        }

        File file = getWrappedObject().showOpenDialog(window);

        if (file != null) {
            File parentFile = file.getParentFile();

            if (parentFile != null) {
                getWrappedObject().setInitialDirectory(parentFile);
            }
        }

        return file;
    }

    @Signature
    public File showSaveDialog(@Nullable Window window) {
        if (getWrappedObject().getInitialDirectory() != null && !getWrappedObject().getInitialDirectory().isDirectory()) {
            getWrappedObject().setInitialDirectory(null);
        }

        File file = getWrappedObject().showSaveDialog(window);

        if (file != null) {
            getWrappedObject().setInitialDirectory(file.getParentFile());
        }

        return file;
    }

    @Signature
    public List<File> showOpenMultipleDialog(@Nullable Window window) {
        if (getWrappedObject().getInitialDirectory() != null && !getWrappedObject().getInitialDirectory().isDirectory()) {
            getWrappedObject().setInitialDirectory(null);
        }

        List<File> files = getWrappedObject().showOpenMultipleDialog(window);

        if (files == null) {
            return Collections.emptyList();
        }

        if (!files.isEmpty()) {
            getWrappedObject().setInitialDirectory(files.get(0).getParentFile());
        }

        return files;
    }

    @Signature
    public File showOpenDialog() {
        return showOpenDialog(null);
    }

    @Signature
    public File showSaveDialog() {
        return showSaveDialog(null);
    }

    @Signature
    public List<File> showOpenMultipleDialog() {
        return showOpenMultipleDialog(null);
    }
}
