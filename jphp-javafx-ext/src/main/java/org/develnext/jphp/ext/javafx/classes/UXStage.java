package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXStage")
public class UXStage extends UXWindow {
    interface WrappedInterface {
        @Property String title();

        @Property double maxHeight();
        @Property double maxWidth();
        @Property double minHeight();
        @Property double minWidth();

        @Property Modality modality();

        @Property Window owner();

        @Property StageStyle style();

        @Property boolean fullScreen();
        @Property boolean iconified();
        @Property boolean resizable();

        void show();
        void showAndWait();

        void toBack();
        void toFront();
    }

    public UXStage(Environment env, Stage wrappedObject) {
        super(env, wrappedObject);
    }

    public UXStage(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Stage getWrappedObject() {
        return (Stage) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Stage();

        AnchorPane layout = new AnchorPane();
        Scene scene = new Scene(layout);

        getWrappedObject().setScene(scene);
    }

    @Signature
    public void __construct(StageStyle style) {
        __wrappedObject = new Stage(style);
    }

    @Setter
    protected void setScene(@Nullable Scene scene) {
        getWrappedObject().setScene(scene);
    }

    @Setter
    protected void setOwner(@Nullable Window window) {
        getWrappedObject().initOwner(window);
    }

    @Setter
    protected void setStyle(StageStyle style) {
        getWrappedObject().initStyle(style);
    }

    @Setter
    protected void setModality(Modality modality) {
        getWrappedObject().initModality(modality);
    }
}
