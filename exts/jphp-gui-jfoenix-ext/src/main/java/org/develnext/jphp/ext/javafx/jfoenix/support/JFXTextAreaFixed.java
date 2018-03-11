package org.develnext.jphp.ext.javafx.jfoenix.support;

import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.Skin;

public class JFXTextAreaFixed extends JFXTextArea {
    public JFXTextAreaFixed() {
    }

    public JFXTextAreaFixed(String text) {
        super(text);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new JFXTextAreaSkin(this);
    }
}
