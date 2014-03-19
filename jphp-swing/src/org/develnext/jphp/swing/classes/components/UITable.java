package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.WrapColor;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.support.JTableX;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UITable")
public class UITable extends UIContainer {
    protected JTableX component;

    public UITable(Environment env, JTableX component) {
        super(env);
        this.component = component;
    }

    public UITable(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JTableX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JTableX();
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
    protected Memory __getSelectionBackground(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, component.getContent().getSelectionForeground()));
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionBackground(Environment evn, Memory... args) {
        component.getContent().setSelectionBackground(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getGridColor(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, component.getContent().getGridColor()));
    }

    @Signature(@Arg("value"))
    protected Memory __setGridColor(Environment evn, Memory... args) {
        component.getContent().setGridColor(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectionForeground(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, component.getContent().getSelectionForeground()));
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionForeground(Environment evn, Memory... args) {
        component.getContent().setSelectionForeground(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRowMargin(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getRowMargin());
    }

    @Signature(@Arg("value"))
    protected Memory __setRowMargin(Environment evn, Memory... args) {
        component.getContent().setRowMargin(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getEditingColumn(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getEditingColumn());
    }

    @Signature(@Arg("value"))
    protected Memory __setEditingColumn(Environment evn, Memory... args) {
        component.getContent().setEditingColumn(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getEditingRow(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getEditingRow());
    }

    @Signature(@Arg("value"))
    protected Memory __setEditingRow(Environment evn, Memory... args) {
        component.getContent().setEditingRow(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("height"), @Arg(value = "row", optional = @Optional("NULL"))})
    public Memory setRowHeight(Environment evn, Memory... args) {
        if (args[1].isNull())
            component.getContent().setRowHeight(args[0].toInteger());
        else
            component.getContent().setRowHeight(args[1].toInteger(), args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg(value = "row", optional = @Optional("NULL"))})
    public Memory getRowHeight(Environment evn, Memory... args) {
        if (args[0].isNull())
            return LongMemory.valueOf(component.getContent().getRowHeight());
        else
            return LongMemory.valueOf(component.getContent().getRowHeight(args[0].toInteger()));
    }

    @Signature({@Arg("value"), @Arg("row"), @Arg("column")})
    public Memory setValueAt(Environment evn, Memory... args) {
        component.getContent().setValueAt(
                args[0].isNull() ? null : args[0].toString(), args[1].toInteger(), args[2].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("row"), @Arg("column")})
    public Memory getValueAt(Environment evn, Memory... args) {
        Object o = component.getContent().getValueAt(args[0].toInteger(), args[1].toInteger());
        if (o == null)
            return Memory.NULL;
        return new StringMemory(o.toString());
    }

    @Signature({@Arg("index0"), @Arg("index1")})
    public Memory addColumnSelectionInterval(Environment evn, Memory... args) {
        component.getContent().addColumnSelectionInterval(args[0].toInteger(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("index0"), @Arg("index1")})
    public Memory addRowSelectionInterval(Environment evn, Memory... args) {
        component.getContent().addRowSelectionInterval(args[0].toInteger(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y")})
    public Memory columnAtPoint(Environment evn, Memory... args) {
        return LongMemory.valueOf(
                component.getContent().columnAtPoint(new Point(args[0].toInteger(), args[1].toInteger()))
        );
    }

    @Signature({@Arg("x"), @Arg("y")})
    public Memory rowAtPoint(Environment evn, Memory... args) {
        return LongMemory.valueOf(
                component.getContent().rowAtPoint(new Point(args[0].toInteger(), args[1].toInteger()))
        );
    }

    @Signature({@Arg("row"), @Arg("column")})
    public Memory editCellAt(Environment evn, Memory... args) {
        return TrueMemory.valueOf(
                component.getContent().editCellAt(args[0].toInteger(), args[1].toInteger())
        );
    }

    @Signature(@Arg("column"))
    public Memory getColumnName(Environment evn, Memory... args) {
        return StringMemory.valueOf(
                component.getContent().getColumnName(args[0].toInteger())
        );
    }

    @Signature(@Arg("column"))
    public Memory setEditingColumn(Environment evn, Memory... args) {
        component.getContent().setEditingColumn(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("row"))
    public Memory setEditingRow(Environment evn, Memory... args) {
        component.getContent().setEditingRow(args[0].toInteger());
        return Memory.NULL;
    }
}
