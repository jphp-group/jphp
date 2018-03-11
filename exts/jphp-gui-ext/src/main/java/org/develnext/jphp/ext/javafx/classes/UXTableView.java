package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import javax.swing.table.TableColumn;
import java.util.List;

@Name(JavaFXExtension.NS + "UXTableView")
public class UXTableView extends UXControl<TableView> {
    interface WrappedInterface {
        @Property boolean editable();
        @Property ObservableList items();
        @Property ObservableList<TableColumn> columns();
        @Property double fixedCellSize();
        @Property boolean tableMenuButtonVisible();

        @Property @Nullable Node placeholder();
    }

    public UXTableView(Environment env, TableView wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTableView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TableView<>();
        getWrappedObject().setPlaceholder(new Label(""));
    }

    @Signature
    public void update() {
        ObservableList items = getWrappedObject().getItems();

        getWrappedObject().setItems(null);
        getWrappedObject().setItems(items);

        getWrappedObject().setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @Getter
    public boolean getConstrainedResizePolicy() {
        return getWrappedObject().getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY;
    }

    @Setter
    public void setConstrainedResizePolicy(boolean value) {
        getWrappedObject().setColumnResizePolicy(value ? TableView.CONSTRAINED_RESIZE_POLICY : TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    @Getter
    public boolean getMultipleSelection() {
        return getWrappedObject().getSelectionModel().getSelectionMode() == SelectionMode.MULTIPLE;
    }

    @Setter
    public void setMultipleSelection(boolean value) {
        getWrappedObject().getSelectionModel().setSelectionMode(value ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
    }

    @Getter
    public List<Integer> getSelectedIndexes() {
        return getWrappedObject().getSelectionModel().getSelectedIndices();
    }

    @Setter
    public void setSelectedIndexes(int[] indexes) {
        MultipleSelectionModel selectionModel = getWrappedObject().getSelectionModel();

        selectionModel.clearSelection();

        for (int index : indexes) {
            selectionModel.select(index);
        }
    }

    @Getter
    public int getSelectedIndex() {
        return getWrappedObject().getSelectionModel().getSelectedIndex();
    }

    @Setter
    public void setSelectedIndex(int index) {
        MultipleSelectionModel selectionModel = getWrappedObject().getSelectionModel();

        selectionModel.clearSelection();
        selectionModel.select(index);
    }

    @Getter
    public Object getFocusedItem() {
        return getWrappedObject().getFocusModel().getFocusedItem();
    }

    @Getter
    public List<Object> getSelectedItems() {
        return getWrappedObject().getSelectionModel().getSelectedItems();
    }

    @Getter
    public Object getSelectedItem() {
        ObservableList selectedItems = getWrappedObject().getSelectionModel().getSelectedItems();
        return selectedItems.isEmpty() ? null : selectedItems.get(0);
    }

    @Getter
    public Object getFocusedIndex() {
        return getWrappedObject().getFocusModel().getFocusedIndex();
    }

    @Setter
    public void setFocusedIndex(int index) {
        getWrappedObject().getFocusModel().focus(index);
    }

}
