package org.develnext.jphp.ext.javafx.support.event;

import javafx.animation.Animation;
import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class AnimationEventProvider extends EventProvider<Animation> {
    public AnimationEventProvider() {
        setHandler("finish", new Handler() {
            @Override
            public void set(Animation target, EventHandler eventHandler) {
                target.setOnFinished(eventHandler);
            }

            @Override
            public EventHandler get(Animation target) {
                return target.getOnFinished();
            }
        });
    }

    @Override
    public Class<Animation> getTargetClass() {
        return Animation.class;
    }
}
