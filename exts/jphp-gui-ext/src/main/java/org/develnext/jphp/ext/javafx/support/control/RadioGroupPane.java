package org.develnext.jphp.ext.javafx.support.control;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class RadioGroupPane<T> extends VBox {
    private ObjectProperty<ObservableList<T>> items;
    private Font font = Font.getDefault();
    private Color textColor = Color.BLACK;
    private List<RadioButton> buttons = new ArrayList<>();

    protected EventHandler<Event> onAction = null;

    private Orientation orientation = Orientation.VERTICAL;
    private ToggleGroup group = new ToggleGroup();

    public final void setItems(ObservableList<T> value) {
        itemsProperty().set(value);
    }

    public final ObservableList<T> getItems() {
        return items == null ? null : items.get();
    }

    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        if (items == null) {
            items = new SimpleObjectProperty<>(this, "items");
        }

        return items;
    }

    public RadioGroupPane() {
        setSpacing(5);
        setPrefHeight(-1);

        itemsProperty().addListener(new ChangeListener<ObservableList<T>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldValue, ObservableList<T> newValue) {
                update();

                if (newValue != null) {
                    newValue.addListener(new ListChangeListener<T>() {
                        @Override
                        public void onChanged(Change<? extends T> c) {
                            update();
                        }
                    });
                }
            }
        });

        setItems(FXCollections.<T>observableArrayList());

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (onAction != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            onAction.handle(new Event(RadioGroupPane.this, RadioGroupPane.this, EventType.ROOT));
                        }
                    });
                }
            }
        });
    }

    public ToggleGroup getToggleGroup() {
        return group;
    }

    public T getSelected() {
        Toggle toggle = getToggleGroup().getSelectedToggle();
        T selected = null;

        if (toggle != null) {
            selected = (T) toggle.getUserData();
        }

        return selected;
    }

    public void setSelected(T item) {
        if (item == null) {
            getToggleGroup().selectToggle(null);
        } else {
            for (RadioButton button : buttons) {
                if (item.equals(button.getUserData())) {
                    getToggleGroup().selectToggle(button);
                    break;
                }
            }
        }
    }

    public int getSelectedIndex() {
        Toggle toggle = getToggleGroup().getSelectedToggle();

        return buttons.indexOf((Toggle) toggle);
    }

    public void setSelectedIndex(int index) {
        if (index < 0 || index > buttons.size() - 1) {
            getToggleGroup().selectToggle(null);
        } else {
            getToggleGroup().selectToggle(buttons.get(index));
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        update();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;

        for (RadioButton button : buttons) {
            button.setFont(font);
        }
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;

        for (RadioButton button : buttons) {
            button.setTextFill(textColor);
        }
    }

    public void update() {
        updateUi(getItems());
    }


    public EventHandler<Event> getOnAction() {
        return onAction;
    }

    public void setOnAction(EventHandler<Event> onAction) {
        this.onAction = onAction;
    }

    protected void updateUi(ObservableList<? extends T> list) {
        T selected = getSelected();

        buttons.clear();
        getChildren().clear();

        ObservableList<Node> children = getChildren();

        if (orientation == Orientation.HORIZONTAL) {
            final HBox hBox = new HBox();
            hBox.setSpacing(getSpacing());
            hBox.setPrefWidth(-1);
            hBox.setPrefHeight(-1);
            hBox.setAlignment(Pos.BASELINE_LEFT);

            spacingProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    hBox.setSpacing(newValue.intValue());
                }
            });

            children.add(hBox);

            children = hBox.getChildren();
        }

        for (T t : list) {
            String label = "" + t;

            RadioButton button = new RadioButton(label);
            button.setToggleGroup(group);
            button.setUserData(t);
            button.setFont(font);
            button.setTextFill(textColor);

            buttons.add(button);
            children.add(button);
        }

        setSelected(selected);
    }
}
