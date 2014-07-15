package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.*;
import java.awt.*;

@Tag("ui-toolbar")
public class UIToolBarTag extends BaseTag<JToolBar> {
    @Override
    public JToolBar create(ElementItem element, UIReader uiReader) {
        return new JToolBar();
    }

    @Override
    public void read(ElementItem element, JToolBar component, Node node, UIReader uiReader) {

    }

    @Override
    public void addUnknown(JToolBar component, Node node, UIReader uiReader) {
        if (node.getNodeName().equals("separator")) {
            ElementItem item = new ElementItem(node);
            Value size = null;
            if (item.getAttr("size") != null)
                size = new Value(item.getAttr("size"));

            if (size == null)
                component.addSeparator();
            else {
                Dimension dimension = size.asDimension();
                if (dimension.getHeight() < 0)
                    dimension = new Dimension((int)dimension.getWidth(),
                            component.getHeight() + ((int)dimension.getHeight() + 1));

                component.addSeparator(dimension);
            }
        }
    }
}
