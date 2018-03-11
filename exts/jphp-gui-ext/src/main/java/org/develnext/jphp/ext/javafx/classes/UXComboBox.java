package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.ForeachIterator;
import php.runtime.reflection.ClassEntity;

import java.util.Scanner;

@Name(JavaFXExtension.NS + "UXComboBox")
public class UXComboBox extends UXComboBoxBase {
    interface WrappedInterface {
        @Property ObservableList items();
        @Property int visibleRowCount();
        @Property TextField editor();
    }

    public UXComboBox(Environment env, ComboBox wrappedObject) {
        super(env, wrappedObject);
    }

    public UXComboBox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ComboBox getWrappedObject() {
        return (ComboBox) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ComboBox();
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void __construct(Environment env, ForeachIterator iterator) {
        __construct();

        while (iterator.next()) {
            getWrappedObject().getItems().add(Memory.unwrap(env, iterator.getValue()));
        }
    }

    @Getter
    public Object getSelected() {
        SingleSelectionModel selectionModel = getWrappedObject().getSelectionModel();
        return selectionModel.getSelectedItem();
    }

    @Setter
    public void setSelected(Object value) {
        getWrappedObject().getSelectionModel().select(value);
    }

    @Getter
    public int getSelectedIndex() {
        SingleSelectionModel selectionModel = getWrappedObject().getSelectionModel();
        return selectionModel.getSelectedIndex();
    }

    @Setter
    public void setSelectedIndex(int value) {
        getWrappedObject().getSelectionModel().select(value);
    }

    @Getter
    public String getText() {
        if (getWrappedObject().getEditor() == null) {
            Object value = getWrappedObject().getValue();
            return value == null ? null : value.toString();
        }

        return getWrappedObject().getEditor().getText();
    }

    @Setter
    public void setText(String value) {
        if (getWrappedObject().getEditor() == null) {
            throw new IllegalStateException("Property 'text' is read only");
        }

        getWrappedObject().getEditor().setText(value);
    }

    @Getter
    protected String getItemsText() {
        return StringUtils.join(getWrappedObject().getItems(), "\n");
    }

    @Setter
    protected void setItemsText(String value) {
        Scanner scanner = new Scanner(value);

        getWrappedObject().getItems().clear();

        while (scanner.hasNextLine()) {
            getWrappedObject().getItems().add(scanner.nextLine());
        }
    }

    @Signature
    public void onButtonRender(final Environment env, @Nullable final Invoker handler) {
        getWrappedObject().setButtonCell(new ListCell() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (handler != null) {
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        handler.callAny(new UXListCell(env, this), item);
                    }
                }
            }
        });
    }

    @Signature
    public void onCellRender(final Environment env, @Nullable final Invoker handler) {
        getWrappedObject().setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ListCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);

                        if (handler != null) {
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                handler.callAny(new UXListCell(env, this), item, empty);
                            }
                        }
                    }
                };
            }
        });
    }
}
