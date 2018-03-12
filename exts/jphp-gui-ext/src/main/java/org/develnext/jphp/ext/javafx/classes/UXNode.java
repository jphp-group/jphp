package org.develnext.jphp.ext.javafx.classes;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.print.PrinterJob;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.effect.UXEffectPipeline;
import org.develnext.jphp.ext.javafx.classes.support.Eventable;
import org.develnext.jphp.ext.javafx.support.JavaFxUtils;
import org.develnext.jphp.ext.javafx.support.UserData;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.memory.*;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Abstract
@Name(JavaFXExtension.NS + "UXNode")
public class UXNode<T extends Node> extends BaseWrapper<Node> implements Eventable {
    interface WrappedInterface {
        @Property
        double baselineOffset();

        @Property
        BlendMode blendMode();

        @Property
        @Nullable
        Effect effect();

        @Property(hiddenInDebugInfo = true)
        @Nullable
        Node clip();

        @Property(hiddenInDebugInfo = true)
        Orientation contentBias();

        @Property(hiddenInDebugInfo = true)
        DepthTest depthTest();

        @Property
        String id();

        @Property(hiddenInDebugInfo = true)
        double opacity();

        @Property
        double rotate();

        @Property
        double scaleX();

        @Property
        double scaleY();

        @Property
        double scaleZ();

        @Property
        String style();

        @Property
        double translateX();

        @Property
        double translateY();

        @Property
        double translateZ();

        @Property
        boolean cache();

        @Property
        CacheHint cacheHint();

        //@Property boolean disable();
        @Property
        boolean disabled();

        @Property
        boolean focused();

        @Property
        boolean focusTraversable();

        @Property
        boolean hover();

        @Property
        boolean managed();

        @Property
        boolean mouseTransparent();

        @Property
        boolean pickOnBounds();

        @Property
        boolean pressed();

        @Property
        boolean resizable();

        @Property
        boolean visible();

        @Property("classes")
        ObservableList<String> styleClass();

        //@Property(hiddenInDebugInfo = true) @Nullable Object userData();

        boolean contains(double localX, double localY);

        void relocate(double x, double y);

        void resize(double width, double height);

        void startFullDrag();

        void toBack();

        void toFront();

        void requestFocus();
    }

    public UXNode(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXNode(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    public UXNode getRealObject() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends BaseWrapper> wrapperClass = MemoryOperation.getWrapper(getWrappedObject().getClass());
        if (wrapperClass != null) {
            return (UXNode) wrapperClass
                    .getConstructor(Environment.class, getWrappedObject().getClass())
                    .newInstance(getEnvironment(), getWrappedObject());
        } else {
            return this;
        }
    }


    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory info = new ArrayMemory();
        info.refOfIndex("*id").assign(getWrappedObject().getId());
        info.refOfIndex("*nativeType").assign(getWrappedObject().getClass().getName());
        info.refOfIndex("*classes").assign(ArrayMemory.ofStringCollection(getWrappedObject().getStyleClass()));

        return info.toConstant();
    }

    @Getter
    public Memory getEffects(Environment env) {
        Memory effects = data("--effects");

        if (effects.instanceOf(UXEffectPipeline.class)) {
            return effects;
        } else {
            effects = ObjectMemory.valueOf(new UXEffectPipeline(env, this.getWrappedObject()));
            data(env, "--effects", effects);
            return effects;
        }
    }

    @Getter
    public double getX() {
        return getWrappedObject().getLayoutX();
    }

    @Setter
    public void setX(double v) {
        getWrappedObject().setLayoutX(v);
    }

    @Getter
    public double getY() {
        return getWrappedObject().getLayoutY();
    }

    @Setter
    public void setY(double v) {
        getWrappedObject().setLayoutY(v);
    }

    @Getter
    public Memory getUserData(Environment env) {
        Object userData = getWrappedObject().getUserData();

        if (userData == null) {
            return null;
        }

        if (userData instanceof UserData) {
            return ((UserData) userData).getValue();
        }

        return Memory.wrap(env, userData);
    }

    @Setter
    public void setUserData(Environment env, @Nullable Object value) {
        Object userData = getWrappedObject().getUserData();

        if (userData instanceof UserData) {
            ((UserData) userData).setValue(Memory.wrap(env, value));
        } else {
            getWrappedObject().setUserData(value);
        }
    }

    @Getter
    public double[] getScreenPosition() {
        return new double[] { getScreenX(), getScreenY() };
    }

    @Setter
    public void setScreenPosition(double[] value) {
        if (value.length >= 2) {
            setScreenX(value[0]);
            setScreenY(value[1]);
        }
    }

    @Getter
    public double getScreenX() {
        Bounds pt = getWrappedObject().localToScreen(getWrappedObject().getLayoutBounds());
        if (pt == null) {
            return 0;
        }

        return pt.getMinX();
    }

    @Setter
    public void setScreenX(double value) {
        Point2D pt = getWrappedObject().screenToLocal(value, getScreenY());
        getWrappedObject().setLayoutX(pt.getX());
    }

    @Getter
    public double getScreenY() {
        Bounds pt = getWrappedObject().localToScreen(getWrappedObject().getLayoutBounds());
        if (pt == null) {
            return 0;
        }

        return pt.getMinY();
    }

    @Setter
    public void setScreenY(double value) {
        Point2D pt = getWrappedObject().screenToLocal(getScreenX(), value);
        getWrappedObject().setLayoutY(pt.getY());
    }

    @Getter
    public double[] getScale() {
        return new double[] { getWrappedObject().getScaleX(), getWrappedObject().getScaleY() };
    }

    @Setter
    public void setScale(Memory value) {
        if (value.isArray()) {
            getWrappedObject().setScaleX(value.valueOfIndex(0).toDouble());
            getWrappedObject().setScaleY(value.valueOfIndex(1).toDouble());
        } else {
            getWrappedObject().setScaleX(value.toDouble());
            getWrappedObject().setScaleY(value.toDouble());
        }
    }

    @Getter(hiddenInDebugInfo = true)
    public String getClassesString() {
        StringBuilder sb = new StringBuilder();

        Set<String> set = new HashSet<>();

        for (String s : getWrappedObject().getStyleClass()) {
            if (set.add(s)) {
                sb.append(s.trim()).append(" ");
            }
        }

        return sb.toString();
    }

    @Setter
    public void setClassesString(String value) {
        String[] strings = value.split(" ");

        Set<String> set = new LinkedHashSet<>();
        for (String string : strings) {
            set.add(string.trim());
        }

        getWrappedObject().getStyleClass().clear();
        getWrappedObject().getStyleClass().addAll(set);
    }

    @Getter
    public boolean getEnabled() {
        return !getWrappedObject().isDisable();
    }

    @Setter
    public void setEnabled(boolean value) {
        getWrappedObject().setDisable(!value);
    }

    @Getter(hiddenInDebugInfo = true)
    protected double[] getPosition() {
        return new double[]{getX(), getY()};
    }

    @Setter
    protected void setPosition(double[] value) {
        if (value.length >= 2) {
            setX(value[0]);
            setY(value[1]);
        }
    }

    @Getter(hiddenInDebugInfo = true)
    protected double[] getSize() {
        Bounds bounds = getWrappedObject().getLayoutBounds();
        return new double[]{bounds.getWidth(), bounds.getHeight()};
    }

    @Setter
    protected void setSize(double[] size) {
        if (size.length >= 2) {
            setWidth(size[0]);
            setHeight(size[1]);
        }
    }

    @Getter
    public Bounds getBoundsInParent() {
        return getWrappedObject().getBoundsInParent();
    }

    @Getter
    public Bounds getLayoutBounds() {
        return getWrappedObject().getLayoutBounds();
    }

    @Getter
    protected double getWidth() {
        return getWrappedObject().getLayoutBounds().getWidth();
    }

    @Setter
    protected void setWidth(double v) {
        getWrappedObject().resize(v, getHeight());
    }

    @Getter
    protected double getHeight() {
        return getWrappedObject().getLayoutBounds().getHeight();
    }

    @Setter
    protected void setHeight(double v) {
        getWrappedObject().resize(getWidth(), v);
    }

    @Getter
    public Memory getAnchors() {
        ArrayMemory r = new ArrayMemory();

        r.refOfIndex("left").assign(getLeftAnchor());
        r.refOfIndex("top").assign(getTopAnchor());
        r.refOfIndex("right").assign(getRightAnchor());
        r.refOfIndex("bottom").assign(getBottomAnchor());

        return r.toConstant();
    }

    @Getter
    public Memory getAnchorFlags() {
        ArrayMemory r = new ArrayMemory();

        r.refOfIndex("left").assign(getLeftAnchor().isNotNull());
        r.refOfIndex("top").assign(getTopAnchor().isNotNull());
        r.refOfIndex("right").assign(getRightAnchor().isNotNull());
        r.refOfIndex("bottom").assign(getBottomAnchor().isNotNull());

        return r.toConstant();
    }

    @Setter
    public void setAnchorFlags(ArrayMemory value) {
        Parent parent = getWrappedObject().getParent();
        Bounds parentBounds = parent == null ? null : parent.getLayoutBounds();

        if (parent == null) {
            return;
        }

        Effect effect = getWrappedObject().getEffect();
        getWrappedObject().setEffect(null);

        Bounds bounds = getBoundsInParent();

        Border border = parent instanceof Region ? ((Region) parent).getBorder() : null;
        Insets borderInsets = border == null ? Insets.EMPTY : border.getInsets();


        if (value.containsKey("left")) {
            boolean left = value.valueOfIndex("left").toBoolean();

            if (left) {
                setLeftAnchor(DoubleMemory.valueOf(bounds.getMinX() - borderInsets.getLeft()));
            } else {
                setLeftAnchor(Memory.NULL);
            }
        }

        if (value.containsKey("top")) {
            boolean top = value.valueOfIndex("top").toBoolean();

            if (top) {
                setTopAnchor(DoubleMemory.valueOf(bounds.getMinY() - borderInsets.getTop()));
            } else {
                setTopAnchor(Memory.NULL);
            }
        }

        if (value.containsKey("right")) {
            boolean right = value.valueOfIndex("right").toBoolean();

            if (right) {
                setRightAnchor(DoubleMemory.valueOf(parentBounds.getWidth() - bounds.getMaxX() - borderInsets.getRight()));
            } else {
                setRightAnchor(Memory.NULL);
            }
        }

        if (value.containsKey("bottom")) {
            boolean bottom = value.valueOfIndex("bottom").toBoolean();

            if (bottom) {
                setBottomAnchor(DoubleMemory.valueOf(parentBounds.getHeight() - bounds.getMaxY() - borderInsets.getBottom()));
            } else {
                setBottomAnchor(Memory.NULL);
            }
        }

        getWrappedObject().setEffect(effect);
    }

    @Setter
    public void setAnchors(ArrayMemory value) {
        if (value.containsKey("left")) {
            setLeftAnchor(value.valueOfIndex("left"));
        }

        if (value.containsKey("top")) {
            setTopAnchor(value.valueOfIndex("top"));
        }

        if (value.containsKey("right")) {
            setRightAnchor(value.valueOfIndex("right"));
        }

        if (value.containsKey("bottom")) {
            setBottomAnchor(value.valueOfIndex("bottom"));
        }
    }

    @Setter
    public void setLeftAnchor(Memory v) {
        AnchorPane.setLeftAnchor(getWrappedObject(), v.isNull() ? null : v.toDouble());
    }

    @Getter
    public Memory getLeftAnchor() {
        Double anchor = AnchorPane.getLeftAnchor(getWrappedObject());
        return anchor == null ? Memory.NULL : DoubleMemory.valueOf(anchor);
    }

    @Setter
    public void setRightAnchor(Memory v) {
        AnchorPane.setRightAnchor(getWrappedObject(), v.isNull() ? null : v.toDouble());
    }

    @Getter
    public Memory getRightAnchor() {
        Double anchor = AnchorPane.getRightAnchor(getWrappedObject());
        return anchor == null ? Memory.NULL : DoubleMemory.valueOf(anchor);
    }

    @Setter
    public void setTopAnchor(Memory v) {
        AnchorPane.setTopAnchor(getWrappedObject(), v.isNull() ? null : v.toDouble());
    }

    @Getter
    public Memory getTopAnchor() {
        Double anchor = AnchorPane.getTopAnchor(getWrappedObject());
        return anchor == null ? Memory.NULL : DoubleMemory.valueOf(anchor);
    }

    @Setter
    public void setBottomAnchor(Memory v) {
        AnchorPane.setBottomAnchor(getWrappedObject(), v.isNull() ? null : v.toDouble());
    }

    @Getter
    public Memory getCursor(Environment env) {
        Cursor cursor = getWrappedObject().getCursor();

        if (cursor instanceof ImageCursor) {
            return ObjectMemory.valueOf(new UXImage(env, ((ImageCursor) cursor).getImage()));
        }

        return cursor == null ? Memory.NULL : StringMemory.valueOf(cursor.toString());
    }

    @Setter
    public void setCursor(Memory value) {
        try {
            if (value.instanceOf(UXImage.class)) {
                getWrappedObject().setCursor(new ImageCursor(value.toObject(UXImage.class).getWrappedObject()));
            } else {
                getWrappedObject().setCursor((Cursor) Cursor.class.getField(value.toString()).get(null));
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Invalid cursor - " + value);
        }
    }

    @Getter
    public Memory getBottomAnchor() {
        Double anchor = AnchorPane.getBottomAnchor(getWrappedObject());
        return anchor == null ? Memory.NULL : DoubleMemory.valueOf(anchor);
    }

    @Signature
    public Memory css(Environment env, Memory name) {
        if (name.isArray()) {
            ForeachIterator iterator = name.getNewIterator(env);

            while (iterator.next()) {
                css(iterator.getStringKey(), iterator.getValue());
            }

            return Memory.NULL;
        } else {
            String property = name.toString();
            String style = getWrappedObject().getStyle();

            Pattern pattern = Pattern.compile(
                    Pattern.quote(property) + "[ ]{0,}\\:[ ]{0,}(.+)\\;",
                    Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL
            );

            Matcher matcher = pattern.matcher(style);

            if (matcher.find()) {
                return StringMemory.valueOf(matcher.group(1));
            }

            return Memory.NULL;
        }
    }

    @Signature
    public Memory css(String property, Memory value) {
        String style = getWrappedObject().getStyle();

        Pattern pattern = Pattern.compile(
                Pattern.quote(property) + "[ ]{0,}\\:[ ]{0,}(.+)\\;",
                Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(style);

        if (matcher.find()) {
            matcher.reset();

            if (value.isNull()) {
                style = matcher.replaceFirst("");
            } else {
                style = matcher.replaceFirst(property + ": " + value + ";");
            }
        } else {
            if (value.isNull()) {
                return Memory.NULL;
            }

            style = style + "\n" + property + ": " + value + ";";
        }

        getWrappedObject().setStyle(style);

        return Memory.NULL;
    }

    @Signature
    public void applyCss() {
        getWrappedObject().applyCss();
    }

    @Signature
    public Memory data(String name) {
        return JavaFxUtils.data(getWrappedObject(), name);
    }

    @Signature
    public Memory data(Environment env, String name, Memory value) {
        return JavaFxUtils.data(env, getWrappedObject(), name, value);
    }

    @Signature
    public double[] screenToLocal(double x, double y) {
        Point2D pt = getWrappedObject().screenToLocal(x, y);

        if (pt == null) {
            return null;
        }

        return new double[]{pt.getX(), pt.getY()};
    }

    @Signature
    public void show() {
        getWrappedObject().setVisible(true);
    }

    @Signature
    public void hide() {
        getWrappedObject().setVisible(false);
    }

    @Signature
    public void toggle() {
        getWrappedObject().setVisible(!getWrappedObject().isVisible());
    }

    @Signature
    @SuppressWarnings("unchecked")
    public Memory lookup(Environment env, TraceInfo trace, String selector) throws Throwable {
        Node result = __globalLookup(getWrappedObject(), selector);

        if (result == null) {
            return null;
        }

        MemoryOperation operation = MemoryOperation.get(result.getClass(), null);

        if (operation == null) {
            return null;
        }

        return operation.unconvert(env, trace, result);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public Memory lookupAll(Environment env, TraceInfo trace, String selector) throws Throwable {
        Set<Node> result = getWrappedObject().lookupAll(selector);

        ArrayMemory r = new ArrayMemory();

        for (Node node : result) {
            Memory el = MemoryOperation.get(node.getClass(), null).unconvert(env, trace, node);
            r.add(el);
        }

        return r.toConstant();
    }

    @Getter(hiddenInDebugInfo = true)
    protected Memory getParent(Environment env) {
        return Memory.wrap(env, getWrappedObject().getParent());
    }

    @Getter(hiddenInDebugInfo = true)
    protected UXScene getScene(Environment env) {
        if (getWrappedObject().getScene() == null) {
            return null;
        }

        return new UXScene(env, getWrappedObject().getScene());
    }

    @Getter(hiddenInDebugInfo = true)
    protected UXForm getForm(Environment env) {
        Scene scene = getWrappedObject().getScene();
        if (scene == null) {
            return null;
        }

        Window window = scene.getWindow();

        if (window == null && scene.getUserData() instanceof Window) {
            window = (Window) scene.getUserData();
        }

        if (window instanceof Stage) {
            return new UXForm(env, (Stage) window);
        }

        return null;
    }

    @Getter(hiddenInDebugInfo = true)
    protected UXWindow getWindow(Environment env) {
        Scene scene = getWrappedObject().getScene();
        if (scene == null) {
            return null;
        }

        Window window = scene.getWindow();

        if (window == null && scene.getUserData() instanceof Window) {
            window = (Window) scene.getUserData();
        }

        if (window != null) {
            return new UXWindow(env, window);
        }

        return null;
    }

    @Signature
    public UXImage snapshot(Environment env) {
        SnapshotParameters snapParams = new SnapshotParameters();
        snapParams.setFill(Color.TRANSPARENT);

        WritableImage snapshot = getWrappedObject().snapshot(snapParams, null);

        return snapshot == null ? null : new UXImage(env, snapshot);
    }

    @Signature
    public UXDragboard startDrag(Environment env, List<TransferMode> modes) {
        Dragboard dragboard = getWrappedObject().startDragAndDrop(modes.toArray(new TransferMode[0]));

        return dragboard == null ? null : new UXDragboard(env, dragboard);
    }

    @Signature
    public ObservableValue observer(String property) {
        return JavaFxUtils.findObservable(this.getWrappedObject(), property);
    }

    @Signature
    public void watch(final String property, final Invoker invoker) throws InvocationTargetException, IllegalAccessException {
        String name = property + "Property";

        Class<? extends Node> aClass = getWrappedObject().getClass();

        try {
            Method method = aClass.getMethod(name);

            ReadOnlyProperty bindProperty = (ReadOnlyProperty) method.invoke(getWrappedObject());

            bindProperty.addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    invoker.callAny(UXNode.this, property, oldValue, newValue);
                }
            });
        } catch (NoSuchMethodException | ClassCastException e) {
            throw new IllegalArgumentException("Unable to find the '" + property + "' property for watching");
        }
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void on(String event, Invoker invoker, String group) {
        JavaFxUtils.on(getWrappedObject(), event, invoker, group);
    }

    @Signature
    public void on(String event, Invoker invoker) {
        JavaFxUtils.on(getWrappedObject(), event, invoker, "general");
    }

    @Signature
    public void off(String event, @Nullable String group) {
        JavaFxUtils.off(getWrappedObject(), event, group);
    }

    @Signature
    public void off(String event) {
        JavaFxUtils.off(getWrappedObject(), event);
    }

    @Signature
    public void trigger(String event, @Nullable Event e) {
        JavaFxUtils.trigger(getWrappedObject(), event, e);
    }

    @Signature
    public boolean isFree() {
        return getWrappedObject().getParent() == null;
    }

    @Signature
    public boolean free() {
        Parent parent = getWrappedObject().getParent();

        if (parent instanceof Pane) {
            return ((Pane) parent).getChildren().remove(getWrappedObject());
        }

        return false;
    }

    @Signature
    public Memory __get(Environment env, String name) throws Throwable {
        Memory data = data("--property-" + name + "-getter");

        if (data.isNotNull()) {
            Invoker invoker = Invoker.create(env, data);

            if (invoker != null) {
                return invoker.call();
            }
        }

        data = data("--property-" + name);
        return data;
    }

    @Signature
    public Memory __set(Environment env, String name, Memory value) throws Throwable {
        Memory data = data("--property-" + name + "-setter");

        if (data.isNotNull()) {
            Invoker invoker = Invoker.create(env, data);

            if (invoker != null) {
                invoker.call(value);
                return Memory.NULL;
            }
        }

        data(env, "--property-" + name, value);
        return Memory.NULL;
    }

    @Signature
    public Memory __isset(Environment env, String name) throws Throwable {
        if (data("--property-" + name).isNotNull()) {
            return Memory.TRUE;
        }

        // jphp hack
        CallStackItem callStackItem = env.peekCall(0);

        if (callStackItem != null) {
            callStackItem.classEntity = callStackItem.staticClassEntity;
            callStackItem.clazz = callStackItem.staticClazz;
        }

        Memory memory = getReflection().getProperty(env, env.trace(), this, name, null, 0);

        return memory.isNotNull() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public IObject __getOriginInstance() {
        Memory memory = JavaFxUtils.data(this.getWrappedObject(), "--node-instance");

        if (memory.isObject()) {
            return memory.toValue(ObjectMemory.class).value;
        }

        return super.__getOriginInstance();
    }

    @Override
    public int getPointer() {
        return getWrappedObject().hashCode();
    }

    public static Node __globalLookup(Node parent, String select) {
        if (parent == null) { // fix null pointer exceptio
            return null;
        }

        Node node = parent.lookup(select);

        if (node != null) {
            return node;
        }

        if (parent instanceof Parent) {
            ObservableList<Node> nodes = ((Parent) parent).getChildrenUnmodifiable();

            if (parent instanceof SplitPane) {
                nodes = ((SplitPane) parent).getItems();
            }

            for (Node nd : nodes) {
                if (nd instanceof TitledPane && ((TitledPane) nd).getContent() instanceof Parent) {
                    node = __globalLookup(((TitledPane) nd).getContent(), select);

                    if (node != null) {
                        return node;
                    }
                } else if (nd instanceof ScrollPane && ((ScrollPane) nd).getContent() instanceof Parent) {
                    node = __globalLookup(((ScrollPane) nd).getContent(), select);

                    if (node != null) {
                        return node;
                    }
                } else if (nd instanceof SplitPane) {
                    for (Node one : ((SplitPane) nd).getItems()) {
                        if (one instanceof Parent) {
                            node = __globalLookup(one, select);

                            if (node != null) {
                                return node;
                            }
                        }
                    }
                } else if (nd instanceof TabPane) {
                    for (Tab tab : ((TabPane) nd).getTabs()) {
                        if (tab.getContent() instanceof Parent) {
                            node = __globalLookup(tab.getContent(), select);

                            if (node != null) {
                                return node;
                            }
                        }
                    }
                } else if (nd instanceof Parent) {
                    Node n = __globalLookup(nd, select);

                    if (n != null) {
                        return n;
                    }
                }
            }
        }

        return null;
    }
}
