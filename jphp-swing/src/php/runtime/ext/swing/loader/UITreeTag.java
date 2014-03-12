package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.classes.components.tree.UITree;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@Tag("ui-tree")
public class UITreeTag extends BaseTag<JTree> {
    @Override
    public JTree create(ElementItem element, UIReader uiReader) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        return new JTree(new DefaultTreeModel(root));
    }

    @Override
    public void read(ElementItem element, JTree component, Node node) {
        component.setCellRenderer(new UITree.CustomTreeCellRenderer());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addUnknown(JTree component, Node node) {

    }
}
