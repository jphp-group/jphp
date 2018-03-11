package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "UXTreeView")
public class UXTreeView extends UXControl<TreeView> {
    interface WrappedInterface {
        @Property TreeItem root();
        @Property TreeItem editingItem();
        @Property int expandedItemCount();
        @Property double fixedCellSize();

        @Property boolean editable();
        @Property("rootVisible") boolean showRoot();

        TreeItem getTreeItem(int row);
        int getTreeItemLevel(TreeItem item);

        void edit(TreeItem item);
    }

    public UXTreeView(Environment env, TreeView wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTreeView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TreeView<>();
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

        for (int index : indexes) {
            selectionModel.select(index);
        }
    }

    @Getter
    public List<TreeItem> getSelectedItems() {
        return getWrappedObject().getSelectionModel().getSelectedItems();
    }

    @Setter
    public void setSelectedItems(List<TreeItem> items) {
        MultipleSelectionModel selectionModel = getWrappedObject().getSelectionModel();
        selectionModel.clearSelection();

        for (TreeItem item : items) {
            selectionModel.select(item);
        }
    }

    private void _getExpandedItems(Environment env, TreeItem item, ArrayMemory result) {
        if (item.isExpanded()) {
            result.add(new UXTreeItem(env, item));
        }

        ObservableList<TreeItem> children = item.getChildren();

        for (TreeItem one : children) {
            _getExpandedItems(env, one, result);
        }
    }

    @Getter
    public Memory getExpandedItems(Environment env) {
        TreeItem root = getWrappedObject().getRoot();

        ArrayMemory result = new ArrayMemory();
        if (root != null) {
            _getExpandedItems(env, root, result);
        }

        return result;
    }

    @Getter
    public TreeItem getFocusedItem() {
        return (TreeItem) getWrappedObject().getFocusModel().getFocusedItem();
    }

    @Setter
    public void setFocusedItem(@Nullable TreeItem item) {
        getWrappedObject().getFocusModel().focus(item == null ? -1 : getWrappedObject().getRow(item));
    }

    @Signature
    public boolean isTreeItemFocused(TreeItem item) {
        return getWrappedObject().getFocusModel().isFocused(getWrappedObject().getRow(item));
    }

    @Signature
    public int getTreeItemIndex(TreeItem item) {
        return getWrappedObject().getRow(item);
    }

    @Signature
    public void scrollTo(TreeItem item) {
        getWrappedObject().scrollTo(getWrappedObject().getRow(item));
    }

    private void expandTreeView(TreeItem<?> item){
        if(item != null && !item.isLeaf()){
            item.setExpanded(true);
            for(TreeItem<?> child:item.getChildren()){
                expandTreeView(child);
            }
        }
    }

    private void collapseTreeView(TreeItem<?> item){
        if(item != null && !item.isLeaf()){
            item.setExpanded(false);
            for(TreeItem<?> child:item.getChildren()){
                collapseTreeView(child);
            }
        }
    }

    @Signature
    public void collapseAll() {
        TreeItem root = getWrappedObject().getRoot();

        if (root != null) {
            collapseTreeView(root);
        }
    }

    @Signature
    public void expandAll() {
        TreeItem root = getWrappedObject().getRoot();

        if (root != null) {
            expandTreeView(root);
        }
    }
}
