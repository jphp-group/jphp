package org.develnext.jphp.swing.support;

import org.develnext.jphp.swing.XYLayout;

import javax.swing.*;
import java.awt.*;

public class JScrollPanelX extends JScrollableComponent<JPanel> {
    @Override
    protected JPanel newComponent() {
        return new JPanel(new XYLayout());
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        getContent().setPreferredSize(preferredSize);
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if (getContent() != null)
            getContent().add(comp, constraints, index);
        else
            super.addImpl(comp, constraints, index);
    }
}
