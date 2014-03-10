package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JListbox;

import javax.swing.*;
import java.util.Arrays;

@Tag("ui-listbox")
public class UIListboxTag extends BaseTag<JListbox> {
    @Override
    public JListbox create(ElementItem element, UIReader uiReader) {
        return new JListbox();
    }

    @Override
    public void read(ElementItem element, JListbox component, Node node) {
    }

    @Override
    public void addUnknown(JListbox component, Node node) {
        if (node.getNodeName().equalsIgnoreCase("item")) {
            DefaultListModel model = (DefaultListModel) component.getModel();
            model.addElement(node.getTextContent());
            Node selected = node.getAttributes().getNamedItem("selected");
            if (selected != null && "1".equals(selected.getNodeValue())){
                int[] sel = component.getSelectedIndices();
                int[] newSel = Arrays.copyOf(sel, sel.length + 1);
                newSel[newSel.length - 1] = model.getSize() - 1;

                component.setSelectedIndices(newSel);
            }
        }
    }
}
