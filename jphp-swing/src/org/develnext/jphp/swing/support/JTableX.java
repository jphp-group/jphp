package org.develnext.jphp.swing.support;

import javax.swing.*;

public class JTableX extends JScrollableComponent<JTable> {
    @Override
    protected JTable newComponent() {
        return new JTable();
    }
}
