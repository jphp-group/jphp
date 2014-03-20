package org.develnext.jphp.swing.support;

import javax.swing.*;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.print.PrinterException;

public class JTextAreaX extends JScrollableComponent<JTextArea> implements RootTextElement {

    @Override
    protected JTextArea newComponent() {
        return new JTextArea();
    }

    @Override
    public void setText(String value) {
        component.setText(value);
    }

    @Override
    public String getText() {
        return component.getText();
    }

    @Override
    public int getCaretPosition() {
        return component.getCaretPosition();
    }

    @Override
    public void setCaretPosition(int pos) {
        component.setCaretPosition(pos);
    }

    @Override
    public Color getCaretColor() {
        return component.getCaretColor();
    }

    @Override
    public void setCaretColor(Color color) {
        component.setCaretColor(color);
    }

    @Override
    public boolean isEditable() {
        return component.isEditable();
    }

    @Override
    public void setEditable(boolean value) {
        component.setEditable(value);
    }

    @Override
    public int getSelectionStart() {
        return component.getSelectionStart();
    }

    @Override
    public void setSelectionStart(int value) {
        component.setSelectionStart(value);
    }

    @Override
    public int getSelectionEnd() {
        return component.getSelectionEnd();
    }

    @Override
    public void setSelectionEnd(int value) {
        component.setSelectionEnd(value);
    }

    @Override
    public String getSelectedText() {
        return component.getSelectedText();
    }

    @Override
    public Color getSelectionColor() {
        return component.getSelectionColor();
    }

    @Override
    public void setSelectionColor(Color color) {
        component.setSelectionColor(color);
    }

    @Override
    public Color getSelectedTextColor() {
        return component.getSelectedTextColor();
    }

    @Override
    public void setSelectedTextColor(Color color) {
        component.setSelectedTextColor(color);
    }

    @Override
    public Color getDisabledTextColor() {
        return component.getDisabledTextColor();
    }

    @Override
    public void setDisabledTextColor(Color color) {
        component.setDisabledTextColor(color);
    }

    @Override
    public void copy() {
        component.copy();
    }

    @Override
    public void cut() {
        component.cut();
    }

    @Override
    public void paste() {
        component.paste();
    }

    @Override
    public void select(int start, int end) {
        component.select(start, end);
    }

    @Override
    public void selectAll() {
        component.selectAll();
    }

    @Override
    public void replaceSelection(String content) {
        component.replaceSelection(content);
    }

    @Override
    public boolean print() throws PrinterException {
        return component.print();
    }

    @Override
    public void setMargin(Insets insets) {
        component.setMargin(insets);
    }

    @Override
    public Insets getMargin() {
        return component.getMargin();
    }

    public void addCaretListener(CaretListener listener) {
        component.addCaretListener(listener);
    }
}
