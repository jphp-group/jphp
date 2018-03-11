package org.develnext.jphp.ext.gui.desktop.classes;

import org.develnext.jphp.ext.gui.desktop.GuiDesktopExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

@Reflection.Name("php\\gui\\desktop\\Mouse")
public class MouseEx extends Mouse {
    public MouseEx(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
