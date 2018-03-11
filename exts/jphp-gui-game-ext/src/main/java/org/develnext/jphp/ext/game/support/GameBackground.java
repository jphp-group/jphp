package org.develnext.jphp.ext.game.support;

import javafx.animation.AnimationTimer;
import javafx.css.Styleable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class GameBackground extends Canvas implements Styleable {
    private final GraphicsContext g2;
    protected Image image;
    protected Image drawImage;
    protected boolean autoSize;

    protected Vec2d viewPos = new Vec2d(0, 0);
    protected Vec2d velocity = new Vec2d(0, 0);

    protected AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (image != null) {
                if (velocity.x != 0 || velocity.y != 0) {
                    float dt = 1 / 60.0f;

                    viewPos.x += GameScene2D.toPixels(velocity.x * dt);
                    viewPos.y += GameScene2D.toPixels(velocity.y * dt);

                    if (viewPos.x + image.getWidth() < 0) {
                        viewPos.x = 0;
                    }

                    if (viewPos.y + image.getHeight() < 0) {
                        viewPos.y = 0;
                    }

                    if (viewPos.x > image.getWidth()) {
                        viewPos.x = 0;
                    }

                    if (viewPos.y > image.getHeight()) {
                        viewPos.y = 0;
                    }

                    update();
                }
            }
        }
    };

    public Vec2d getVelocity() {
        return velocity;
    }

    public Vec2d getViewPos() {
        return viewPos;
    }

    public void setViewPos(Vec2d viewPos) {
        this.viewPos = viewPos;
    }

    public void setVelocity(Vec2d velocity) {
        this.velocity = velocity;
    }

    public void setVelocityX(double value) {
        this.velocity.x = value;
    }

    public void setVelocityY(double value) {
        this.velocity.y = value;
    }

    public double getVelocityX() {
        return this.velocity.x;
    }

    public double getVelocityY() {
        return this.velocity.y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        update();
    }

    public GameBackground() {
        super();

        animationTimer.start();

        g2 = this.getGraphicsContext2D();

        widthProperty().addListener((observableValue, number, t1) -> {
            update();
        });

        heightProperty().addListener((observableValue, number, t1) -> {
            update();
        });

        parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                animationTimer.stop();
            } else {
                animationTimer.start();
            }
        });

        getStyleClass().add("without-focus");
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
        update();
    }

    @Override
    public void resize(double v, double v1) {
        setWidth(v);
        setHeight(v1);
    }

    @Override
    public boolean isResizable() {
        return !autoSize;
    }


    protected void update() {
        if (autoSize) {
            setWidth(image == null ? 0 : image.getWidth());
            setHeight(image == null ? 0 : image.getHeight());
        }

        g2.clearRect(0, 0, getWidth(), getHeight());

        if (image != null) {
            double x = 0, y = 0, w = image.getWidth(), h = image.getHeight();

            int horCount = (int) Math.ceil(getWidth() / w);
            int verCount = (int) Math.ceil(getHeight() / h);

            for (int i = -1; i < verCount + 1; i++) {
                for (int j = -1; j < horCount + 1; j++) {
                    g2.drawImage(image, viewPos.x + (j * w), viewPos.y + (i * h), w, h);
                }
            }
        }
    }
}