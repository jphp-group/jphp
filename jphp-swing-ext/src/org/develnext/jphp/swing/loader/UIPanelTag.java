package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-panel")
public class UIPanelTag extends BaseTag<JPanel> {
    @Override
    public JPanel create(ElementItem element, UIReader uiReader) {
        return new JPanel();
    }

    @Override
    public void read(ElementItem element, JPanel component, Node node, UIReader uiReader) {
        component.setLayout(new XYLayout());
    }
}
