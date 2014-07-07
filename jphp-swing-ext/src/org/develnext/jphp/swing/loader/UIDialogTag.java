package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JDialogX;

@Tag("ui-dialog")
public class UIDialogTag extends UIWindowTag<JDialogX> {
    @Override
    public JDialogX create(ElementItem element, UIReader uiReader) {
        return new JDialogX();
    }

    @Override
    public void read(ElementItem element, JDialogX component, Node node, UIReader uiReader) {
        component.setLayout(new XYLayout());
    }
}
