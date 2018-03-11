package org.develnext.jphp.ext.javafx.support;

import com.sun.javafx.scene.control.skin.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;

import java.lang.reflect.Field;

public class FixMenuSkinBar extends com.sun.javafx.scene.control.skin.MenuBarSkin {
    public FixMenuSkinBar(MenuBar control) {
        super(control);

        try {
            Field field = null;
            field = getClass().getSuperclass().getDeclaredField("firstMenuRunnable");

            field.setAccessible(true);
            field.set(this, new Runnable() {
                @Override
                public void run() {
                    ;
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // nop
        }
    }
}
