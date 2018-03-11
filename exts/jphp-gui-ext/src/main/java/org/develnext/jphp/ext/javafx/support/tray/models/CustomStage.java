package org.develnext.jphp.ext.javafx.support.tray.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomStage extends PopupControl {
    private Location bottomRight;
    private Location bottomLeft;
    private Location topLeft;
    private Location topRight;

    private final AnchorPane ap;
    private double horGap;
    private double verGap;

    private Location location;

    public CustomStage(AnchorPane ap, double horGap, double verGap) {
        this.ap = ap;
        this.horGap = horGap;
        this.verGap = verGap;

        setSize(ap.getPrefWidth(), ap.getPrefHeight());
    }

    protected void reinit() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double x = screenBounds.getMinX() + screenBounds.getWidth() - ap.getPrefWidth() - horGap;
        double y = screenBounds.getMinY() + screenBounds.getHeight() - ap.getPrefHeight() - verGap;

        bottomRight = new Location(x, y);
        bottomLeft = new Location(horGap, y);
        topLeft = new Location(horGap, verGap);
        topRight = new Location(x, verGap);
    }

    public double getHorGap() {
        return horGap;
    }

    public double getVerGap() {
        return verGap;
    }

    public void setHorGap(double horGap) {
        this.horGap = horGap;
        reinit();
    }

    public void setVerGap(double verGap) {
        this.verGap = verGap;
        reinit();
    }

    public Location getBottomRight(int viewIndex) {
        if (viewIndex > 0) {
            return new Location(bottomRight.getX(), bottomRight.getY() - (viewIndex * (getHeight() + 2)));
        }

        return bottomRight;
    }

    public Location getBottomLeft(int viewIndex) {
        if (viewIndex > 0) {
            return new Location(bottomLeft.getX(), bottomLeft.getY() - (viewIndex * (getHeight() + 2)));
        }

        return bottomLeft;
    }

    public Location getTopLeft(int viewIndex) {
        if (viewIndex > 0) {
            return new Location(topLeft.getX(), topLeft.getY() - (viewIndex * (getHeight() + 2)));
        }

        return topLeft;
    }

    public Location getTopRight(int viewIndex) {
        if (viewIndex > 0) {
            return new Location(topRight.getX(), topRight.getY() + (viewIndex * (getHeight() + 2)));
        }

        return topRight;
    }

    public void setSize(double width, double height) {
        setWidth(width);
        setHeight(height);
    }

    public Location getOffScreenBounds() {
        Location loc = getBottomRight(0);

        return new Location(loc.getX() + this.getWidth(), loc.getY());
    }

    public void setLocation(Location loc) {
        setX(loc.getX());
        setY(loc.getY());

        location = loc;
    }

    public Location getLocation() {
        return location;
    }

    private SimpleDoubleProperty xLocationProperty = new SimpleDoubleProperty() {
        @Override
        public void set(double newValue) {
            setX(newValue);
        }

        @Override
        public double get() {
            return getX();
        }
    };

    public SimpleDoubleProperty xLocationProperty() {
        return xLocationProperty;
    }

    private SimpleDoubleProperty yLocationProperty = new SimpleDoubleProperty() {
        @Override
        public void set(double newValue) {
            setY(newValue);
        }

        @Override
        public double get() {
            return getY();
        }
    };

    public SimpleDoubleProperty yLocationProperty() {
        return yLocationProperty;
    }
}
