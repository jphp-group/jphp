package org.develnext.jphp.ext.systemtray.event;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.PickResult;
import org.develnext.jphp.ext.javafx.support.EventProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TrayIconEventAdapter extends EventProvider<TrayIcon> {
    public TrayIconEventAdapter() {
        setHandler("click", new Handler() {
            @Override
            public void set(TrayIcon target, EventHandler eventHandler) {
                target.addMouseListener(new ClickEventAdapter(target, eventHandler));
            }

            @Override
            public EventHandler get(TrayIcon target) {
                for (MouseListener listener : target.getMouseListeners()) {
                    if (listener instanceof ClickEventAdapter) {
                        return ((ClickEventAdapter) listener).getEventHandler();
                    }
                }

                return null;
            }
        });

        setHandler("mouseMove", new Handler() {
            @Override
            public void set(TrayIcon target, EventHandler eventHandler) {
                target.addMouseListener(new MouseMoveEventAdapter(target, eventHandler));
            }

            @Override
            public EventHandler get(TrayIcon target) {
                for (MouseListener listener : target.getMouseListeners()) {
                    if (listener instanceof MouseMoveEventAdapter) {
                        return ((MouseEventAdapter) listener).getEventHandler();
                    }
                }

                return null;
            }
        });

        setHandler("mouseEnter", new Handler() {
            @Override
            public void set(TrayIcon target, EventHandler eventHandler) {
                target.addMouseListener(new MouseEnterEventAdapter(target, eventHandler));
            }

            @Override
            public EventHandler get(TrayIcon target) {
                for (MouseListener listener : target.getMouseListeners()) {
                    if (listener instanceof MouseEnterEventAdapter) {
                        return ((MouseEventAdapter) listener).getEventHandler();
                    }
                }

                return null;
            }
        });

        setHandler("mouseExit", new Handler() {
            @Override
            public void set(TrayIcon target, EventHandler eventHandler) {
                target.addMouseListener(new MouseExitEventAdapter(target, eventHandler));
            }

            @Override
            public EventHandler get(TrayIcon target) {
                for (MouseListener listener : target.getMouseListeners()) {
                    if (listener instanceof MouseExitEventAdapter) {
                        return ((MouseEventAdapter) listener).getEventHandler();
                    }
                }

                return null;
            }
        });
    }

    static protected javafx.scene.input.MouseEvent convertToJavaFx(Object target, EventType eventType, MouseEvent e) {
        MouseButton button = MouseButton.NONE;

        if (SwingUtilities.isLeftMouseButton(e)) {
            button = MouseButton.PRIMARY;
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            button = MouseButton.SECONDARY;
        }

        if (SwingUtilities.isMiddleMouseButton(e)) {
            button = MouseButton.MIDDLE;
        }

        return new javafx.scene.input.MouseEvent(target, null,
                eventType,
                    e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(),
                    button, e.getClickCount(), false, false, false, false,
                    button == MouseButton.PRIMARY, button == MouseButton.MIDDLE, button == MouseButton.SECONDARY,
                false, false, false, new PickResult(null, e.getXOnScreen(), e.getYOnScreen())
        );
    }

    @Override
    public Class<TrayIcon> getTargetClass() {
        return TrayIcon.class;
    }

    abstract static class MouseEventAdapter<T> extends MouseAdapter {
        protected final EventHandler eventHandler;
        protected final T target;

        protected MouseEventAdapter(T target, EventHandler eventHandler) {
            this.eventHandler = eventHandler;
            this.target = target;
        }

        public EventHandler getEventHandler() {
            return eventHandler;
        }
    }

    static class ClickEventAdapter extends MouseEventAdapter {
        protected ClickEventAdapter(Object target, EventHandler eventHandler) {
            super(target, eventHandler);
        }

        public void mouseClicked(MouseEvent e) {
            Platform.runLater(() -> eventHandler.handle(convertToJavaFx(target, javafx.scene.input.MouseEvent.MOUSE_CLICKED, e)));
        }
    }

    static class MouseMoveEventAdapter extends MouseEventAdapter {
        protected MouseMoveEventAdapter(Object target, EventHandler eventHandler) {
            super(target, eventHandler);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Platform.runLater(() -> eventHandler.handle(convertToJavaFx(target, javafx.scene.input.MouseEvent.MOUSE_MOVED, e)));
        }
    }


    static class MouseEnterEventAdapter extends MouseEventAdapter {
        protected MouseEnterEventAdapter(Object target, EventHandler eventHandler) {
            super(target, eventHandler);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Platform.runLater(() -> eventHandler.handle(convertToJavaFx(target, javafx.scene.input.MouseEvent.MOUSE_ENTERED, e)));
        }
    }


    static class MouseExitEventAdapter extends MouseEventAdapter {
        protected MouseExitEventAdapter(Object target, EventHandler eventHandler) {
            super(target, eventHandler);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Platform.runLater(() -> eventHandler.handle(convertToJavaFx(target, javafx.scene.input.MouseEvent.MOUSE_EXITED, e)));
        }
    }
}
