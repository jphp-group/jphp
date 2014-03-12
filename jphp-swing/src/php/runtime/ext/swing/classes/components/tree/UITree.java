package php.runtime.ext.swing.classes.components.tree;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.UILabel;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.ext.swing.classes.components.support.UIElement;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UITree")
public class UITree extends UIContainer {
    protected JTree component;

    public UITree(Environment env, JTree component) {
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
        this.component = (JTree) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        component = new JTree(new DefaultTreeModel(root));
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
        return TrueMemory.valueOf(component.getDragEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setDragEnabled(Environment env, Memory... args) {
        component.setDragEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getExpandsSelectedPaths(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getExpandsSelectedPaths());
    }

    @Signature(@Arg("value"))
    protected Memory __setExpandsSelectedPaths(Environment env, Memory... args) {
        component.setExpandsSelectedPaths(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getInvokesStopCellEditing(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getInvokesStopCellEditing());
    }

    @Signature(@Arg("value"))
    protected Memory __setInvokesStopCellEditing(Environment env, Memory... args) {
        component.setInvokesStopCellEditing(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getEditable(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isEditable());
    }

    @Signature(@Arg("value"))
    protected Memory __setEditable(Environment env, Memory... args) {
        component.setEditable(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __isRootVisible(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isRootVisible());
    }

    @Signature(@Arg("value"))
    protected Memory __setRootVisible(Environment env, Memory... args) {
        component.setRootVisible(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVisibleRowCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getVisibleRowCount());
    }

    @Signature(@Arg("value"))
    protected Memory __setVisibleRowCount(Environment env, Memory... args) {
        component.setVisibleRowCount(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRowHeight(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getRowHeight());
    }

    @Signature(@Arg("value"))
    protected Memory __setRowHeight(Environment env, Memory... args) {
        component.setRowHeight(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getScrollsOnExpand(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getScrollsOnExpand());
    }

    @Signature(@Arg("value"))
    protected Memory __setScrollsOnExpand(Environment env, Memory... args) {
        component.setScrollsOnExpand(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMaxSelectionRow(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getMaxSelectionRow());
    }

    @Signature
    protected Memory __getMinSelectionRow(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getMinSelectionRow());
    }

    @Signature
    protected Memory __getRowCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getRowCount());
    }

    @Signature
    protected Memory __getSelectionCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getSelectionCount());
    }

    @Signature
    protected Memory __getLeadSelectionRow(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getLeadSelectionRow());
    }

    @Signature
    protected Memory __getSelectionRows(Environment env, Memory... args) {
        return new ArrayMemory(component.getSelectionRows()).toConstant();
    }

    @Signature(@Arg(value = "value", type = HintType.ARRAY))
    protected Memory __setSelectionRows(Environment env, Memory... args) {
        component.setSelectionRows(args[0].toValue(ArrayMemory.class).toIntArray());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedNode(Environment env, Memory... args) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) component.getLastSelectedPathComponent();
        if (node == null)
            return Memory.NULL;
        return new ObjectMemory(new WrapTreeNode(env, node));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME, optional = @Optional("NULL")))
    protected Memory __setSelectedNode(Environment env, Memory... args) {
        if (args[0].isNull())
            component.setSelectionPath(null);
        else {
            DefaultMutableTreeNode node = args[0].toObject(WrapTreeNode.class).getNode();
            component.setSelectionPath(new TreePath(node));
        }
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedNodes(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        TreePath[] paths = component.getSelectionPaths();
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
            component.setSelectionPaths(null);
        else {
            ArrayMemory arr = args[0].toValue(ArrayMemory.class);
            TreePath[] paths = new TreePath[arr.size()];

            int i = 0;
            for(WrapTreeNode e : arr.toObjectArray(WrapTreeNode.class)) {
                paths[i] = new TreePath(e.getNode());
            }

            component.setSelectionPaths(paths);
        }
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory expandNode(Environment env, Memory... args) {
        component.expandPath(new TreePath(args[0].toObject(WrapTreeNode.class).getNode()));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory collapseNode(Environment env, Memory... args) {
        component.collapsePath(new TreePath(args[0].toObject(WrapTreeNode.class).getNode()));
        return Memory.NULL;
    }

    private CustomTreeCellRenderer getRenderer(){
        return (CustomTreeCellRenderer) component.getCellRenderer();
    }

    @Signature(@Arg(value = "renderer", type = HintType.CALLABLE, optional = @Optional("NULL")))
    public Memory onCellRender(Environment env, Memory... args) {
        if (args[0].isNull())
            getRenderer().setOnRender(null);
        else
            getRenderer().setOnRender(Invoker.valueOf(env, null, args[0]));

        return Memory.NULL;
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
