package org.develnext.jphp.swing.support;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class JTextFieldX extends JTextField implements RootTextElement {
    @Override
    public JTextComponent getTextComponent() {
        return this;
    }
}
