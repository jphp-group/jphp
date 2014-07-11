package org.develnext.jphp.swing.support;

import org.develnext.jphp.swing.XYLayout;

import javax.swing.*;

public class JScrollPanelX extends JScrollableComponent<JPanel> {
    @Override
    protected JPanel newComponent() {
        return new JPanel(new XYLayout());
    }
}
