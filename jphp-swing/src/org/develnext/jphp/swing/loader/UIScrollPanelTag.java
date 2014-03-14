package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JScrollPanel;

import java.awt.*;

@Tag("ui-scroll-panel")
public class UIScrollPanelTag extends BaseTag<JScrollPanel> {
    @Override
    public JScrollPanel create(ElementItem element, UIReader uiReader) {
        return new JScrollPanel();
    }

    @Override
    public void read(ElementItem element, JScrollPanel component, Node node) {

    }

    @Override
    public void addChildren(JScrollPanel component, Component child) {
        component.setViewportView(child);
    }
}
