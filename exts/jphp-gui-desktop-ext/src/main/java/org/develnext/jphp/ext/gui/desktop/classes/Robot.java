package org.develnext.jphp.ext.gui.desktop.classes;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import org.develnext.jphp.ext.gui.desktop.GuiDesktopExtension;
import org.develnext.jphp.ext.javafx.classes.UXScreen;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.event.KeyEvent.*;

@Reflection.Namespace(GuiDesktopExtension.NS)
public class Robot extends BaseWrapper<java.awt.Robot> {
    interface WrappedInterface {

    }

    public Robot(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Robot(Environment env, java.awt.Robot wrappedObject) {
        super(env, wrappedObject);
    }

    @Signature
    public void __construct() throws AWTException {
        __wrappedObject = new java.awt.Robot();
    }

    @Getter
    public int getX() {
        return Mouse.x();
    }

    @Setter
    public void setX(int x) {
        getWrappedObject().mouseMove(x, Mouse.y());
    }

    @Getter
    public int getY() {
        return Mouse.y();
    }

    @Setter
    public void setY(int y) {
        getWrappedObject().mouseMove(Mouse.x(), y);
    }

    @Getter
    public int[] getPosition() {
        return new int[] { getX(), getY() };
    }

    @Setter
    public void setPosition(int[] pos) {
        if (pos.length >= 2) {
            setX(pos[0]);
            setY(pos[1]);
        }
    }

    @Signature
    public void mouseClick() {
        mouseClick(MouseButton.PRIMARY);
    }

    @Signature
    public void mouseClick(MouseButton button) {
        mouseDown(button);
        mouseUp(button);
    }

    @Signature
    public void mouseDown() {
        mouseDown(MouseButton.PRIMARY);
    }

    @Signature
    public void mouseDown(MouseButton button) {
        switch (button) {
            case PRIMARY:
                getWrappedObject().mousePress(InputEvent.BUTTON1_MASK);
                break;
            case MIDDLE:
                getWrappedObject().mousePress(InputEvent.BUTTON2_MASK);
                break;
            case SECONDARY:
                getWrappedObject().mousePress(InputEvent.BUTTON3_MASK);
                break;
        }
    }

    @Signature
    public void mouseUp() {
        mouseUp(MouseButton.PRIMARY);
    }

    @Signature
    public void mouseUp(MouseButton button) {
        switch (button) {
            case PRIMARY:
                getWrappedObject().mouseRelease(InputEvent.BUTTON1_MASK);
                break;
            case MIDDLE:
                getWrappedObject().mouseRelease(InputEvent.BUTTON2_MASK);
                break;
            case SECONDARY:
                getWrappedObject().mouseRelease(InputEvent.BUTTON3_MASK);
                break;
        }
    }

    @Signature
    public void mouseScroll(int wheelAmt) {
        getWrappedObject().mouseWheel(wheelAmt);
    }

    @Signature
    public void type(String characters) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection( characters );
        clipboard.setContents(stringSelection, null);

        getWrappedObject().keyPress(KeyEvent.VK_CONTROL);
        getWrappedObject().keyPress(KeyEvent.VK_V);
        getWrappedObject().keyRelease(KeyEvent.VK_V);
        getWrappedObject().keyRelease(KeyEvent.VK_CONTROL);
    }

    protected List<Integer> keyCodes(String keys) {
        String[] strings = StringUtils.split(keys, '+');

        List<Integer> codes = new ArrayList<>();

        for (String string : strings) {
            string = string.trim().replace(' ', '_').replace('-', '_').toUpperCase();

            try {
                Field field = KeyEvent.class.getField("VK_" + string);
                int code = field.getInt(null);
                codes.add(code);
            } catch (NoSuchFieldException|IllegalAccessException e) {
                throw new IllegalArgumentException("Unknown key - " + string);
            }
        }

        if (codes.isEmpty()) {
            throw new IllegalArgumentException("Key combination cannot be empty");
        }

        return codes;
    }

    @Signature
    public void keyDown(String keys) {
        for (Integer code : keyCodes(keys)) {
            getWrappedObject().keyPress(code);
        }
    }

    @Signature
    public void keyUp(String keys) {
        for (Integer code : keyCodes(keys)) {
            getWrappedObject().keyRelease(code);
        }
    }

    @Signature
    public void keyPress(String keys) {
        List<Integer> codes = keyCodes(keys);

        for (Integer code : codes) {
            getWrappedObject().keyPress(code);
        }

        Collections.reverse(codes);

        for (Integer code : codes) {
            getWrappedObject().keyRelease(code);
        }
    }

    @Signature
    public Image screenshot() {
        return screenshot(null);
    }

    @Signature
    public Image screenshot(@Nullable Rectangle2D bounds) {
        return screenshot(bounds, null);
    }

    @Signature
    public Image screenshot(@Nullable Rectangle2D bounds, @Nullable Screen screen) {
        screen = screen == null ? Screen.getPrimary() : screen;
        bounds = bounds == null ? screen.getVisualBounds() : bounds;

        return screenshotArea((int) bounds.getMinX(), (int) bounds.getMinY(), (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    protected Image screenshotArea(int x, int y, int width, int height) {
        BufferedImage capture = getWrappedObject().createScreenCapture(new Rectangle(x, y, width, height));

        return SwingFXUtils.toFXImage(capture, null);
    }
}
