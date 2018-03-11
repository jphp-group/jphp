package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.text.UXFont;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Abstract
@Reflection.Name(JavaFXExtension.NS + "UXGraphicsContext")
public class UXGraphicsContext extends BaseWrapper<GraphicsContext> {
    interface WrappedInterface {
        @Property Canvas canvas();

        @Property double globalAlpha();
        @Property BlendMode globalBlendMode();
        @Property FillRule fillRule();
        /*@Property Effect effect();*/

        @Property double lineWidth();
        @Property StrokeLineCap lineCap();
        @Property StrokeLineJoin lineJoin();
        @Property double miterLimit();

        void beginPath();
        void lineTo(double x1, double y1);
        void moveTo(double x0, double y0);
        void quadraticCurveTo(double xc, double yc, double x1, double y1);
        void bezierCurveTo(double xc1, double yc1, double xc2, double yc2, double x1, double y1);
        void arcTo(double x1, double y1, double x2, double y2, double radius);
        void arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length);
        void rect(double x, double y, double w, double h);
        void appendSVGPath(String svgpath);
        void closePath();

        void fill();
        void stroke();
        void clip();

        boolean isPointInPath(double x, double y);

        void clearRect(double x, double y, double w, double h);
        void fillRect(double x, double y, double w, double h);
        void strokeRect(double x, double y, double w, double h);
        void fillOval(double x, double y, double w, double h);
        void strokeOval(double x, double y, double w, double h);
        void fillArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure);
        void strokeArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure);
        void fillRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight);
        void strokeRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight);
        void strokeLine(double x1, double y1, double x2, double y2);
        void fillPolygon(double xPoints[], double yPoints[], int nPoints);
        void strokePolygon(double xPoints[], double yPoints[], int nPoints);
        void strokePolyline(double xPoints[], double yPoints[], int nPoints);

        void fillText(String text, double x, double y);
        void fillText(String text, double x, double y, double maxWidth);

        void strokeText(String text, double x, double y);
        void strokeText(String text, double x, double y, double maxWidth);

        void drawImage(Image img, double x, double y);
        void drawImage(Image img, double x, double y, double w, double h);
        void drawImage(Image img, double x, double y, double w, double h, double dx, double dy, double dw, double dh);

        void translate(double x, double y);
        void scale(double x, double y);
        void rotate(double degrees);

        void applyEffect(Effect e);
        void restore();
        void save();
    }

    public UXGraphicsContext(Environment env, GraphicsContext wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGraphicsContext(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Setter
    @Signature
    public void setFillColor(@Nullable Color color) {
        getWrappedObject().setFill(color);
    }

    @Getter
    public Color getFillColor() {
        return getWrappedObject().getFill() instanceof Color ? (Color) getWrappedObject().getFill() : null;
    }

    @Setter
    @Signature
    public void setStrokeColor(@Nullable Color color) {
        getWrappedObject().setStroke(color);
    }

    @Getter
    public Color getStrokeColor() {
        return getWrappedObject().getStroke() instanceof Color ? (Color) getWrappedObject().getStroke() : null;
    }

    @Getter
    public UXFont getFont(Environment env) {
        return new UXFont(env, getWrappedObject().getFont(), font -> getWrappedObject().setFont(font));
    }

    @Setter
    public void setFont(Font font) {
        getWrappedObject().setFont(font);
    }
}
