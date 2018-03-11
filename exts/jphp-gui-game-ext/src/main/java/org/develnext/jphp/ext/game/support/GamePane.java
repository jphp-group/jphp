package org.develnext.jphp.ext.game.support;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.develnext.jphp.ext.javafx.support.control.Panel;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;

public class GamePane extends Panel {
    protected GameScene2D scene = null;
    protected AnchorPane area;

    protected EventHandler<ActionEvent> onScrollScene;
    protected ObjectProperty<AnchorPane> content = new SimpleObjectProperty<>(null);

    public GamePane() {
        setFocusTraversable(false);
        setBorderWidth(0);
        setBackgroundColor(Color.WHITE);

        addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                    case DOWN:
                    case LEFT:
                    case SPACE:
                    case RIGHT:
                        event.consume();
                }
            }
        });

        addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();
            }
        });

        contentProperty().addListener(new ChangeListener<AnchorPane>() {
            @Override
            public void changed(ObservableValue<? extends AnchorPane> observable, AnchorPane oldValue, AnchorPane newValue) {
                if (newValue == null) {
                    getChildren().clear();
                } else {
                    getChildren().setAll(newValue);
                }
            }
        });
        setContent(new AnchorPane());

        getStyleClass().add("without-focus");
    }

    public void loadArea(final AnchorPane area) {
        setContent(area);
        requestFocus();
    }

    public AnchorPane getContent() {
        return content.get();
    }

    public ObjectProperty<AnchorPane> contentProperty() {
        return content;
    }

    public void setContent(AnchorPane content) {
        this.content.set(content);
        area = content;

        AnchorPane.setLeftAnchor(content, null);
        AnchorPane.setTopAnchor(content, null);
        AnchorPane.setBottomAnchor(content, null);
        AnchorPane.setRightAnchor(content, null);
    }

    public double getViewX() {
        return -area.getLayoutX();
    }

    public double getViewY() {
        return -area.getLayoutY();
    }

    public double getViewWidth() {
        return getPrefWidth();
    }

    public double getViewHeight() {
        return getPrefHeight();
    }

    public DoubleProperty viewXProperty() {
        return area.layoutXProperty();
    }

    public DoubleProperty viewYProperty() {
        return area.layoutYProperty();
    }

    public void scrollTo(double x, double y) {
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        if (y > area.getPrefHeight() - getPrefHeight()) {
            y = area.getPrefHeight() - getPrefHeight();
        }

        if (x > area.getPrefWidth() - getPrefWidth()) {
            x = area.getWidth() - getPrefWidth();
        }

        area.setClip(new Rectangle(x, y, getPrefWidth(), getPrefHeight()));
        area.setLayoutX(-x);
        area.setLayoutY(-y);

        if (getOnScrollScene() != null) {
            getOnScrollScene().handle(new ActionEvent(this, this));
        }
    }

    public void setGameScene(GameScene2D scene) {
        if (this.scene != null) {
            this.scene.setScrollHandler(null);
        }

        this.scene = scene;
    }

    public GameScene2D getGameScene() {
        return scene;
    }

    public EventHandler<ActionEvent> getOnScrollScene() {
        return onScrollScene;
    }

    public void setOnScrollScene(EventHandler<ActionEvent> onScrollScene) {
        this.onScrollScene = onScrollScene;
    }
}
