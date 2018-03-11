package org.develnext.jphp.ext.game.support.event;

import javafx.event.EventHandler;
import org.develnext.jphp.ext.game.support.GamePane;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class GamePaneEventProvider extends EventProvider<GamePane> {
    public GamePaneEventProvider() {
        setHandler("scrollScene", new Handler() {
            @Override
            public void set(GamePane target, EventHandler eventHandler) {
                target.setOnScrollScene(eventHandler);
            }

            @Override
            public EventHandler get(GamePane target) {
                return target.getOnScrollScene();
            }
        });
    }

    @Override
    public Class<GamePane> getTargetClass() {
        return GamePane.class;
    }
}
