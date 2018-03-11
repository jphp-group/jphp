package org.develnext.jphp.ext.javafx.support.tray.animations;

import php.runtime.common.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A provider class that houses all animation objects that implement TrayAnimation
 * This makes it easier to lookup any existing animations and retrieve them
 * This means the user can easily switch animations, and in the background all possible animation types
 * Are stores in here, so when a user selects an animation type, this class will return the TrayAnimation that satisfies that request
 * It's much like a mini database
 */
public class AnimationProvider {

    private List<TrayAnimation> animationsList;

    public AnimationProvider(TrayAnimation... animations) {
        animationsList = new ArrayList<>();
        Collections.addAll(animationsList, animations);
    }

    public void addAll(TrayAnimation... animations) {
        Collections.addAll(animationsList, animations);
    }

    public TrayAnimation get(int index) {
        return animationsList.get(index);
    }

    public TrayAnimation findFirstWhere(Callback<Boolean, ? super TrayAnimation> predicate) {
        for (TrayAnimation trayAnimation : animationsList) {
            if (predicate.call(trayAnimation)) {
                return trayAnimation;
            }
        }

        return null;
    }
}