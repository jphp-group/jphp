package org.develnext.jphp.ext.game.support;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SpriteView extends Canvas {
    protected ObjectProperty<Sprite> sprite = new SimpleObjectProperty<>(null);

    private boolean flipX;
    private boolean flipY;

    private boolean animationEnabled = false;
    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long internalTime) {
            update(internalTime);
        }
    };

    public SpriteView() {
        setPickOnBounds(false);

        EventHandler<MouseEvent> eventFilter = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sprite.get() == null) {
                    event.consume();
                }

                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);

                WritableImage snapshot = SpriteView.this.snapshot(parameters, null);

                int x = (int) event.getX();
                int y = (int) event.getY();

                if (x >= snapshot.getWidth() || x < 0 || y >= snapshot.getHeight() || y < 0) {
                    return;
                }

                PixelReader pixelReader = snapshot.getPixelReader();
                double opacity = pixelReader.getColor(x, y).getOpacity();

                if (opacity <= 0.00001) {
                    event.consume();
                }
            }
        };

        addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter);
        addEventFilter(MouseEvent.MOUSE_PRESSED, eventFilter);
        addEventFilter(MouseEvent.MOUSE_RELEASED, eventFilter);

        addEventFilter(MouseEvent.MOUSE_ENTERED, eventFilter);
        addEventFilter(MouseEvent.MOUSE_EXITED, eventFilter);
        addEventFilter(MouseEvent.MOUSE_MOVED, eventFilter);
    }

    public SpriteView(Sprite sprite) {
        this();
        setSprite(sprite);
    }

    public ObjectProperty<Sprite> spriteProperty() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite.set( sprite == null ? null : new Sprite(sprite) );

        if (sprite == null) {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
        } else {
            setWidth(this.sprite.get().getFrameWidth());
            setHeight(this.sprite.get().getFrameHeight());
            setFlipX(flipX);
            setFlipY(flipY);

            setFrame(0);
        }
    }

    public Sprite getSprite() {
        return sprite.get();
    }

    public boolean getAnimationEnabled() {
        return animationEnabled;
    }

    public String getAnimationName() {
        return sprite.get().getCurrentAnimation();
    }

    public void setAnimationName(String name) {
        sprite.get().setCurrentAnimation(name);
    }

    public void setAnimationSpeed(int speed) {
        sprite.get().setSpeed(speed);
    }

    public int getAnimationSpeed() {
        return sprite.get().getSpeed();
    }

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;

        Sprite sprite = getSprite();
        if (sprite != null) {
            sprite.setFlipX(flipX);

            if (sprite.isFreeze()) {
                setFrame(getFrame());
            }
        }
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;

        Sprite sprite = getSprite();
        if (sprite != null) {
            sprite.setFlipY(flipY);

            if (sprite.isFreeze()) {
                setFrame(getFrame());
            }
        }
    }

    public void playOnce(String animation) {
        Sprite sprite = getSprite();

        if (sprite != null) {
            setAnimationEnabled(true);
            sprite.setCycledAnimation(false);
            sprite.play(animation);
        }
    }

    public void playOnce(String animation, int speed) {
        Sprite sprite = getSprite();
        if (sprite != null) {
            setAnimationEnabled(true);
            sprite.setCycledAnimation(false);
            sprite.play(animation, speed);
        }
    }

    public void play(String animation) {
        Sprite sprite = getSprite();
        if (sprite != null) {
            setAnimationEnabled(true);
            sprite.setCycledAnimation(true);
            sprite.play(animation);
        }
    }

    public void play(String animation, int speed) {
        Sprite sprite = getSprite();
        if (sprite != null) {
            setAnimationEnabled(true);
            sprite.setCycledAnimation(true);
            sprite.play(animation, speed);
        }
    }

    public boolean isPaused() {
        if (getSprite() != null) {
            return getSprite().isFreeze();
        }

        return false;
    }

    public void pause() {
        if (getSprite() != null) {
            getSprite().freeze();
        }
    }

    public void resume() {
        if (getSprite() != null) {
            getSprite().unfreeze();
        }
    }

    public void setFrame(int index) {
        Sprite sprite = getSprite();

        if (sprite != null) {
            setAnimationEnabled(false);

            sprite.unfreeze();
            sprite.draw(this, index);
            sprite.freeze();
        }
    }

    public int getFrameCount() {
        Sprite sprite = getSprite();

        if (sprite != null) {
            return sprite.getMaxIndex() + 1;
        } else {
            return 0;
        }
    }

    public int getFrame() {
        Sprite sprite = getSprite();

        if (sprite != null) {
            return sprite.getCurrentIndex();
        } else {
            return -1;
        }
    }

    public void setAnimationEnabled(boolean value) {
        if (animationEnabled != value) {
            animationEnabled = value;

            if (value) {
                timer.start();
            } else {
                timer.stop();
            }
        }
    }

    public final void update(final long now) {
        onUpdate(now);
    }

    protected void onUpdate(long now) {
        if (!animationEnabled) {
            timer.stop();
            return;
        }

        if (!isVisible()) {
            return;
        }

        Sprite sprite = this.sprite.get();

        if (sprite != null) {
            sprite.drawByTime(this, now);
        }
    }
}
