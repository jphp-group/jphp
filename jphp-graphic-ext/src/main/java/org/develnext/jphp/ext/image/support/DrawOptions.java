package org.develnext.jphp.ext.image.support;

import java.awt.*;

public class DrawOptions {
    private boolean outline;
    private Color fill;
    private Font font;

    public boolean isOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
    }

    public Color getFill() {
        return fill;
    }

    public void setFill(Color fill) {
        this.fill = fill;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
