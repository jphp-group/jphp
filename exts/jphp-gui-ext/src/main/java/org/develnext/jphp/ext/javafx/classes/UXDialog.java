package org.develnext.jphp.ext.javafx.classes;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.util.Optional;

@Name(JavaFXExtension.NS + "UXDialog")
public class UXDialog extends BaseObject {
    public UXDialog(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public static void show(final String text) {
        show(text, Alert.AlertType.INFORMATION);
    }

    @Signature
    public static void show(final String text, final Alert.AlertType type) {
        show(text, type, null);
    }

    @Signature
    public static void show(final String text, final Alert.AlertType type, @Nullable Window owner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showAndWait(text, type, owner);
            }
        });
    }

    @Signature
    public static String showAndWait(String text, Alert.AlertType type) {
        return showAndWait(text, type, null);
    }

    @Signature
    public static String showAndWait(String text, Alert.AlertType type, @Nullable Window owner) {
        Alert alert = new Alert(type);

        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.setResizable(false);
        alert.setContentText(text);
        alert.setHeaderText(null);

        ButtonType result = alert.showAndWait().orElse(null);
        return result == null ? null : result.getButtonData().getTypeCode();
    }

    @Signature
    public static String showAndWait(String text) {
        return showAndWait(text, Alert.AlertType.INFORMATION);
    }

    @Signature
    public static String showExpanded(String text, Node content, boolean expanded) {
        return showExpanded(text, content, expanded, Alert.AlertType.NONE);
    }

    @Signature
    public static String showExpanded(String text, Node content, boolean expanded, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        //alert.setContentText(text);
        alert.setHeaderText(text);

        alert.getDialogPane().setExpanded(expanded);
        alert.getDialogPane().setExpandableContent(content);

        ButtonType result = alert.showAndWait().orElse(null);
        return result == null ? null : result.getButtonData().getTypeCode();
    }

    @Signature
    public static boolean confirm(String question) {
        return confirm(question, null);
    }

    @Signature
    public static boolean confirm(String question, @Nullable Window owner) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.setResizable(false);
        alert.setContentText(question);
        alert.setHeaderText(null);

        ButtonType result = alert.showAndWait().orElse(null);

        return result != ButtonType.CANCEL;
    }

    @Signature
    public static String input(String text) {
        return input(text, "");
    }

    @Signature
    public static String input(String text, String defaultValue) {
        return input(text, defaultValue, null);
    }

    @Signature
    public static String input(String text, String defaultValue, Window owner) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);

        if (owner != null) {
            dialog.initOwner(owner);
        }

        dialog.setContentText(text);

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }
}
