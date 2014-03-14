package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JFrameX;

@Tag("ui-form")
public class UIFormTag extends UIWindowTag<JFrameX> {
    @Override
    public JFrameX create(ElementItem element, UIReader uiReader) {
        return new JFrameX();
    }

    @Override
    public void read(ElementItem element, JFrameX component, Node node) {
        component.setLayout(new XYLayout());
    }
}
