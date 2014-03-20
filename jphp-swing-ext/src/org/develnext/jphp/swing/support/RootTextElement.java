package org.develnext.jphp.swing.support;

import java.awt.*;
import java.awt.print.PrinterException;

public interface RootTextElement {

    void setText(String value);
    String getText();

    int getCaretPosition();
    void setCaretPosition(int pos);

    Color getCaretColor();
    void setCaretColor(Color color);

    boolean isEditable();
    void setEditable(boolean value);

    int getSelectionStart();
    void setSelectionStart(int value);

    int getSelectionEnd();
    void setSelectionEnd(int value);

    String getSelectedText();

    Color getSelectionColor();
    void setSelectionColor(Color color);

    Color getSelectedTextColor();
    void setSelectedTextColor(Color color);

    Color getDisabledTextColor();
    void setDisabledTextColor(Color color);

    void copy();
    void cut();
    void paste();
    void select(int start, int end);
    void selectAll();

    void replaceSelection(String content);

    boolean print() throws PrinterException;

    void setMargin(Insets insets);
    Insets getMargin();
}
