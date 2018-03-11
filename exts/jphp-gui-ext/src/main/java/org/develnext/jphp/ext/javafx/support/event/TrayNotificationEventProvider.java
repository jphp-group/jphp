package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.tray.notification.TrayNotification;

public class TrayNotificationEventProvider extends EventProvider<TrayNotification> {
    public TrayNotificationEventProvider() {
        setHandler("click", new Handler() {
            @Override
            public void set(TrayNotification target, EventHandler eventHandler) {
                target.setOnClick(eventHandler);
            }

            @Override
            public EventHandler get(TrayNotification target) {
                return target.getOnClick();
            }
        });

        setHandler("show", new Handler() {
            @Override
            public void set(TrayNotification target, EventHandler eventHandler) {
                target.setOnShown(eventHandler);
            }

            @Override
            public EventHandler get(TrayNotification target) {
                return target.getOnShown();
            }
        });

        setHandler("hide", new Handler() {
            @Override
            public void set(TrayNotification target, EventHandler eventHandler) {
                target.setOnDismiss(eventHandler);
            }

            @Override
            public EventHandler get(TrayNotification target) {
                return target.getOnDismissed();
            }
        });
    }

    @Override
    public Class<TrayNotification> getTargetClass() {
        return TrayNotification.class;
    }
}
