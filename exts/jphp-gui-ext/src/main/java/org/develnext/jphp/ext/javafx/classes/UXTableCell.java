package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXTableCell")
public class UXTableCell extends UXCell<TableCell> {
    interface WrappedInterface {
        void updateTableView(TableView tv);
        void updateTableColumn(TableColumn col);
    }

    public UXTableCell(Environment env, TableCell wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTableCell(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TableCell<>();
    }
}
