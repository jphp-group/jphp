package org.develnext.jphp.ext.javafx.support.event;

import javafx.scene.media.MediaPlayer;
import org.develnext.jphp.ext.javafx.support.EventProvider;

// TODO
public class MediaPlayerEventProvider extends EventProvider<MediaPlayer> {
    public MediaPlayerEventProvider() {
    }

    @Override
    public Class<MediaPlayer> getTargetClass() {
        return MediaPlayer.class;
    }
}
