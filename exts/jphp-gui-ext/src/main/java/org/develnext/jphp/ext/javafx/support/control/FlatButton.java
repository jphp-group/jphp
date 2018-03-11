package org.develnext.jphp.ext.javafx.support.control;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class FlatButton extends Button {
    private static final String DEFAULT_STYLE_CLASS = "x-flat-button";

    protected Color color;
    protected Color hoverColor;
    protected Color clickColor;

    protected double borderRadius = 0;

    private boolean _entered = false;
    private boolean _clicked = false;

    public FlatButton() {
        this(null, null);
    }

    public FlatButton(String text) {
        this(text, null);
    }

    public FlatButton(String text, Node graphic) {
        super(text, graphic);

        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setTextFill(Color.BLACK);
        setColor(null);
        setHoverColor(null);
        setClickColor(null);

        addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (hoverColor != null) {
                    setBackgroundColor(hoverColor);
                } else {
                    setBackgroundColor(color);
                }

                _entered = true;
            }
        });

        addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setBackgroundColor(color);
                _entered = false;
            }
        });

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                _clicked = true;

                if (clickColor != null) {
                    setBackgroundColor(clickColor);
                } else {
                    setBackgroundColor(hoverColor != null && _entered ? hoverColor : color);
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                _clicked = false;

                setBackgroundColor(hoverColor != null && _entered ? hoverColor : color);
            }
        });
    }

    protected void setBackgroundColor(Color color) {
        setBackground(new Background(new BackgroundFill(color, new CornerRadii(borderRadius), null)));
    }

    protected Color getBackgroundColor() {
        Background background = getBackground();

        if (background != null && background.getFills().size() > 0) {
            Paint fill = background.getFills().get(0).getFill();
            if (fill instanceof Color) {
                return (Color) fill;
            }
        }

        return null;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;

        if (!_entered && !_clicked) {
            setBackgroundColor(color);
        }
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;

        if (_entered && hoverColor != null) {
            setBackgroundColor(hoverColor);
        }
    }

    public Color getClickColor() {
        return clickColor;
    }

    public void setClickColor(Color clickColor) {
        this.clickColor = clickColor;

        if (_clicked && clickColor != null) {
            setBackgroundColor(clickColor);
        }
    }

    public double getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(double borderRadius) {
        this.borderRadius = borderRadius;
        setBackgroundColor(getBackgroundColor());
    }
}
