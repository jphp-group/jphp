package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-combobox")
public class UIComboboxTag extends BaseTag<JComboBox> {
    @Override
    public JComboBox create(ElementItem element, UIReader uiReader) {
        return new JComboBox(new DefaultComboBoxModel());
    }

    @Override
    public void read(ElementItem element, JComboBox component, Node node, UIReader uiReader) {

    }

    @Override
    public void addUnknown(JComboBox component, Node node) {
        if (node.getNodeName().equalsIgnoreCase("item")) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) component.getModel();
            model.addElement(node.getTextContent());
        }
    }
}
