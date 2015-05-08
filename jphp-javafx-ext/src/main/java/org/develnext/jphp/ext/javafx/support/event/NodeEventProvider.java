package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.Node;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class NodeEventProvider extends EventProvider<Node> {
    public NodeEventProvider() {
        // Mouse Events
        setHandler("click", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseClicked(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseClicked();
            }
        });

        setHandler("mouseMove", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseMoved(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseMoved();
            }
        });

        setHandler("mouseEnter", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseEntered(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseEntered();
            }
        });

        setHandler("mouseExit", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseExited(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseExited();
            }
        });

        setHandler("mouseDown", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMousePressed(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMousePressed();
            }
        });

        setHandler("mouseUp", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseReleased(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseReleased();
            }
        });

        // Scroll Events

        setHandler("scroll", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnScroll(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnScroll();
            }
        });

        setHandler("scrollStart", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnScrollStarted(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnScrollStarted();
            }
        });

        setHandler("scrollFinish", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnScrollFinished(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnScrollFinished();
            }
        });

        // Key Events

        setHandler("keyPress", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnKeyTyped(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnKeyTyped();
            }
        });

        setHandler("keyDown", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnKeyPressed(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnKeyPressed();
            }
        });

        setHandler("keyUp", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnKeyReleased(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnKeyReleased();
            }
        });

        // Touch Events
        setHandler("touchDown", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnTouchPressed(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnTouchPressed();
            }
        });

        setHandler("touchMove", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnTouchMoved(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnTouchMoved();
            }
        });

        setHandler("touchUp", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnTouchReleased(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnTouchReleased();
            }
        });

        // Swipe Events
        setHandler("swipeDown", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeDown(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeDown();
            }
        });

        setHandler("swipeUp", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeUp(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeUp();
            }
        });

        setHandler("swipeLeft", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeLeft(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeLeft();
            }
        });

        setHandler("swipeRight", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeRight(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeRight();
            }
        });

        // Rotate
        setHandler("rotate", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnRotate(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnRotate();
            }
        });

        setHandler("rotateStart", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnRotationStarted(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnRotationStarted();
            }
        });

        setHandler("rotateFinish", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnRotationFinished(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnRotationFinished();
            }
        });

        // Zoom
        setHandler("zoom", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnZoom(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnZoom();
            }
        });

        setHandler("zoomStart", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnZoomStarted(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnZoomStarted();
            }
        });

        setHandler("zoomFinish", new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnZoomFinished(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnZoomFinished();
            }
        });
    }

    @Override
    public Class<Node> getTargetClass() {
        return Node.class;
    }
}
