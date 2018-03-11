package org.develnext.jphp.ext.javafx.support.tray.animations;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.support.tray.models.CustomStage;
import javafx.util.Duration;

public class PopupAnimation implements TrayAnimation {

    private final Timeline showAnimation, dismissAnimation;
    private final SequentialTransition sq;
    private final CustomStage stage;
    private boolean trayIsShowing;

    public PopupAnimation(CustomStage s) {
        this.stage = s;

        showAnimation = setupShowAnimation();
        dismissAnimation = setupDismissAnimation();

        sq = new SequentialTransition(setupShowAnimation(), setupDismissAnimation());
    }

    private Timeline setupDismissAnimation() {
        Timeline tl = new Timeline();

        KeyValue kv1 = new KeyValue(stage.yLocationProperty(), stage.getY() + stage.getHeight());
        KeyFrame kf1 = new KeyFrame(Duration.millis(500), kv1);

        KeyValue kv2 = new KeyValue(stage.opacityProperty(), 0.0);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);

        tl.getKeyFrames().addAll(kf1, kf2);

        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                trayIsShowing = false;
                stage.hide();
                stage.setLocation(stage.getLocation());
            }
        });

        return tl;
    }

    private Timeline setupShowAnimation() {
        Timeline tl = new Timeline();

        KeyValue kv1 = new KeyValue(stage.yLocationProperty(), stage.getLocation().getY() + stage.getHeight());
        KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);

        KeyValue kv2 = new KeyValue(stage.yLocationProperty(), stage.getLocation().getY());
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);

        KeyValue kv3 = new KeyValue(stage.opacityProperty(), 0.0);
        KeyFrame kf3 = new KeyFrame(Duration.ZERO, kv3);

        KeyValue kv4 = new KeyValue(stage.opacityProperty(), 1.0);
        KeyFrame kf4 = new KeyFrame(Duration.millis(500), kv4);

        tl.getKeyFrames().addAll(kf1, kf2, kf3, kf4);

        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                trayIsShowing = true;
            }
        });

        return tl;
    }

    /**
     * The type of animation this class plays
     *
     * @return The type of animation this class plays
     */
    @Override
    public AnimationType getAnimationType() {
        return AnimationType.POPUP;
    }

    @Override
    public void setOnFinished(EventHandler<ActionEvent> onFinished) {
        sq.setOnFinished(onFinished);
    }

    /**
     * Plays both the show and dismiss animation using a sequential transition object
     *
     * @param dismissDelay How long to delay the start of the dismiss animation
     */
    @Override
    public void playSequential(Duration dismissDelay) {
        sq.getChildren().get(1).setDelay(dismissDelay);
        sq.play();
    }

    /**
     * Plays the implemented show animation
     */
    @Override
    public void playShowAnimation() {
        showAnimation.play();
    }

    /**
     * Plays the implemented dismiss animation
     */
    @Override
    public void playDismissAnimation() {
        dismissAnimation.play();
    }

    /**
     * Signifies if the tray is current showing
     *
     * @return boolean resultant
     */
    @Override
    public boolean isShowing() {
        return trayIsShowing;
    }
}