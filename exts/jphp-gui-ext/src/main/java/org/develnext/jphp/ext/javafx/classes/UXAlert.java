package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXAlert")
public class UXAlert extends BaseWrapper<Alert> {
    interface WrappedInterface {
    }

    public UXAlert(Environment env, Alert wrappedObject) {
        super(env, wrappedObject);
    }

    public UXAlert(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Alert.AlertType type) {
        __wrappedObject = new Alert(type);
    }

    @Signature
    public void setButtonTypes(String... types) {
        getWrappedObject().getButtonTypes().clear();

        for (String type : types) {
            getWrappedObject().getButtonTypes().add(new ButtonType(type));
        }
    }

    @Getter
    public boolean getExpanded() {
        return getWrappedObject().getDialogPane().isExpanded();
    }

    @Setter
    public void setExpanded(boolean value) {
        getWrappedObject().getDialogPane().setExpanded(value);
    }

    @Setter
    public void setExpandableContent(@Nullable Node node) {
        getWrappedObject().getDialogPane().setExpandableContent(node);
    }

    @Getter
    public Node getExpandableContent() {
        return getWrappedObject().getDialogPane().getExpandableContent();
    }

    @Setter
    public void setGraphic(@Nullable Node node) {
        getWrappedObject().getDialogPane().setGraphic(node);
    }

    @Getter
    public Node getGraphic() {
        return getWrappedObject().getDialogPane().getGraphic();
    }

    @Setter
    public void setContentText(String text) {
        getWrappedObject().setContentText(text);
    }

    @Getter
    public String getContentText() {
        return getWrappedObject().getContentText();
    }

    @Setter
    public void setHeaderText(String text) {
        getWrappedObject().setHeaderText(text);
    }

    @Getter
    public String getHeaderText() {
        return getWrappedObject().getHeaderText();
    }

    @Setter
    public void setTitle(String text) {
        getWrappedObject().setTitle(text);
    }

    @Getter
    public String getTitle() {
        return getWrappedObject().getTitle();
    }

    @Signature
    public void show() {
        getWrappedObject().show();
    }

    @Signature
    public void hide() {
        getWrappedObject().hide();
    }

    @Signature
    public Memory showAndWait() {
        ButtonType get = getWrappedObject().showAndWait().orElse(null);

        return get == null ? Memory.NULL : StringMemory.valueOf(get.getText());
    }
}
