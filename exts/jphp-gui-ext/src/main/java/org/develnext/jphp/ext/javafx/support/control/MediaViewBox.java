package org.develnext.jphp.ext.javafx.support.control;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.layout.UXAnchorPane;

public class MediaViewBox extends Panel {
    private final MediaView mediaView;

    public MediaViewBox() {
        super();

        setBackgroundColor(Color.BLACK);
        setBorderWidth(0);

        mediaView = new MediaView();

        BorderPane borderPane = new BorderPane(mediaView);

        mediaView.fitWidthProperty().bind(borderPane.widthProperty());
        mediaView.fitHeightProperty().bind(borderPane.heightProperty());

        UXAnchorPane.setAnchor(borderPane, 0);

        getChildren().add(borderPane);
    }

    public boolean isProportional() {
        return mediaView.isPreserveRatio();
    }

    public void setProportional(boolean proportional) {
        this.mediaView.setPreserveRatio(proportional);
    }

    public boolean isSmooth() {
        return mediaView.isSmooth();
    }

    public void setSmooth(boolean proportional) {
        this.mediaView.setSmooth(proportional);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaView.getMediaPlayer();
    }

    public void setMediaPlayer(MediaPlayer player) {
        mediaView.setMediaPlayer(player);
    }
}
