package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-combobox")
public class UIComboboxTag extends BaseTag<JComboBox> {
    @Override
    public JComboBox create(ElementItem element, UIReader uiReader) {
        return new JComboBox(new DefaultComboBoxModel<String>());
    }

    @Override
    public void read(ElementItem element, JComboBox component, Node node) {

    }

    @Override
    public void addUnknown(JComboBox component, Node node) {
        if (node.getNodeName().equalsIgnoreCase("item")) {
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) component.getModel();
            model.addElement(node.getTextContent());
        }
    }
}
