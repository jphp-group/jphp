package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

@Tag("ui-slider")
public class UISliderTag extends BaseTag<JSlider> {
    @Override
    public JSlider create(ElementItem element, UIReader uiReader) {
        return new JSlider();
    }

    @Override
    public void read(ElementItem element, JSlider component, Node node) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addUnknown(JSlider component, Node node) {
        if (node.getNodeName().equalsIgnoreCase("item")) {
            Dictionary hashtable = component.getLabelTable();
            if (component.getLabelTable() == null)
                hashtable = new Hashtable();

            Node value = node.getAttributes().getNamedItem("value");
            int index = hashtable.size() - 1;
            if (value != null)
                index = Integer.parseInt(value.getNodeValue());

            hashtable.put(index, new JLabel(node.getTextContent()));

            if (component.getLabelTable() == null)
                component.setLabelTable(hashtable);
        }
    }
}
