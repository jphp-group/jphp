package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.w3c.dom.Node;

import javax.swing.*;

@Tag("ui-internal-form")
public class UIInternalFormTag extends BaseTag<JInternalFrame> {
    @Override
    public JInternalFrame create(ElementItem element, UIReader uiReader) {
        return new JInternalFrame();
    }

    @Override
    public void read(ElementItem element, JInternalFrame component, Node node, UIReader uiReader) {
        component.setLayout(new XYLayout());
    }
}
