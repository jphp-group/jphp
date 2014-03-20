package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JListbox;

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
