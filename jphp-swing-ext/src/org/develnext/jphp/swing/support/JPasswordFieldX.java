package org.develnext.jphp.swing.support;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class JPasswordFieldX extends JPasswordField implements RootTextElement {
    @Override
    public JTextComponent getTextComponent() {
        return this;
    }
}
