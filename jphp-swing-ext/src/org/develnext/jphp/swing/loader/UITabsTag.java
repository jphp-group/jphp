package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.loader.support.Value;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.swing.*;

@Tag("ui-tabs")
public class UITabsTag extends BaseTag<JTabbedPane> {
    @Override
    public JTabbedPane create(ElementItem element, UIReader uiReader) {
        return new JTabbedPane();
    }

    @Override
    public void addUnknown(JTabbedPane component, Node node) {
        if ("tab".equals(node.getNodeName())) {
            NamedNodeMap attrs = node.getAttributes();

            int index = 0;
            if (attrs.getNamedItem("index") != null)
                index = new Value(attrs.getNamedItem("index").getNodeValue()).asInteger();

            String title = null;
            if (attrs.getNamedItem("title") != null)
                title = new Value(attrs.getNamedItem("title").getNodeValue()).asString();

            String tooltip = null;
            if (attrs.getNamedItem("tooltip") != null)
                tooltip = new Value(attrs.getNamedItem("tooltip").getNodeValue()).asString();

            Icon icon = null;
            if (attrs.getNamedItem("icon") != null)
                icon = new Value(attrs.getNamedItem("icon").getNodeValue()).asIcon();

            if (title != null)
                component.setTitleAt(index, title);

            if (tooltip != null)
                component.setToolTipTextAt(index, tooltip);

            if (icon != null)
                component.setIconAt(index, icon);
        }
    }
}
