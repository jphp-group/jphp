package org.develnext.jphp.ext.game.classes;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.game.GameExtension;
import org.develnext.jphp.ext.game.support.GamePane;
import org.develnext.jphp.ext.javafx.classes.layout.UXAnchorPane;
import org.develnext.jphp.ext.javafx.classes.layout.UXPanel;
import org.develnext.jphp.ext.javafx.classes.layout.UXScrollPane;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(GameExtension.NS)
public class UXGamePane extends UXPanel<GamePane> {
    interface WrappedInterface {
        @Property Node content();

        @Property double viewX();
        @Property double viewY();
        @Property double viewWidth();
        @Property double viewHeight();

        void scrollTo(double x, double y);
        void loadArea(AnchorPane area);
    }

    public UXGamePane(Environment env, GamePane wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGamePane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public void __construct() {
        __wrappedObject = new GamePane();
    }

    @Override
    protected double getWidth() {
        return getWrappedObject().getViewWidth();
    }

    @Override
    protected double getHeight() {
        return getWrappedObject().getViewHeight();
    }

    @Override
    protected double[] getSize() {
        return new double[] { getWidth(), getHeight() };
    }

    @Override
    public GamePane getWrappedObject() {
        return (GamePane) super.getWrappedObject();
    }
}
