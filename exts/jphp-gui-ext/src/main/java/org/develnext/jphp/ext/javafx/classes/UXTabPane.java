package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.control.TabPaneEx;
import org.develnext.jphp.ext.javafx.support.control.tabpane.DndTabPane;
import org.develnext.jphp.ext.javafx.support.control.tabpane.DndTabPaneFactory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXTabPane")
public class UXTabPane extends UXControl<TabPane> {
    interface WrappedInterface {
        @Property Side side();
        @Property ObservableList<Tab> tabs();

        @Property double tabMaxWidth();
        @Property double tabMaxHeight();
        @Property double tabMinHeight();
        @Property double tabMinWidth();

        @Property TabPane.TabClosingPolicy tabClosingPolicy();
    }

    public UXTabPane(Environment env, TabPane wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTabPane(Environment env, DndTabPane wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTabPane(Environment env, TabPaneEx wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTabPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TabPaneEx();
    }

    @Signature
    public static Pane createDefaultDnDPane() {
        if (JavaFXExtension.isJigsaw()) {
            return null;
        }

        return DndTabPaneFactory.createDefaultDnDPane(DndTabPaneFactory.FeedbackType.MARKER, null);
    }

    @Getter
    protected Tab getSelectedTab() {
        ObservableList<Tab> tabs = getWrappedObject().getTabs();

        for (Tab tab : tabs) {
            if (tab.isSelected()) {
                return tab;
            }
        }

        return null;
    }

    @Getter
    public int getSelectedIndex() {
        return getWrappedObject().getSelectionModel().getSelectedIndex();
    }

    @Setter
    public void setSelectedIndex(int index) {
        getWrappedObject().getSelectionModel().select(index);
    }

    @Setter
    protected void setSelectedTab(Tab tab) {
        selectTab(tab);
    }

    @Signature
    public void selectTab(Tab tab) {
        getWrappedObject().getSelectionModel().select(tab);
    }

    @Signature
    public void selectFirstTab() {
        getWrappedObject().getSelectionModel().selectFirst();
    }

    @Signature
    public void selectNextTab() {
        getWrappedObject().getSelectionModel().selectNext();
    }

    @Signature
    public void selectLastTab() {
        getWrappedObject().getSelectionModel().selectLast();
    }

    @Signature
    public void selectPreviousTab() {
        getWrappedObject().getSelectionModel().selectPrevious();
    }

    @Signature
    public void clearSelection() {
        getWrappedObject().getSelectionModel().clearSelection();
    }
}
