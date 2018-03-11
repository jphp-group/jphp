package org.develnext.jphp.ext.javafx.support;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class BugFix8Utils {
    static public void disableContextMenu(TextField textField) {
        textField.setSkin(new TextFieldSkin(textField, new TextFieldBehavior(textField) {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    return; // don't allow context menu to show default context menu
                }

                super.mouseReleased(e);
            }
        }));
    }
}
