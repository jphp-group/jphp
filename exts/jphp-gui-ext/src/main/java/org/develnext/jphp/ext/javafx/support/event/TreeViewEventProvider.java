package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.TreeView;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class TreeViewEventProvider extends EventProvider<TreeView> {
    public Handler scrolltoHandler() {
        return new Handler() {
            @Override
            public void set(TreeView target, EventHandler eventHandler) {
                target.setOnScrollTo(eventHandler);
            }

            @Override
            public EventHandler get(TreeView target) {
                return target.getOnScrollTo();
            }
        };
    }

    public Handler editstartHandler() {
        return new Handler() {
            @Override
            public void set(TreeView target, EventHandler eventHandler) {
                target.setOnEditStart(eventHandler);
            }

            @Override
            public EventHandler get(TreeView target) {
                return target.getOnEditStart();
            }
        };
    }

    public Handler editcommitHandler() {
        return new Handler() {
            @Override
            public void set(TreeView target, EventHandler eventHandler) {
                target.setOnEditCommit(eventHandler);
            }

            @Override
            public EventHandler get(TreeView target) {
                return target.getOnEditCommit();
            }
        };
    }

    public Handler editcancelHandler() {
        return new Handler() {
            @Override
            public void set(TreeView target, EventHandler eventHandler) {
                target.setOnEditCancel(eventHandler);
            }

            @Override
            public EventHandler get(TreeView target) {
                return target.getOnEditCancel();
            }
        };
    }

    @Override
    public Class<TreeView> getTargetClass() {
        return TreeView.class;
    }
}
