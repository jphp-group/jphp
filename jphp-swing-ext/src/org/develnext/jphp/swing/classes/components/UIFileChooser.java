package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.common.Constants;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.FileObject;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.classes.components.support.UIWindow;
import php.runtime.invoke.Invoker;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIFileChooser")
public class UIFileChooser extends UIContainer {
    protected JFileChooser component;

    public UIFileChooser(Environment env, JFileChooser component) {
        super(env);
        this.component = component;
    }

    public UIFileChooser(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JFileChooser) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JFileChooser();
    }

    @Signature
    protected Memory __getDialogTitle(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getDialogTitle());
    }

    @Signature(@Arg("value"))
    protected Memory __setDialogTitle(Environment env, Memory... args) {
        component.setDialogTitle(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMultiSelection(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isMultiSelectionEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setMultiSelection(Environment env, Memory... args) {
        component.setMultiSelectionEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectionMode(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getFileSelectionMode());
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionMode(Environment env, Memory... args) {
        component.setFileSelectionMode(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getDragEnabled(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getDragEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setDragEnabled(Environment env, Memory... args) {
        component.setDragEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getFileHiding(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isFileHidingEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setFileHiding(Environment env, Memory... args) {
        component.setFileHidingEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getControlButtonVisible(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getControlButtonsAreShown());
    }

    @Signature(@Arg("value"))
    protected Memory __setControlButtonVisible(Environment env, Memory... args) {
        component.setControlButtonsAreShown(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getApproveButtonText(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getApproveButtonText());
    }

    @Signature(@Arg("value"))
    protected Memory __setApproveButtonText(Environment env, Memory... args) {
        component.setApproveButtonText(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getCurrentDirectory(Environment env, Memory... args) {
        return ObjectMemory.valueOf(new FileObject(env, component.getCurrentDirectory()));
    }

    @Signature(@Arg(value = "value", typeClass = "php\\io\\File", optional = @Optional("NULL")))
    protected Memory __setCurrentDirectory(Environment env, Memory... args) {
        component.setCurrentDirectory(args[0].toObject(FileObject.class).getFile());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedFile(Environment env, Memory... args) {
        if (component.getSelectedFile() == null)
            return Memory.NULL;
        return ObjectMemory.valueOf(new FileObject(env, component.getSelectedFile()));
    }

    @Signature(@Arg(value = "value"))
    protected Memory __setSelectedFile(Environment env, Memory... args) {
        if (args[0].instanceOf(FileObject.class)){
            component.setSelectedFile(args[0].toObject(FileObject.class).getFile());
        } else {
            component.setSelectedFile(new File(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedFiles(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        for(File f : component.getSelectedFiles()) {
            r.add(ObjectMemory.valueOf(
                    new FileObject(env, component.getSelectedFile())
            ));
        }
        return r.toConstant();
    }

    @Signature(@Arg(value = "value", type = HintType.ARRAY))
    protected Memory __setSelectedFiles(Environment env, Memory... args) {
        ArrayMemory value = args[0].toValue(ArrayMemory.class);
        File[] files = new File[value.size()];

        ForeachIterator iterator = value.foreachIterator(false, false);
        int i = 0;
        while (iterator.next()) {
            Memory el = iterator.getValue();
            if (el.instanceOf(FileObject.class)) {
                files[i] = el.toObject(FileObject.class).getFile();
            } else {
                files[i] = new File(el.toString());
            }
            i++;
        }

        component.setSelectedFiles(files);
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "approveText"),
            @Arg(value = "window", typeClass = SwingExtension.NAMESPACE + "UIWindow", optional = @Optional("NULL"))
    })
    public Memory showDialog(Environment env, Memory... args) {
        Window window = null;
        if (!args[1].isNull())
            window = args[1].toObject(UIWindow.class).getWindow();
        int r = component.showDialog(window, args[0].toBinaryString());
        return TrueMemory.valueOf(r = JFileChooser.APPROVE_OPTION);
    }

    @Signature({
            @Arg(value = "window", typeClass = SwingExtension.NAMESPACE + "UIWindow", optional = @Optional("NULL"))
    })
    public Memory showSaveDialog(Environment env, Memory... args) {
        Window window = null;
        if (!args[0].isNull())
            window = args[0].toObject(UIWindow.class).getWindow();
        int r = component.showSaveDialog(window);
        return TrueMemory.valueOf(r == JFileChooser.APPROVE_OPTION);
    }

    @Signature({
            @Arg(value = "window", typeClass = SwingExtension.NAMESPACE + "UIWindow", optional = @Optional("NULL"))
    })
    public Memory showOpenDialog(Environment env, Memory... args) {
        Window window = null;
        if (!args[0].isNull())
            window = args[0].toObject(UIWindow.class).getWindow();
        int r = component.showOpenDialog(window);
        return TrueMemory.valueOf(r == JFileChooser.APPROVE_OPTION);
    }

    @Signature(@Arg("file"))
    public Memory isTraversable(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isTraversable(FileObject.valueOf(args[0])));
    }

    @Signature(@Arg("file"))
    public Memory ensureFileIsVisible(Environment env, Memory... args) {
        component.ensureFileIsVisible(FileObject.valueOf(args[0]));
        return Memory.NULL;
    }

    @Signature
    public Memory approveSelection(Environment env, Memory... args) {
        component.approveSelection();
        return Memory.NULL;
    }

    @Signature
    public Memory cancelSelection(Environment env, Memory... args) {
        component.cancelSelection();
        return Memory.NULL;
    }

    @Signature
    public Memory changeToParentDirectory(Environment env, Memory... args) {
        component.changeToParentDirectory();
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "extensions", type = HintType.ARRAY),
            @Arg(value = "description"),
            @Arg(value = "showDirectories", optional = @Optional(value = "1", type = HintType.BOOLEAN))
    })
    public Memory addChoosableExtensions(final Environment env, final Memory... args) {
        final Set<String> extensions = new HashSet<String>();
        ForeachIterator iterator = args[0].getNewIterator(env, false, false);
        while (iterator.next()) {
            extensions.add(iterator.getValue().toString());
        }

        final String description = args[1].toString();
        component.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return args[2].toBoolean();
                }

                String path = f.getPath();
                for(String ext : extensions) {
                    if (Constants.PATH_NAME_CASE_INSENSITIVE) {
                        if (path.endsWith("." + ext))
                            return true;
                    } else {
                        if (path.toLowerCase().endsWith("." + ext.toLowerCase()))
                            return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return description;
            }
        });
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "filter", type = HintType.CALLABLE),
            @Arg(value = "description")
    })
    public Memory addChoosableFilter(final Environment env, Memory... args) {
        final Invoker invoker = Invoker.valueOf(env, null, args[0]);
        final String description = args[1].toString();

        component.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return invoker.callNoThrow(new ObjectMemory(new FileObject(env, f))).toBoolean();
            }

            @Override
            public String getDescription() {
                return description;
            }
        });
        return Memory.NULL;
    }

    @Signature
    public Memory resetChoosableFilters(Environment env, Memory... args) {
        component.resetChoosableFileFilters();
        return Memory.NULL;
    }
}
