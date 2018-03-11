package org.develnext.jphp.ext.javafx.classes;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.util.Callback;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.JavaFxUtils;
import org.develnext.jphp.ext.javafx.support.UserData;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.support.ReflectionUtils;

@Reflection.Name(JavaFXExtension.NS + "UXTableColumn")
public class UXTableColumn extends BaseWrapper<TableColumnBase> {
    interface WrappedInterface {
        @Property boolean editable();
        @Property boolean resizable();
        @Property boolean sortable();
        @Property String text();
        @Property String style();
        @Property String id();
        @Property Node graphic();
        @Property boolean visible();
    }

    public UXTableColumn(Environment env, TableColumn wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTableColumn(Environment env, TableColumnBase wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTableColumn(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TableColumn();
        setCellValueFactoryForArrays();
    }

    @Signature
    public Memory data(String name) {
        return JavaFxUtils.data(getWrappedObject(), name);
    }

    @Signature
    public Memory data(Environment env, String name, Memory value) {
        return JavaFxUtils.data(env, getWrappedObject(), name, value);
    }

    @Setter
    public void setWidth(double width) {
        getWrappedObject().setPrefWidth(width);
    }

    @Getter
    public double getWidth() {
        return getWrappedObject().getWidth();
    }

    @Setter
    public void setMaxWidth(double width) {
        getWrappedObject().setMaxWidth(width);
    }

    @Getter
    public double getMaxWidth() {
        return getWrappedObject().getMaxWidth();
    }

    @Setter
    public void setMinWidth(double width) {
        getWrappedObject().setMinWidth(width);
    }

    @Getter
    public double getMinWidth() {
        return getWrappedObject().getMinWidth();
    }

    @Signature
    public void setCellValueFactoryForArrays() {
        ((TableColumn) getWrappedObject()).setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Object value = param.getValue();

                if (value instanceof Memory) {
                    Memory memory = (Memory) value;

                    memory = memory.valueOfIndex(getWrappedObject().getId());

                    if (memory.instanceOf(UXNode.class)) {
                        return new ReadOnlyObjectWrapper<Node>(memory.toObject(UXNode.class).getWrappedObject());
                    }

                    return new SimpleStringProperty(memory.toString());
                }

                return new SimpleStringProperty("");
            }
        });
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void setCellValueFactory(final Environment env, @Nullable final Invoker invoker) {
        if (invoker == null) {
            ((TableColumn) getWrappedObject()).setCellValueFactory(null);
            return;
        }

        ((TableColumn) getWrappedObject()).setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Memory result = invoker.callNoThrow(
                        Memory.wrap(env, param.getValue()),
                        ObjectMemory.valueOf(new UXTableColumn(env, param.getTableColumn()))
                );

                if (result.instanceOf(UXNode.class)) {
                    return new ReadOnlyObjectWrapper<Node>(result.toObject(UXNode.class).getWrappedObject());
                }

                return new SimpleStringProperty(result.toString());
            }
        });
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void setCellFactory(final Environment env, @Nullable final Invoker invoker) {
        if (invoker == null) {
            ((TableColumn) getWrappedObject()).setCellFactory(null);
            return;
        }

        ((TableColumn) getWrappedObject()).setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                Memory result = invoker.callNoThrow(
                        ObjectMemory.valueOf(new UXTableColumn(env, param))
                );

                if (!result.instanceOf(UXTableCell.class)) {
                    env.exception("The factory of tableColumn must return an instance of " + ReflectionUtils.getClassName(UXTableCell.class));
                    return null;
                }

                return result.toObject(UXTableCell.class).getWrappedObject();
            }
        });
    }
}
