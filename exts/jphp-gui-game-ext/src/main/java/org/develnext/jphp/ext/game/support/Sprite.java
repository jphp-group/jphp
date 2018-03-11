package org.develnext.jphp.ext.game.support;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Sprite {
    public static class Animation {
        protected String name;
        protected int[] indexes;
        protected int speed = -1;

        public Animation(String name, int... indexes) {
            this.name = name;
            this.indexes = indexes;
        }

        public void setIndexes(int... indexes) {
            this.indexes = indexes;
        }

        public void setRange(int from, int to) {
            int len = to - from;

            indexes = new int[len];

            for (int i = 0; i < len; i++) {
                indexes[i] = i + from;
            }
        }

        public int getMax() {
            return indexes.length - 1;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public String getName() {
            return name;
        }
    }

    public final Map<String, Animation> animations = new HashMap<>();

    protected Image image;
    protected Double frameWidth;
    protected Double frameHeight;

    private int rows = 0;
    private int cols = 0;

    private int maxIndex = -1;

    private int speed = 12;
    private int currentIndex = -1;
    private Animation currentAnimation = null;
    private boolean cycledAnimation = true;

    private boolean freeze = false;
    private boolean flipX = false;
    private boolean flipY = false;

    private Sprite prototype;

    public Sprite() {
    }

    /**
     * Создать клон из спрайта-прототипа, клон будет себя вести немного по другому, брать изображение и кадры из оригинала.
     */
    public Sprite(Sprite sprite) {
        this();

        if (sprite == null) {
            return;
        }

        if (sprite.prototype != null) {
            sprite = sprite.prototype;
        }

        image = sprite.image;
        frameWidth = sprite.frameWidth;
        frameHeight = sprite.frameHeight;
        flipX = sprite.flipX;
        flipY = sprite.flipY;

        speed = sprite.speed;
        cycledAnimation = sprite.cycledAnimation;

        rows = -1;
        cols = -1;
        maxIndex = -1;

        currentAnimation = sprite.currentAnimation;

        prototype = sprite;
    }

    public int getMaxIndex() {
        return prototype != null ? prototype.maxIndex : maxIndex;
    }

    public void freeze() {
        this.freeze = true;
    }

    public void unfreeze() {
        this.freeze = false;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public double getFrameWidth() {
        return prototype != null ? prototype.frameWidth : frameWidth;
    }

    public double getFrameHeight() {
        return prototype != null ? prototype.frameHeight : frameHeight;
    }

    public void setFrameWidth(double frameWidth) {
        if (prototype != null) {
            prototype.frameWidth = frameWidth;
        } else {
            this.frameWidth = frameWidth;
        }
    }

    public void setFrameHeight(double frameHeight) {
        if (prototype != null) {
            prototype.frameHeight = frameHeight;
            prototype.setImage(getImage());
        } else {
            this.frameHeight = frameHeight;
            setImage(getImage());
        }
    }

    public void setFrameSize(double[] size) {
        if (size != null && size.length >= 2) {
            setFrameSize(size[0], size[1]);
        }
    }

    public void setFrameSize(double width, double height) {
        if (prototype != null) {
            prototype.setFrameSize(width, height);
            prototype.setImage(getImage());
        } else {
            this.frameWidth = width;
            this.frameHeight = height;
            setImage(getImage());
        }
    }

    public double[] getFrameSize() {
        if (prototype != null) {
            return prototype.getFrameSize();
        }

        return new double[] { frameWidth, frameHeight };
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Image getImage() {
        if (prototype != null) {
            return prototype.image;
        }

        return image;
    }

    public void setImage(Image image) {
        if (prototype != null) {
            prototype.setImage(image);
            return;
        }

        this.image = image;

        if (image != null) {
            double width = image.getWidth();
            double height = image.getHeight();

            if (frameWidth == null || frameHeight == null) {
                maxIndex = 1;
                cols = 1;
                rows = 1;
                setFrameSize(width, height);
            } else {
                cols = (int) Math.floor(width / frameWidth);
                rows = (int) Math.floor(height / frameHeight);

                maxIndex = (cols * rows) - 1;
            }
        } else {
            rows = cols = maxIndex = -1;
        }
    }

    public void setAnimation(String name, int... indexes) {
        if (prototype != null) {
            prototype.setAnimation(name, indexes);
            return;
        }

        synchronized (animations) {
            animations.put(name, new Animation(name, indexes));
        }
    }

    public void setAnimation(String name, int from, int length) {
        if (prototype != null) {
            prototype.setAnimation(name, from, length);
            return;
        }

        synchronized (animations) {
            Animation value = new Animation(name);
            value.setRange(from, from + length + 1);
            animations.put(name, value);
        }
    }

    synchronized public void setAnimationSpeed(String name, int speed) {
        if (prototype != null) {
            prototype.setAnimationSpeed(name, speed);
            return;
        }

        synchronized (animations) {
            if (!animations.containsKey(name)) {
                setAnimation(name);
            }

            animations.get(name).setSpeed(speed);
        }
    }

    public boolean isCycledAnimation() {
        return cycledAnimation;
    }

    public void setCycledAnimation(boolean cycledAnimation) {
        this.cycledAnimation = cycledAnimation;
    }

    public String getCurrentAnimation() {
        return currentAnimation == null ? null : currentAnimation.getName();
    }

    public void setCurrentAnimation(String name) {
        this.currentAnimation = prototype != null ? prototype.animations.get(name) : animations.get(name);

        if (this.currentAnimation != null) {
            if (this.currentAnimation.getSpeed() != -1) {
                setSpeed(this.currentAnimation.getSpeed());
            }
        }

        if (name != null && !name.isEmpty() && currentAnimation == null) {
            throw new IllegalArgumentException("Animation '" + name + "' not found");
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void play(String animation) {
        unfreeze();
        setCurrentAnimation(animation);
    }

    public void play(String animation, int speed) {
        unfreeze();
        setCurrentAnimation(animation);
        setSpeed(speed);
    }

    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    public void drawNext(Canvas canvas) {
        currentIndex++;

        if (currentIndex > maxIndex) {
            currentIndex = 0;
        }

        draw(canvas, currentIndex);
    }

    public void drawByTime(Canvas canvas, long now) {
        int maxIndex = prototype == null ? this.maxIndex : prototype.maxIndex;

        if (freeze || maxIndex < 1) {
            return;
        }

        int drawIndex = -1;

        if (currentAnimation != null) {
            if (speed <= 0) {
                drawIndex = currentAnimation.indexes[currentIndex];
            } else {
                int newIndex = (int) Math.floor((now - currentIndex) / (1000000000 / speed)); //Determine how many frames we need to advance to maintain frame rate independence

                newIndex = newIndex % (currentAnimation.getMax() + 1);
                drawIndex = currentAnimation.indexes[newIndex];
            }

            if (drawIndex >= 0) {
                boolean last = drawIndex < currentIndex;

                draw(canvas, drawIndex);

                if (!cycledAnimation && last) {
                    freeze();
                }
            }
        } else {
            if (speed <= 0) {
                drawIndex = currentIndex;
            } else {
                int newIndex = (int) Math.floor((now - currentIndex) / (1000000000 / speed)); //Determine how many frames we need to advance to maintain frame rate independence

                drawIndex = newIndex % (maxIndex + 1);
            }

            if (cycledAnimation || (currentIndex == -1 || currentIndex < drawIndex)) {
                if (drawIndex >= 0) {
                    draw(canvas, drawIndex);
                }
            }
        }
    }

    public void draw(Canvas node, int index) {
        if (freeze) {
            return;
        }

        /*if (currentIndex == index) {
            return;
        } */

        currentIndex = index;
        GraphicsContext gc = node.getGraphicsContext2D();

        gc.setFill(Color.TRANSPARENT);
        gc.clearRect(0, 0, node.getWidth(), node.getHeight());

        double frameWidth = this.getFrameWidth();
        double frameHeight = this.getFrameHeight();

        int maxIndex = prototype != null ? prototype.maxIndex : this.maxIndex;

        Image image = prototype != null ? prototype.image : this.image;

        if (image == null || frameHeight < 0.1 || frameWidth < 0.1 || index > maxIndex || index < 0) {
            return;
        }

        int cols = prototype != null ? prototype.cols : this.cols;

        //
        int row = index / cols;
        int col = index % cols;

        double x = col * frameWidth;
        double y = row * frameHeight;

        gc.drawImage(
                image, x, y, frameWidth, frameHeight,
                flipX ? frameWidth : 0,
                flipY ? frameHeight : 0,
                frameWidth * (flipX ? -1 : 1),
                frameHeight * (flipY ? -1 : 1)
        );
    }

    public Image getFrameImage(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index must be greater or equal to zero");
        }

        if (index > maxIndex) {
            throw new IllegalArgumentException("index must be smaller or equal to " + maxIndex);
        }

        double w = frameWidth == null ? 0 : frameWidth;
        double h = frameHeight == null ? 0 : frameHeight;

        Canvas canvas = new Canvas(w, h);

        draw(canvas, index);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        return canvas.snapshot(parameters, null);
    }
}
