package org.develnext.jphp.swing.support;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

public class JTreeX extends JScrollableComponent<JTree> {
    @Override
    protected JTree newComponent() {
        return new JTree();
    }

    public void setModel(TreeModel model) {
        component.setModel(model);
    }

    public TreeModel getModel(){
        return component.getModel();
    }

    public void setCellRenderer(TreeCellRenderer renderer) {
        component.setCellRenderer(renderer);
    }
}
