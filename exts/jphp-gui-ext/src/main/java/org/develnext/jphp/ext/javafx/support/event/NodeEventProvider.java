package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.Node;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class NodeEventProvider extends EventProvider<Node> {
    public Handler clickHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseClicked(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseClicked();
            }
        };
    }

    public Handler mousemoveHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseMoved(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseMoved();
            }
        };
    }

    public Handler mouseenterHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseEntered(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseEntered();
            }
        };
    }

    public Handler mouseexitHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseExited(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseExited();
            }
        };
    }

    public Handler mousedownHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMousePressed(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMousePressed();
            }
        };
    }

    public Handler mouseupHandler() {
        return  new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseReleased(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseReleased();
            }
        };
    }

    public Handler mousedragHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseDragged(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseDragged();
            }
        };
    }

    public Handler mousedragoverHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseDragOver(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseDragOver();
            }
        };
    }

    public Handler mousedragreleaseHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnMouseDragReleased(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnMouseDragReleased();
            }
        };
    }

    public Handler dragdetectHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnDragDetected(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnDragDetected();
            }
        };
    }

    public Handler dragoverHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnDragOver(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnDragOver();
            }
        };
    }

    public Handler dragdoneHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnDragDone(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnDragDone();
            }
        };
    }

    public Handler dragenterHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnDragEntered(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnDragEntered();
            }
        };
    }

    public Handler dragexitHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnDragExited(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnDragExited();
            }
        };
    }

    public Handler dragdropHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnDragDropped(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnDragDropped();
            }
        };
    }

    public Handler scrollHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnScroll(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnScroll();
            }
        };
    }

    public Handler scrollstartHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnScrollStarted(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnScrollStarted();
            }
        };
    }

    public Handler scrollfinishHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnScrollFinished(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnScrollFinished();
            }
        };
    }

    public Handler keypressHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnKeyTyped(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnKeyTyped();
            }
        };
    }

    public Handler keydownHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnKeyPressed(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnKeyPressed();
            }
        };
    }

    public Handler keyupHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnKeyReleased(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnKeyReleased();
            }
        };
    }

    public Handler touchdownHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnTouchPressed(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnTouchPressed();
            }
        };
    }

    public Handler touchmoveHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnTouchMoved(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnTouchMoved();
            }
        };
    }

    public Handler touchupHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnTouchReleased(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnTouchReleased();
            }
        };
    }

    public Handler swipedownHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeDown(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeDown();
            }
        };
    }

    public Handler swipeupHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeUp(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeUp();
            }
        };
    }

    public Handler swipeleftHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeLeft(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeLeft();
            }
        };
    }

    public Handler swiperightHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnSwipeRight(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnSwipeRight();
            }
        };
    }

    public Handler rotateHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnRotate(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnRotate();
            }
        };
    }

    public Handler rotatestartHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnRotationStarted(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnRotationStarted();
            }
        };
    }

    public Handler rotatefinishHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnRotationFinished(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnRotationFinished();
            }
        };
    }

    public Handler zoomHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnZoom(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnZoom();
            }
        };
    }

    public Handler zoomstartHandler() {
        return new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnZoomStarted(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnZoomStarted();
            }
        };
    }

    public Handler zoomfinishHandler() {
        return  new Handler() {
            @Override
            public void set(Node target, EventHandler eventHandler) {
                target.setOnZoomFinished(eventHandler);
            }

            @Override
            public EventHandler get(Node target) {
                return target.getOnZoomFinished();
            }
        };
    }

    @Override
    public Class<Node> getTargetClass() {
        return Node.class;
    }
}
