package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.loader.support.BaseTag;
import org.w3c.dom.Node;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JFrameX;

import javax.swing.*;
import java.awt.*;

@Tag("ui-form")
public class UIFormTag extends BaseTag<Container> {
    @Override
    public Container create(ElementItem element, UIReader uiReader) {
        if (uiReader.isUseInternalForms())
            return new JInternalFrame();
        else
            return new JFrameX();
    }

    @Override
    public void read(ElementItem element, Container component, Node node, UIReader uiReader) {
        component.setLayout(new XYLayout());
    }
}
