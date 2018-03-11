package org.develnext.jphp.ext.javafx.classes;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXChoiceBox")
public class UXChoiceBox extends UXControl<ChoiceBox> {
    interface WrappedInterface {
        @Property ObservableList items();
        @Property Object value();
    }

    public UXChoiceBox(Environment env, ChoiceBox wrappedObject) {
        super(env, wrappedObject);
    }

    public UXChoiceBox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new WrapChoiceBox();
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

    @Signature
    @SuppressWarnings("unchecked")
    public void update() {
        ObservableList items = getWrappedObject().getItems();

        getWrappedObject().setItems(null);
        getWrappedObject().setItems(items);
    }

    public static class WrapChoiceBox extends ChoiceBox {
        protected ChangeListener changeListener;

        public WrapChoiceBox() {
            super();
        }

        public void setActionListener(ChangeListener listener) {
            if (this.changeListener != null) {
                this.getSelectionModel().selectedIndexProperty().removeListener(this.changeListener);
            }

            this.changeListener = listener;

            if (listener != null) {
                this.getSelectionModel().selectedIndexProperty().addListener(listener);
            }
        }

        public ChangeListener getChangeListener() {
            return changeListener;
        }
    }
}
