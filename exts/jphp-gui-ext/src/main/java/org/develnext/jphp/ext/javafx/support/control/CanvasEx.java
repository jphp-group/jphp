package org.develnext.jphp.ext.javafx.support.control;

import javafx.scene.canvas.Canvas;

public class CanvasEx extends Canvas {
    public CanvasEx() {
    }

    public CanvasEx(double width, double height) {
        super(width, height);
    }

    @Override
    public boolean isResizable() {
        return true;
    }
    
    @Override
    public void resize(double v, double v1) {
        setWidth(v);
        setHeight(v1);
    }
}
