package org.develnext.jphp.ext.javafx.support;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.develnext.jphp.ext.javafx.classes.text.UXFont;

public class ImageViewEx extends Canvas implements Styleable {
    private final GraphicsContext g2;
    protected Image image;

    protected Image originImage;
    protected Image hoverImage;
    protected Image clickImage;

    protected boolean autoSize;
    protected boolean stretch;
    protected boolean smartStretch;
    protected boolean centered;
    protected boolean proportional;

    protected boolean flipX;
    protected boolean flipY;

    protected boolean mosaic;
    protected double mosaicGap = 0;

    private boolean _entered = false;

    protected Paint background = null;

    protected Font font = new Font("System", 12);
    protected Paint textFill = Color.BLACK;
    protected String text;

    public ImageViewEx() {
        super();

        g2 = this.getGraphicsContext2D();

        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                update();
            }
        });

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                update();
            }
        });

        addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ImageViewEx.this._entered = true;
                if (hoverImage != null) {
                    ImageViewEx.this.image = hoverImage;
                    update();
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ImageViewEx.this.image = originImage;
                ImageViewEx.this._entered = false;
                update();
            }
        });

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (clickImage != null) {
                    ImageViewEx.this.image = clickImage;
                    update();
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (_entered && hoverImage != null) {
                    ImageViewEx.this.image = hoverImage;
                    update();
                } else if (image != originImage) {
                    ImageViewEx.this.image = originImage;
                    update();
                }
            }
        });
    }

    public Image getImage() {
        return this.originImage;
    }

    public void setImage(Image image) {
        this.image = image;
        this.originImage = image;

        update();
    }

    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
        update();
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
        update();
    }

    public Image getHoverImage() {
        return hoverImage;
    }

    public void setHoverImage(Image hoverImage) {
        this.hoverImage = hoverImage;
    }

    public Image getClickImage() {
        return clickImage;
    }

    public void setClickImage(Image clickImage) {
        this.clickImage = clickImage;
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
        update();
    }

    public boolean isStretch() {
        return stretch;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
        update();
    }

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
        update();
    }

    public boolean isMosaic() {
        return mosaic;
    }

    public void setMosaic(boolean mosaic) {
        this.mosaic = mosaic;
        update();
    }

    public double getMosaicGap() {
        return mosaicGap;
    }

    public void setMosaicGap(double mosaicGap) {
        this.mosaicGap = mosaicGap;
        update();
    }

    public boolean isProportional() {
        return proportional;
    }

    public void setProportional(boolean proportional) {
        this.proportional = proportional;
        update();
    }

    public boolean isPreserveRatio() {
        return isProportional();
    }

    public void setPreserveRatio(boolean proportional) {
        setProportional(proportional);
    }

    public Paint getBackground() {
        return background;
    }

    public void setBackground(Paint background) {
        this.background = background;
        update();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        update();
    }

    public Paint getTextFill() {
        return textFill;
    }

    public void setTextFill(Paint textFill) {
        this.textFill = textFill;
        update();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        update();
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    @Override
    public boolean isResizable() {
        return !autoSize;
    }

    @Override
    public void resize(double v, double v1) {
        setWidth(v);
        setHeight(v1);
    }

    protected void update() {
        if (autoSize) {
            setWidth(image == null ? 0 : image.getWidth());
            setHeight(image == null ? 0 : image.getHeight());
        }

        g2.clearRect(0, 0, getWidth(), getHeight());

        if (background != null) {
            g2.setFill(background);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (image != null) {
            double x = 0, y = 0, w = image.getWidth(), h = image.getHeight();

            if (isMosaic()) {
                int horCount = (int) Math.ceil(getWidth() / w + mosaicGap);
                int verCount = (int) Math.ceil(getHeight() / h + mosaicGap);

                int startX = 0;
                int startY = 0;

                for (int i = startY; i < verCount; i++) {
                    for (int j = startX; j < horCount; j++) {
                        g2.drawImage(image, j * w + (j * mosaicGap), i * h + (i * mosaicGap), w, h);
                    }
                }
            } else {
                if (isStretch()) {
                    if (!smartStretch || (w > getWidth() || h > getHeight())) {
                        if (isProportional()) {
                            double ratio = Math.min(getWidth() / w, getHeight() / h);
                            w = (int) (w * ratio);
                            h = (int) (h * ratio);
                        } else {
                            w = getWidth();
                            h = getHeight();
                        }
                    }
                }

                if (isCentered()) {
                    x = Math.round(getWidth() / 2 - w / 2);
                    y = Math.round(getHeight() / 2 - h / 2);
                }

                if (isStretch() && (flipX || flipY)) {
                    Canvas canvas = new Canvas();
                    canvas.setWidth(w);
                    canvas.setHeight(h);

                    GraphicsContext g2_tmp = canvas.getGraphicsContext2D();
                    g2_tmp.drawImage(image, x, y, w, h);

                    SnapshotParameters snapshotParameters = new SnapshotParameters();
                    snapshotParameters.setFill(Color.TRANSPARENT);
                    WritableImage snapshot = canvas.snapshot(snapshotParameters, null);

                    g2.drawImage(
                            snapshot, x, y, w, h,
                            flipX ? w : 0,
                            flipY ? h : 0,
                            w * (flipX ? -1 : 1),
                            h * (flipY ? -1 : 1)
                    );
                } else {
                    if (flipX || flipY) {
                        g2.drawImage(
                                image, x, y, w, h,
                                flipX ? w : 0,
                                flipY ? h : 0,
                                w * (flipX ? -1 : 1),
                                h * (flipY ? -1 : 1)
                        );
                    } else {
                        g2.drawImage(image, x, y, w, h);
                    }
                }
            }
        }

        if (text != null && !text.trim().isEmpty()) {
            g2.setFont(font);
            g2.setFill(textFill);

            double fWidth = UXFont.calculateTextWidth(text, font);
            double fHeight = UXFont.getLineHeight(font);

            g2.fillText(text, getWidth() / 2 - fWidth / 2, getHeight() / 2 + fHeight / 4, getWidth());
        }
    }

    public boolean isSmartStretch() {
        return smartStretch;
    }

    public void setSmartStretch(boolean smartStretch) {
        this.smartStretch = smartStretch;
        update();
    }
}
