package org.develnext.jphp.ext.javafx.classes;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.UserData;
import org.develnext.jphp.ext.javafx.support.control.DraggableTab;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXDraggableTab")
public class UXDraggableTab extends UXTab {
    interface WrappedInterface {
        @Property boolean disableDragFirst();
        @Property boolean disableDragLast();

        @Property boolean detachable();
        @Property boolean draggable();
        @Property("detachedForm") Stage detachedStage();
    }

    public UXDraggableTab(Environment env, DraggableTab wrappedObject) {
        super(env, wrappedObject);
    }

    public UXDraggableTab(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new DraggableTab("");
    }

    @Signature
    public void __construct(String title) {
        __wrappedObject = new DraggableTab(title);
    }

    @Signature
    public void __construct(String title, Node content) {
        __wrappedObject = new DraggableTab(title);
        __wrappedObject.setContent(content);
    }

    @Override
    public DraggableTab getWrappedObject() {
        return (DraggableTab) super.getWrappedObject();
    }

    @Getter
    @Override
    public String getText() {
        return getWrappedObject().getLabelText();
    }

    @Setter
    @Override
    public void setText(String value) {
        getWrappedObject().setLabelText(value);
    }

    @Getter
    @Override
    public Node getGraphic() {
        return getWrappedObject().getLabelGraphic();
    }

    @Setter
    @Override
    public void setGraphic(@Nullable Node node) {
        getWrappedObject().setLabelGraphic(node);
    }
}
