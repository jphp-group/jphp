package php.runtime.ext.swing.classes.components.tree;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.UILabel;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.ext.swing.classes.components.support.UIElement;
import php.runtime.ext.swing.support.JTreeX;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UITree")
public class UITree extends UIContainer {
    protected JTreeX component;

    public UITree(Environment env, JTreeX component) {
        super(env);
        this.component = component;
    }

    public UITree(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JTreeX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        component = new JTreeX();
        component.setModel(new DefaultTreeModel(root));
        component.setCellRenderer(new CustomTreeCellRenderer());
    }

    @Signature
    protected Memory __getModel(Environment env, Memory... args) {
        return new ObjectMemory(new WrapTreeModel(env, (DefaultTreeModel)component.getModel()));
    }

    @Signature(@Arg(value = "model", typeClass = WrapTreeModel.NAME))
    protected Memory __setModel(Environment env, Memory... args) {
        component.setModel(args[0].toObject(WrapTreeModel.class).getModel());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getDragEnabled(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().getDragEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setDragEnabled(Environment env, Memory... args) {
        component.getContent().setDragEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRoot(Environment env, Memory... args) {
        if (component.getModel() == null)
            return Memory.NULL;
        if (component.getModel().getRoot() == null)
            return Memory.NULL;
        return new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode) component.getModel().getRoot()));
    }

    @Signature(@Arg(value = "value", typeClass = WrapTreeNode.NAME, optional = @Optional("NULL")))
    protected Memory __setRoot(Environment env, Memory... args) {
        DefaultTreeModel model = (DefaultTreeModel) component.getModel();

        if (args[0].isNull())
            model.setRoot(null);
        else
            model.setRoot(args[0].toObject(WrapTreeNode.class).getNode());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getExpandsSelectedPaths(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().getExpandsSelectedPaths());
    }

    @Signature(@Arg("value"))
    protected Memory __setExpandsSelectedPaths(Environment env, Memory... args) {
        component.getContent().setExpandsSelectedPaths(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getInvokesStopCellEditing(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().getInvokesStopCellEditing());
    }

    @Signature(@Arg("value"))
    protected Memory __setInvokesStopCellEditing(Environment env, Memory... args) {
        component.getContent().setInvokesStopCellEditing(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getEditable(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().isEditable());
    }

    @Signature(@Arg("value"))
    protected Memory __setEditable(Environment env, Memory... args) {
        component.getContent().setEditable(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __isRootVisible(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().isRootVisible());
    }

    @Signature(@Arg("value"))
    protected Memory __setRootVisible(Environment env, Memory... args) {
        component.getContent().setRootVisible(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVisibleRowCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getVisibleRowCount());
    }

    @Signature(@Arg("value"))
    protected Memory __setVisibleRowCount(Environment env, Memory... args) {
        component.getContent().setVisibleRowCount(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRowHeight(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getRowHeight());
    }

    @Signature(@Arg("value"))
    protected Memory __setRowHeight(Environment env, Memory... args) {
        component.getContent().setRowHeight(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getScrollsOnExpand(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().getScrollsOnExpand());
    }

    @Signature(@Arg("value"))
    protected Memory __setScrollsOnExpand(Environment env, Memory... args) {
        component.getContent().setScrollsOnExpand(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMaxSelectionRow(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getMaxSelectionRow());
    }

    @Signature
    protected Memory __getMinSelectionRow(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getMinSelectionRow());
    }

    @Signature
    protected Memory __getRowCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getRowCount());
    }

    @Signature
    protected Memory __getSelectionCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getSelectionCount());
    }

    @Signature
    protected Memory __getLeadSelectionRow(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getLeadSelectionRow());
    }

    @Signature
    protected Memory __getSelectionRows(Environment env, Memory... args) {
        return new ArrayMemory(component.getContent().getSelectionRows()).toConstant();
    }

    @Signature(@Arg(value = "value", type = HintType.ARRAY))
    protected Memory __setSelectionRows(Environment env, Memory... args) {
        component.getContent().setSelectionRows(args[0].toValue(ArrayMemory.class).toIntArray());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedNode(Environment env, Memory... args) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) component.getContent().getLastSelectedPathComponent();
        if (node == null)
            return Memory.NULL;
        return new ObjectMemory(new WrapTreeNode(env, node));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME, optional = @Optional("NULL")))
    protected Memory __setSelectedNode(Environment env, Memory... args) {
        if (args[0].isNull())
            component.getContent().setSelectionPath(null);
        else {
            DefaultMutableTreeNode node = args[0].toObject(WrapTreeNode.class).getNode();
            component.getContent().setSelectionPath(new TreePath(node));
        }
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedNodes(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        TreePath[] paths = component.getContent().getSelectionPaths();
        if (paths == null)
            return r.toConstant();

        for(TreePath p : paths) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
            r.add(new ObjectMemory(new WrapTreeNode(env, node)));
        }

        return r.toArray();
    }

    @Signature(@Arg(value = "node", type = HintType.ARRAY, optional = @Optional("NULL")))
    protected Memory __setSelectedNodes(Environment env, Memory... args) {
        if (args[0].isNull())
            component.getContent().setSelectionPaths(null);
        else {
            ArrayMemory arr = args[0].toValue(ArrayMemory.class);
            TreePath[] paths = new TreePath[arr.size()];

            int i = 0;
            for(WrapTreeNode e : arr.toObjectArray(WrapTreeNode.class)) {
                paths[i] = new TreePath(e.getNode());
            }

            component.getContent().setSelectionPaths(paths);
        }
        return Memory.NULL;
    }

    public static TreePath getPath(TreeNode node) {
        java.util.List<TreeNode> nodes = new ArrayList<TreeNode>();
        nodes.add(node);
        while ((node = node.getParent()) != null) {
            nodes.add(0, node);
        }

        TreePath path = new TreePath(nodes.toArray());
        return path;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory expandNode(Environment env, Memory... args) {
        DefaultMutableTreeNode node = args[0].toObject(WrapTreeNode.class).getNode();
        component.getContent().expandPath(getPath(node));
        return Memory.NULL;
    }

    @Signature(@Arg("row"))
    public Memory expandRow(Environment env, Memory... args) {
        component.getContent().expandRow(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory collapseNode(Environment env, Memory... args) {
        component.getContent().collapsePath(getPath(args[0].toObject(WrapTreeNode.class).getNode()));
        return Memory.NULL;
    }

    @Signature(@Arg("row"))
    public Memory collapseRow(Environment env, Memory... args) {
        component.getContent().collapseRow(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory expandNodeAll(Environment env, Memory... args) {
        setTreeState(component.getContent(), new TreePath(args[0].toObject(WrapTreeNode.class).getNode()), true);
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory collapseNodeAll(Environment env, Memory... args) {
        setTreeState(component.getContent(), new TreePath(args[0].toObject(WrapTreeNode.class).getNode()), false);
        return Memory.NULL;
    }

    @Signature(@Arg("row"))
    public Memory isExpandedRow(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().isExpanded(args[0].toInteger()));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isExpandedNode(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().isExpanded(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        )));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory hasBeenExpanded(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().hasBeenExpanded(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        )));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isNodeSelected(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().isPathSelected(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        )));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isVisible(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().isVisible(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        )));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isNodeEditable(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().isPathEditable(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        )));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory fireTreeExpanded(Environment env, Memory... args) {
        component.getContent().fireTreeExpanded(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        ));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory fireTreeCollapsed(Environment env, Memory... args) {
        component.getContent().fireTreeCollapsed(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        ));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory fireTreeWillExpand(Environment env, Memory... args) throws ExpandVetoException {
        component.getContent().fireTreeWillExpand(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        ));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory fireTreeWillCollapse(Environment env, Memory... args) throws ExpandVetoException {
        component.getContent().fireTreeWillCollapse(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        ));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory addSelectionNode(Environment env, Memory... args) {
        component.getContent().addSelectionPath(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        ));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory removeSelectionNode(Environment env, Memory... args) {
        component.getContent().removeSelectionPath(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        ));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory makeVisible(Environment env, Memory... args) {
        component.getContent().makeVisible(getPath(
                args[0].toObject(WrapTreeNode.class).getNode()
        ));
        return Memory.NULL;
    }

    @Signature
    public Memory cancelEditing(Environment env, Memory... args) {
        component.getContent().cancelEditing();
        return Memory.NULL;
    }

    @Signature
    public Memory clearSelection(Environment env, Memory... args) {
        component.getContent().clearSelection();
        return Memory.NULL;
    }

    @Signature
    protected Memory __getEditingNode(Environment env, Memory... args) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)component.getContent().getEditingPath()
                .getLastPathComponent();
        if (node == null)
            return Memory.NULL;

        return new ObjectMemory(new WrapTreeNode(env, node));
    }

    private CustomTreeCellRenderer getRenderer(){
        return (CustomTreeCellRenderer) component.getContent().getCellRenderer();
    }

    @Signature(@Arg(value = "renderer", type = HintType.CALLABLE, optional = @Optional("NULL")))
    public Memory onCellRender(Environment env, Memory... args) {
        if (args[0].isNull())
            getRenderer().setOnRender(null);
        else
            getRenderer().setOnRender(Invoker.valueOf(env, null, args[0]));

        return Memory.NULL;
    }

    public static void setTreeState(JTree tree, TreePath path, boolean expanded) {
        Object lastNode = path.getLastPathComponent();
        for (int i = 0; i < tree.getModel().getChildCount(lastNode); i++) {
            Object child = tree.getModel().getChild(lastNode,i);
            TreePath pathToChild = path.pathByAddingChild(child);
            setTreeState(tree,pathToChild,expanded);
        }

        if (expanded)
            tree.expandPath(path);
        else
            tree.collapsePath(path);
    }

    public static class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        protected Invoker onRender;

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (onRender != null) {
                Environment env = onRender.getEnvironment();
                Memory r = onRender.callNoThrow(
                        new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode)value)),
                        new ObjectMemory(new UILabel(env, (JLabel)c)),
                        sel ? Memory.TRUE : Memory.FALSE,
                        expanded ? Memory.TRUE : Memory.FALSE,
                        leaf ? Memory.TRUE : Memory.FALSE,
                        LongMemory.valueOf(row),
                        hasFocus ? Memory.TRUE : Memory.FALSE
                );
                if (r.isObject() && r.instanceOf(UIElement.class)) {
                    return r.toObject(UIElement.class).getComponent();
                }
            }
            return c;
        }

        public Invoker getOnRender() {
            return onRender;
        }

        public void setOnRender(Invoker onRender) {
            this.onRender = onRender;
        }
    }
}
