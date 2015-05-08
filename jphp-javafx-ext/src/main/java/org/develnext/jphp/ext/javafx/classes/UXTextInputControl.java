package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.TextInputControl;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NAMESPACE + "UXTextInputControl")
public class UXTextInputControl extends UXControl {
    interface WrappedInterface {
        @Property int anchor();
        @Property int caretPosition();
        @Property int length();
        @Property String promptText();
        @Property String selectedText();
        @Property String text();

        @Property boolean editable();

        void appendText(String text);
        void backward();
        void clear();
        void copy();
        void cut();

        void deselect();

        void end();
        void endOfNextWord();

        void extendSelection(int pos);
        void forward();
        void home();

        void insertText(int index, String text);
        void replaceSelection(String replacement);
        void replaceText(int start, int end, String text);

        void paste();
        void positionCaret(int pos);

        void nextWord();
        void previousWord();
        void selectAll();
        void selectBackward();
        void selectEnd();
        void selectEndOfNextWord();
        void selectForward();
        void selectHome();
        void selectNextWord();
        void selectPositionCaret(int pos);
        void selectPreviousWord();
        void selectRange(int anchor, int caretPosition);
    }

    public UXTextInputControl(Environment env, TextInputControl wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTextInputControl(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public TextInputControl getWrappedObject() {
        return (TextInputControl) super.getWrappedObject();
    }
}
