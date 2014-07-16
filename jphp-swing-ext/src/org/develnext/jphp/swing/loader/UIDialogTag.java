package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.loader.support.BaseTag;
import org.w3c.dom.Node;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JDialogX;

import javax.swing.*;
import java.awt.*;

@Tag("ui-dialog")
public class UIDialogTag extends BaseTag<Container> {
    @Override
    public Container create(ElementItem element, UIReader uiReader) {
        if (uiReader.isUseInternalForms()) {
            JInternalFrame frame = new JInternalFrame();
            return frame;
        } else
            return new JDialogX();
    }

    @Override
    public void read(ElementItem element, Container component, Node node, UIReader uiReader) {
        component.setLayout(new XYLayout());
    }
}
