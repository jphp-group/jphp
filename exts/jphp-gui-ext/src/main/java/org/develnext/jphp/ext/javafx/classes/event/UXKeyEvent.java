package org.develnext.jphp.ext.javafx.classes.event;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "event\\UXKeyEvent")
public class UXKeyEvent extends UXEvent {
    interface WrappedInterface {
        @Property String character();
        @Property String text();

        @Property boolean shiftDown();
        @Property boolean controlDown();
        @Property boolean altDown();
        @Property boolean metaDown();
        @Property boolean shortcutDown();
    }

    public UXKeyEvent(Environment env, KeyEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXKeyEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(KeyEvent event, Object sender) {
        __wrappedObject = new KeyEvent(sender, null, event.getEventType(), event.getCharacter(), event.getText(), event.getCode(), event.isShiftDown(), event.isControlDown(), event.isAltDown(), event.isMetaDown());
    }

    @Override
    public KeyEvent getWrappedObject() {
        return (KeyEvent) super.getWrappedObject();
    }

    @Getter
    public String getCodeName() {
        return getWrappedObject().getCode().getName();
    }

    @Signature
    public boolean isUndefinedKey() {
        return getWrappedObject().getCode() == KeyCode.UNDEFINED;
    }

    @Signature
    public boolean isArrowKey() {
        return getWrappedObject().getCode().isArrowKey();
    }

    @Signature
    public boolean isDigitKey() {
        return getWrappedObject().getCode().isDigitKey();
    }

    @Signature
    public boolean isFunctionKey() {
        return getWrappedObject().getCode().isFunctionKey();
    }

    @Signature
    public boolean isKeypadKey() {
        return getWrappedObject().getCode().isKeypadKey();
    }

    @Signature
    public boolean isLetterKey() {
        return getWrappedObject().getCode().isLetterKey();
    }

    @Signature
    public boolean isMediaKey() {
        return getWrappedObject().getCode().isMediaKey();
    }

    @Signature
    public boolean isModifierKey() {
        return getWrappedObject().getCode().isModifierKey();
    }

    @Signature
    public boolean isNavigationKey() {
        return getWrappedObject().getCode().isNavigationKey();
    }

    @Signature
    public boolean isWhitespaceKey() {
        return getWrappedObject().getCode().isWhitespaceKey();
    }

    @Signature
    public boolean matches(KeyCombination accelerator) {
        if (accelerator == null) {
            return false;
        }

        return accelerator.match(getWrappedObject());
    }

}
