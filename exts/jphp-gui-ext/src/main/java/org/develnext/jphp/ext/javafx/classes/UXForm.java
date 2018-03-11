package org.develnext.jphp.ext.javafx.classes;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.*;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXForm")
public class UXForm extends UXWindow {
    private EventHandler<WindowEvent> fixResizeBugEventFilter = windowEvent -> fixResizeBug();

    interface WrappedInterface {
        @Property String title();

        @Property double maxHeight();
        @Property double maxWidth();
        @Property double minHeight();
        @Property double minWidth();

        @Property Modality modality();

        @Property Window owner();

        @Property StageStyle style();

        @Property boolean iconified();
        //@Property boolean resizable(); see below.

        @Property boolean alwaysOnTop();
        @Property boolean maximized();

        @Property ObservableList<Image> icons();

        //void show();

        void toBack();
        void toFront();
        void close();
    }

    public UXForm(Environment env, Stage wrappedObject) {
        super(env, wrappedObject);

        if (wrappedObject.getScene() == null) {
            AnchorPane layout = new AnchorPane();
            Scene scene = new Scene(layout);

            getWrappedObject().setScene(scene);
        }
    }

    public UXForm(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Stage getWrappedObject() {
        return (Stage) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __construct(null);
    }

    @Signature
    public void __construct(@Nullable Stage stage) {
        __wrappedObject = stage == null ? new Stage() : stage;

        AnchorPane layout = new AnchorPane();
        Scene scene = new Scene(layout);

        getWrappedObject().setScene(scene);
    }

    @Getter
    protected boolean getResizable() {
        return getWrappedObject().isResizable();
    }

    private void fixResizeBug() {
        getWrappedObject().sizeToScene();
    }

    @Setter
    protected void setResizable(Environment env, boolean value) {
        if (!value) {
            getWrappedObject().setResizable(value);
        } else {
            getWrappedObject().setResizable(value);
        }

        getWrappedObject().removeEventFilter(WindowEvent.WINDOW_SHOWN, fixResizeBugEventFilter);
        getWrappedObject().addEventFilter(WindowEvent.WINDOW_SHOWN, fixResizeBugEventFilter);
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

    @Setter
    protected void setVisible(boolean value) {
        if (value) {
            getWrappedObject().show();
        } else {
            getWrappedObject().hide();
        }
    }

    @Signature
    public void maximize() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        getWrappedObject().setX(bounds.getMinX());
        getWrappedObject().setY(bounds.getMinY());
        getWrappedObject().setWidth(bounds.getWidth());
        getWrappedObject().setHeight(bounds.getHeight());
    }

    @Setter
    public void setTransparent(boolean value) {
        if (value) {
            getWrappedObject().getScene().setFill(null);
        } else {
            getWrappedObject().getScene().setFill(Color.WHITE);
        }
    }

    @Getter
    public boolean getTransparent() {
        return getWrappedObject().getScene().getFill() == null;
    }

    @Setter
    public void setFullScreen(final boolean value) {
        getWrappedObject().setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        getWrappedObject().setFullScreenExitHint(null);

        getWrappedObject().setFullScreen(value);

        if (value) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    getWrappedObject().setFullScreen(false);
                    getWrappedObject().setFullScreen(value);
                }
            });
        }
    }

    @Getter
    public boolean getFullScreen() {
        return getWrappedObject().isFullScreen();
    }

    @Signature
    public void showAndWait() {
        if (!getWrappedObject().isShowing()) {
            getWrappedObject().showAndWait();
        }
    }

    @Signature
    public void show() {
        getWrappedObject().show();
    }
}
