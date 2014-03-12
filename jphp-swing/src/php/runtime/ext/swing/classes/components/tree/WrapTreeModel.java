package php.runtime.ext.swing.classes.components.tree;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import static php.runtime.annotation.Reflection.*;

@Name(WrapTreeModel.NAME)
public class WrapTreeModel extends RootObject {
    static final String NAME = SwingExtension.NAMESPACE + "tree\\TreeModel";

    protected DefaultTreeModel model;

    public WrapTreeModel(Environment env, DefaultTreeModel model) {
        super(env);
        this.model = model;
    }

    public WrapTreeModel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public DefaultTreeModel getModel() {
        return model;
    }

    @Signature({
            @Arg(value = "root", typeClass = WrapTreeNode.NAME),
            @Arg(value = "asksAllowsChildren", optional = @Optional(value = "", type = HintType.BOOLEAN))
    })
    public Memory __construct(Environment env, Memory... args) {
        model = new DefaultTreeModel(args[0].toObject(WrapTreeNode.class).getNode(), args[1].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRoot(Environment env, Memory... args) {
        if (model.getRoot() == null)
            return Memory.NULL;
        return new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode) model.getRoot()));
    }

    @Signature(@Arg(value = "value", typeClass = NAME, optional = @Optional("NULL")))
    protected Memory __setRoot(Environment env, Memory... args) {
        if (args[0].isNull())
            model.setRoot(null);
        else
            model.setRoot(args[0].toObject(WrapTreeNode.class).getNode());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory nodeChanged(Environment env, Memory... args) {
        model.nodeChanged(args[0].toObject(WrapTreeNode.class).getNode());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME))
    public Memory nodeStructureChanged(Environment env, Memory... args) {
        model.nodeStructureChanged(args[0].toObject(WrapTreeNode.class).getNode());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "node", typeClass = WrapTreeNode.NAME, optional = @Optional("NULL")))
    public Memory reload(Environment env, Memory... args) {
        if (args[0].isNull())
            model.reload();
        else
            model.reload(args[0].toObject(WrapTreeNode.class).getNode());

        return Memory.NULL;
    }
}
