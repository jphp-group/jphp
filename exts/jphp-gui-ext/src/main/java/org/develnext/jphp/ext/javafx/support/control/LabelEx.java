package org.develnext.jphp.ext.javafx.support.control;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.text.Font;
import org.develnext.jphp.ext.javafx.classes.text.UXFont;

import java.util.Scanner;

public class LabelEx extends Label {
    public enum AutoSizeType { ALL, HORIZONTAL, VERTICAL }

    protected BooleanProperty autoSize;
    protected ObjectProperty<AutoSizeType> autoSizeType = new SimpleObjectProperty<>(this, "autoSizeType", AutoSizeType.ALL);

    public LabelEx() {
        this("");
    }

    public LabelEx(String text) {
        this(text, null);
    }

    public LabelEx(String text, Node graphic) {
        super(text, graphic);

        setMnemonicParsing(false);

        textProperty().addListener((observable, oldValue, newValue) -> LabelEx.this.updateAutoSize());
        fontProperty().addListener((observable, oldValue, newValue) -> LabelEx.this.updateAutoSize());
        prefWidthProperty().addListener((observable, oldValue, newValue) -> LabelEx.this.updateAutoSize());
        prefHeightProperty().addListener((observable, oldValue, newValue) -> LabelEx.this.updateAutoSize());
        graphicProperty().addListener((observable, oldValue, newValue) -> LabelEx.this.updateAutoSize());
        autoSizeTypeProperty().addListener((observable, oldValue, newValue) -> updateAutoSize());
        styleProperty().addListener((observable, oldValue, newValue) -> updateAutoSize());
        borderProperty().addListener((observable, oldValue, newValue) -> updateAutoSize());
        paddingProperty().addListener((observable, oldValue, newValue) -> updateAutoSize());
    }

    public AutoSizeType getAutoSizeType() {
        return autoSizeType.get();
    }

    public ObjectProperty<AutoSizeType> autoSizeTypeProperty() {
        return autoSizeType;
    }

    public void setAutoSizeType(AutoSizeType autoSizeType) {
        this.autoSizeType.set(autoSizeType);
    }

    void updateAutoSize() {
        if (isAutoSize()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Node graphic = getGraphic();

                    if (getAutoSizeType() == AutoSizeType.ALL || getAutoSizeType() == AutoSizeType.HORIZONTAL) {
                        double width = getTextAutoWidth();

                        if (graphic != null) {
                            width += graphic.getLayoutBounds().getWidth() + getGraphicTextGap();
                        }

                        setPrefWidth(width);
                    }

                    if (getAutoSizeType() == AutoSizeType.ALL || getAutoSizeType() == AutoSizeType.VERTICAL) {
                        setPrefHeight(Math.max(
                                getTextAutoHeight(),
                                graphic == null ? 0 : graphic.getLayoutBounds().getHeight()
                        ));
                    }
                }
            });
        }
    }

    public final BooleanProperty autoSizeProperty() {
        if (autoSize == null) {
            autoSize = new SimpleBooleanProperty(this, "autoSize", false);
        }

        return autoSize;
    }

    protected double getTextAutoWidth() {
        double width = UXFont.calculateTextWidth(getText(), LabelEx.this.getFont());

        Insets padding = getPadding();
        if (padding != null) {
            width += padding.getLeft() + padding.getRight();
        }

        Border border = getBorder();
        if (border != null) {
            width += border.getInsets().getLeft() + border.getInsets().getRight();
        }

        return width;
    }

    protected double getTextAutoHeight() {
        double height = UXFont.getLineHeight(LabelEx.this.getFont());

        int count = 0;

        Scanner scanner = new Scanner(getText());
        while (scanner.hasNextLine()) {
            count++;
            scanner.nextLine();
        }

        if (count > 1) {
            height *= count;
        }

        Insets padding = getPadding();
        if (padding != null) {
            height += padding.getTop() + padding.getBottom();
        }

        Border border = getBorder();
        if (border != null) {
            height += border.getInsets().getTop() + border.getInsets().getBottom();
        }

        return height;
    }

    public final void setAutoSize(boolean value) {
        autoSizeProperty().setValue(value);
        updateAutoSize();
    }

    public final boolean isAutoSize() {
        return autoSize == null ? false : autoSize.getValue();
    }
}
