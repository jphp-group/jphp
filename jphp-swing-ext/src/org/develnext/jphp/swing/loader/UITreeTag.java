package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.develnext.jphp.swing.classes.components.tree.UITree;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.loader.support.Value;
import org.develnext.jphp.swing.support.JTreeX;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

@Tag("ui-tree")
public class UITreeTag extends BaseTag<JTreeX> {
    @Override
    public JTreeX create(ElementItem element, UIReader uiReader) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        JTreeX treeX = new JTreeX();
        treeX.setModel(new DefaultTreeModel(root));
        return treeX;
    }

    protected static void appendChildNodes(JTreeX tree, DefaultMutableTreeNode parent, Node node) {
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();

        for(int i = 0; i < length; i++) {
            Node el = childNodes.item(i);
            if ("item".equals(el.getNodeName())){
                ElementItem item = new ElementItem(el);
                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(item.getAttr("text"));
                if (item.getAttr("allows-children") != null)
                    treeNode.setAllowsChildren(new Value(item.getAttr("allows-children")).asBoolean());

                parent.add(treeNode);
                appendChildNodes(tree, treeNode, el);
            }
        }
    }

    @Override
    public void read(ElementItem element, JTreeX component, Node node) {
        component.setCellRenderer(new UITree.CustomTreeCellRenderer());
    }

    @Override
    public void afterRead(ElementItem element, JTreeX component, Node node) {
        appendChildNodes(component, (DefaultMutableTreeNode)component.getModel().getRoot(), node);
        if (element.getAttr("expanded") != null && new Value(element.getAttr("expanded")).asBoolean())
            component.getContent().expandPath(new TreePath(component.getModel().getRoot()));
    }

    @Override
    public boolean isAllowsChildren() {
        return false;
    }
}
