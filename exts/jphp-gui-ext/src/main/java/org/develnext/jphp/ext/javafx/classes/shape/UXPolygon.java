package org.develnext.jphp.ext.javafx.classes.shape;

import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.UXList;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.ArrayList;
import java.util.List;

@Name(JavaFXExtension.NS + "shape\\UXPolygon")
public class UXPolygon extends UXShape<Polygon> {
    interface WrappedInterface {
        @Property UXList points();
    }

    protected Double _width = null;
    protected Double _height = null;

    public UXPolygon(Environment env, Polygon wrappedObject) {
        super(env, wrappedObject);
    }

    public UXPolygon(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Polygon();
    }

    @Signature
    public void __construct(double[] points) {
        __wrappedObject = new Polygon(points);
    }

    public static double getWidth(Polygon polygon) {
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;

        Double[] points = polygon.getPoints().toArray(new Double[0]);

        for(int i = 0; i < points.length; i += 2) {
            double x = points[i];
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }

        return maxX - minX;
    }

    public static double getHeight(Polygon polygon) {
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        Double[] points = polygon.getPoints().toArray(new Double[0]);

        for(int i = 0; i < points.length; i += 2) {
            double y = points[i + 1];
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        return maxY - minY;
    }

    public static void setWidth(Polygon polygon, double newWidth) {
        double width = getWidth(polygon);

        double offset = newWidth - width;

        int i = 0;
        ObservableList<Double> _points = polygon.getPoints();
        List<Double> points = new ArrayList<>(_points.size());

        for (Double point : _points) {
            if (i % 2 == 0 && Double.compare(point, 0d) != 0) {
                if (point < 0) {
                    points.add(point - offset / 2);
                } else {
                    points.add(point + offset / 2);
                }
            } else {
                points.add(point);
            }

            i++;
        }

        polygon.getPoints().setAll(points);
    }

    public static void setHeight(Polygon polygon, double newHeight) {
        double height = getHeight(polygon);

        double offset = newHeight - height;

        int i = 0;
        ObservableList<Double> _points = polygon.getPoints();
        List<Double> points = new ArrayList<>(_points.size());

        for (Double point : _points) {
            if (i % 2 != 0 && Double.compare(point, 0d) != 0) {
                if (point < 0) {
                    points.add(point - offset / 2);
                } else {
                    points.add(point + offset / 2);
                }
            } else {
                points.add(point);
            }

            i++;
        }

        polygon.getPoints().setAll(points);
    }

    @Override
    @Signature
    protected void setWidth(double newWidth) {
        setWidth(getWrappedObject(), newWidth);
    }

    @Override
    @Signature
    protected void setHeight(double newHeight) {
        setHeight(getWrappedObject(), newHeight);
    }

    @Override
    @Signature
    protected double getWidth() {
        if (_width != null) {
            return _width;
        }

        return _width = getWidth(getWrappedObject());
    }

    @Override
    @Signature
    protected double getHeight() {
        if (_height != null) {
            return _height;
        }

        return _height = getHeight(getWrappedObject());
    }

    @Override
    public void setX(double v) {
        super.setX(v + getWidth() / 2);
    }

    @Override
    public void setY(double v) {
        super.setY(v + getHeight() / 2);
    }

    @Override
    public double getX() {
        return super.getX() - getWidth() / 2;
    }

    @Override
    public double getY() {
        return super.getY() - getHeight() / 2;
    }
    @Override
    protected void setSize(double[] size) {
        if (size.length > 1) {
            setWidth(size[0]);
            setHeight(size[1]);
        }
    }
}
