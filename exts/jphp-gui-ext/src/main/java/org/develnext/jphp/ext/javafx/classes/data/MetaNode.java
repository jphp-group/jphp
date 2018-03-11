package org.develnext.jphp.ext.javafx.classes.data;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotResult;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.WritableImage;
import javafx.util.Callback;

@SuppressWarnings("deprecation")
public abstract class MetaNode extends Label {
    @Override
    public final WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        return image;
    }

    @Override
    public final void snapshot(Callback<SnapshotResult, Void> callback, SnapshotParameters params, WritableImage image) { }

    @Override
    public final boolean isResizable() {
        return false;
    }

    @Override
    public final boolean contains(double localX, double localY) {
        return false;
    }

    @Override
    protected final boolean containsBounds(double localX, double localY) {
        return false;
    }

    @Override
    public final boolean contains(Point2D localPoint) {
        return false;
    }

    @Override
    public final boolean intersects(double localX, double localY, double localWidth, double localHeight) {
        return false;
    }

    @Override
    public final boolean intersects(Bounds localBounds) {
        return false;
    }

    @Override
    public final void resize(double width, double height) {}

    @Override
    public final void resizeRelocate(double x, double y, double width, double height) {}

    @Override
    public final double computeAreaInScreen() {
        return 0;
    }

    @Override
    public final void relocate(double x, double y) { }

    @Override
    public final void toBack() { }

    @Override
    public final void toFront() { }
}
