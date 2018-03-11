package org.develnext.jphp.ext.javafx.support.control;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class Panel extends AnchorPane {
    protected Paint borderColor = Color.SILVER;
    protected BorderStrokeStyle borderStyle = BorderStrokeStyle.SOLID;
    protected CornerRadii borderRadius = new CornerRadii(0);
    protected BorderWidths borderWidths = new BorderWidths(1);
    protected String title = "";
    protected String _borderStyle = "SOLID";

    protected LabelEx titleLabel;
    protected Pos titlePosition = Pos.TOP_LEFT;
    protected double titleOffset = 15;

    public Panel() {
        this.titleLabel = new LabelEx();
        this.titleLabel.setAutoSize(true);
        this.titleLabel.setPadding(new Insets(3, 8, 3, 8));
        this.titleLabel.getStyleClass().addAll("x-system-element", "panel-title");
        this.titleLabel.setVisible(false);

        this.titleLabel.heightProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(this::updateTitle);
        });

        this.titleLabel.widthProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(this::updateTitle);
        });

        updateBorder();

        backgroundProperty().addListener((observable, oldValue, newValue) -> updateBorder());

        getChildren().add(this.titleLabel);
    }

    protected void updateTitle() {
        double x = 0;

        switch (titlePosition) {
            case TOP_LEFT:
                x = titleOffset;
                break;
            case TOP_CENTER:
                x = Math.round(getWidth() / 2 - titleLabel.getWidth() / 2);
                break;
            case TOP_RIGHT:
                x = getWidth() - titleLabel.getWidth() - titleOffset;
                break;
        }

        this.titleLabel.setLayoutX(x);
        this.titleLabel.setLayoutY(-(this.titleLabel.getHeight()) + borderWidths.getTop() + (borderRadius.getTopLeftVerticalRadius() > 0 ? 1 : 0));
    }

    protected void updateBorder() {
        Border border = new Border(new BorderStroke(borderColor, borderStyle, borderRadius, borderWidths));
        setBorder(border);

        CornerRadii titleRadius = new CornerRadii(borderRadius.getTopLeftHorizontalRadius(), borderRadius.getTopRightHorizontalRadius(), 0, 0, false);
        titleLabel.setBorder(new Border(new BorderStroke(borderColor, borderColor, Color.TRANSPARENT, borderColor, borderStyle, borderStyle, null, borderStyle, titleRadius, borderWidths, null)));

        Background background = getBackground();

        if (background != null && background.getFills().size() == 1) {
            boolean update = false;

            for (BackgroundFill backgroundFill : background.getFills()) {
                if (!backgroundFill.getRadii().equals(borderRadius)) {
                    update = true;
                    break;
                }
            }

            if (update) {
                BackgroundFill[] fills = new BackgroundFill[background.getFills().size()];

                int i = 0;

                for (BackgroundFill backgroundFill : background.getFills()) {
                    fills[i++] = new BackgroundFill(backgroundFill.getFill(), borderRadius, backgroundFill.getInsets());
                }

                setBackground(new Background(fills));
                titleLabel.setBackground(new Background(fills));
            }
        }

        updateTitle();
    }

    public void setBackgroundColor(Color color) {
        if (color == null) {
            setBackground(null);
            titleLabel.setBackground(null);
        } else {
            setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            titleLabel.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public Color getBackgroundColor() {
        Background background = getBackground();

        if (background != null && background.getFills().size() > 0) {
            Paint fill = background.getFills().get(0).getFill();
            if (fill instanceof Color) {
                return (Color) fill;
            }
        }

        return null;
    }

    public Color getBorderColor() {
        return borderColor instanceof Color ? (Color) borderColor : null;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        updateBorder();
    }

    public String getBorderStyle() {
        return _borderStyle;
    }

    public void setBorderStyle(String borderStyle) {
        try {
            this.borderStyle = (BorderStrokeStyle) BorderStrokeStyle.class.getField(borderStyle.toUpperCase()).get(null);
            this._borderStyle = borderStyle;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Invalid borderStyle - " + borderStyle);
        }
        updateBorder();
    }

    public void setBorderRadius(double radius) {
        if (radius < 0) {
            radius = 0;
        }

        this.borderRadius = new CornerRadii(radius);
        updateBorder();
    }

    public double getBorderRadius() {
        return borderRadius.getTopLeftHorizontalRadius();
    }

    public double getBorderWidth() {
        return borderWidths.getTop();
    }

    public void setBorderWidth(double value) {
        if (value < 0) {
            value = 0;
        }

        borderWidths = new BorderWidths(value);
        updateBorder();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        titleLabel.setText(title);
        titleLabel.setVisible(title != null && !title.isEmpty());
        updateTitle();
    }

    public void setTitleColor(Color color) {
        titleLabel.setTextFill(color);
        updateTitle();
    }

    public Color getTitleColor() {
        return titleLabel.getTextFill() instanceof Color ? (Color) titleLabel.getTextFill() : null;
    }

    public Font getTitleFont() {
        return titleLabel.getFont();
    }

    public void setTitleFont(Font font) {
        titleLabel.setFont(font);
        updateTitle();
    }

    public Pos getTitlePosition() {
        return titlePosition;
    }

    public void setTitlePosition(Pos titlePosition) {
        this.titlePosition = titlePosition;
        updateTitle();
    }

    public double getTitleOffset() {
        return titleOffset;
    }

    public void setTitleOffset(double titleOffset) {
        this.titleOffset = titleOffset;
        updateTitle();
    }
}
