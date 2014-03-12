package php.runtime.ext.swing.classes.components.tree;


import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaObject;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import java.util.NoSuchElementException;

import static php.runtime.annotation.Reflection.*;

@Name(WrapTreeNode.NAME)
public class WrapTreeNode extends RootObject {
    static final String NAME = SwingExtension.NAMESPACE + "tree\\TreeNode";

    protected DefaultMutableTreeNode node;

    public WrapTreeNode(Environment env, DefaultMutableTreeNode node) {
        super(env);
        this.node = node;
    }

    public WrapTreeNode(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public DefaultMutableTreeNode getNode() {
        return node;
    }

    @Signature({
            @Arg(value = "object", optional = @Optional("NULL")),
            @Arg(value = "allowsChildren", optional = @Optional(value = "1", type = HintType.BOOLEAN))
    })
    public Memory __construct(Environment env, Memory... args) {
        node = new DefaultMutableTreeNode(args[0].isNull() ? null : args[0].toString(), args[1].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getDepth(Environment env, Memory... args) {
        return LongMemory.valueOf(node.getDepth());
    }

    @Signature
    protected Memory __getLevel(Environment env, Memory... args) {
        return LongMemory.valueOf(node.getLevel());
    }

    @Signature
    protected Memory __getAllowsChildren(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.getAllowsChildren());
    }

    @Signature(@Arg("value"))
    protected Memory __setAllowsChildren(Environment env, Memory... args) {
        node.setAllowsChildren(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getParent(Environment env, Memory... args) {
        if (node.getParent() == null)
            return Memory.NULL;
        return new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode) node.getParent()));
    }

    @Signature(@Arg(value = "value", typeClass = NAME, optional = @Optional("NULL")))
    protected Memory __setParent(Environment env, Memory... args) {
        if (args[0].isNull())
            node.setParent(null);
        else
            node.setParent(args[0].toObject(WrapTreeNode.class).getNode());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory __setUserData(Environment env, Memory... args) {
        if (args[0].isNull())
            node.setUserObject(null);
        else
            node.setUserObject(args[0].toValue());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getUserData(Environment env, Memory... args) {
        if (node.getUserObject() == null)
            return Memory.NULL;
        if (node.getUserObject() instanceof Memory)
            return (Memory) node.getUserObject();
        return new ObjectMemory(JavaObject.of(env, node.getUserObject()));
    }

    @Signature
    public Memory isRoot(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.isRoot());
    }

    @Signature
    public Memory isLeaf(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.isLeaf());
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isNodeChild(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.isNodeChild(args[0].toObject(WrapTreeNode.class).getNode()));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isNodeAncestor(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.isNodeAncestor(args[0].toObject(WrapTreeNode.class).getNode()));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isNodeDescendant(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.isNodeDescendant(args[0].toObject(WrapTreeNode.class).getNode()));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isNodeRelated(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.isNodeRelated(args[0].toObject(WrapTreeNode.class).getNode()));
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory isNodeSibling(Environment env, Memory... args) {
        return TrueMemory.valueOf(node.isNodeSibling(args[0].toObject(WrapTreeNode.class).getNode()));
    }

    protected static Memory wrap(Environment env, TreeNode node) {
        try {
            if (node == null)
                return Memory.NULL;
            return new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode)node));
        } catch (NoSuchElementException e){
            return Memory.NULL;
        }
    }

    @Signature
    public Memory getRoot(Environment env, Memory... args) {
        return wrap(env, node.getRoot());
    }

    @Signature
    public Memory getNextNode(Environment env, Memory... args) {
        return wrap(env, node.getNextNode());
    }

    @Signature
    public Memory getNextLeaf(Environment env, Memory... args) {
        return wrap(env, node.getNextLeaf());
    }

    @Signature
    public Memory getNextSibling(Environment env, Memory... args) {
        return wrap(env, node.getNextSibling());
    }

    @Signature
    public Memory getPreviousNode(Environment env, Memory... args) {
        return wrap(env, node.getPreviousNode());
    }

    @Signature
    public Memory getPreviousLeaf(Environment env, Memory... args) {
        return wrap(env, node.getPreviousLeaf());
    }

    @Signature
    public Memory getPreviousSibling(Environment env, Memory... args) {
        return wrap(env, node.getPreviousSibling());
    }

    @Signature
    public Memory getFirstChild(Environment env, Memory... args) {
        return wrap(env, node.getFirstChild());
    }

    @Signature
    public Memory getFirstLeaf(Environment env, Memory... args) {
        return wrap(env, node.getFirstLeaf());
    }

    @Signature
    public Memory getLastChild(Environment env, Memory... args) {
        return wrap(env, node.getLastChild());
    }

    @Signature
    public Memory getLastLeaf(Environment env, Memory... args) {
        return wrap(env, node.getLastLeaf());
    }

    @Signature(@Arg(value = "value", typeClass = NAME))
    public Memory add(Environment env, Memory... args) {
        node.add(args[0].toObject(WrapTreeNode.class).getNode());
        return Memory.NULL;
    }

    @Signature({@Arg("childIndex"), @Arg(value = "value", typeClass = NAME)})
    public Memory insert(Environment env, Memory... args) {
        node.insert(args[1].toObject(WrapTreeNode.class).getNode(), args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg(value = "child", typeClass = NAME), @Arg(value = "node", typeClass = NAME)})
    public Memory insertAfter(Environment env, Memory... args) {
        DefaultMutableTreeNode child = args[0].toObject(WrapTreeNode.class).getNode();
        DefaultMutableTreeNode node  = args[1].toObject(WrapTreeNode.class).getNode();

        int childIndex = node.getIndex(child);
        if (childIndex <= 0 || childIndex == this.node.getChildCount() - 1)
            this.node.add(node);
        else {
            this.node.insert(node, childIndex + 1);
        }

        return Memory.NULL;
    }

    @Signature({@Arg(value = "child", typeClass = NAME), @Arg(value = "node", typeClass = NAME)})
    public Memory insertBefore(Environment env, Memory... args) {
        DefaultMutableTreeNode child = args[0].toObject(WrapTreeNode.class).getNode();
        DefaultMutableTreeNode node  = args[1].toObject(WrapTreeNode.class).getNode();

        int childIndex = node.getIndex(child);
        if (childIndex < 0)
            this.node.add(node);
        else {
            this.node.insert(node, childIndex);
        }

        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", typeClass = NAME))
    public Memory remove(Environment env, Memory... args) {
        node.remove(args[0].toObject(WrapTreeNode.class).getNode());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    public Memory removeByIndex(Environment env, Memory... args) {
        node.remove(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory removeAllChildren(Environment env, Memory... args) {
        node.removeAllChildren();
        return Memory.NULL;
    }

    @Signature
    public Memory removeFromParent(Environment env, Memory... args) {
        node.removeFromParent();
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", typeClass = NAME))
    public Memory getIndex(Environment env, Memory... args) {
        return LongMemory.valueOf(node.getIndex(args[0].toObject(WrapTreeNode.class).getNode()));
    }

    @Signature
    public Memory duplicate(Environment env, Memory... args) {
        return new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode) node.clone()));
    }
}
