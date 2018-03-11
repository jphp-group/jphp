package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.Callback;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NS + "UXParent")
public class UXParent<T extends Parent> extends UXNode<Parent> {
    interface WrappedInterface {
        void layout();
        void requestLayout();

        @Property(hiddenInDebugInfo = true) ObservableList stylesheets();
        @Property(hiddenInDebugInfo = true) ObservableList childrenUnmodifiable();
    }

    public UXParent(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXParent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    public Node findNode(final Environment env, final Invoker filter) {
        return __globalScan(getWrappedObject(), new Callback<Boolean, Node>() {
            @Override
            public Boolean call(Node param) {
                try {
                    return filter.callAny(param).toBoolean();
                } catch (Throwable throwable) {
                    env.wrapThrow(throwable);
                    return false;
                }
            }
        });
    }

    @Signature
    public Node findFocusedNode() {
        return __globalScan(getWrappedObject(), new Callback<Boolean, Node>() {
            @Override
            public Boolean call(Node param) {
                return param.isFocused();
            }
        });
    }

    public static Node __globalScan(Node parent, Callback<Boolean, Node> filter) {
        Node node = null;

        if (parent instanceof Parent) {
            ObservableList<Node> nodes = ((Parent) parent).getChildrenUnmodifiable();

            for (Node nd : nodes) {
                if (filter != null && filter.call(nd)) {
                    return nd;
                }

                if (nd instanceof TitledPane && ((TitledPane) nd).getContent() instanceof Parent) {
                    node = __globalScan(((TitledPane) nd).getContent(), filter);

                    if (node != null) {
                        return node;
                    }
                } else if (nd instanceof ScrollPane && ((ScrollPane) nd).getContent() instanceof Parent) {
                    node = __globalScan(((ScrollPane) nd).getContent(), filter);

                    if (node != null) {
                        return node;
                    }
                } else if (nd instanceof TabPane) {
                    for (Tab tab : ((TabPane) nd).getTabs()) {
                        if (tab.getContent() instanceof Parent) {
                            node = __globalScan(tab.getContent(), filter);

                            if (node != null) {
                                return node;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
