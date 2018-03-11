package org.develnext.jphp.ext.javafx.support.tray.animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.support.tray.models.CustomStage;

public class FadeAnimation implements TrayAnimation {

    private final Timeline showAnimation, dismissAnimation;
    private final SequentialTransition sq;
    private final CustomStage stage;
    private boolean trayIsShowing;

    /**
     * Initializes a fade type animation on a stage
     * @param customStage The stage associate the fade animation with
     */
    public FadeAnimation(CustomStage customStage) {

        this.stage = customStage;

        //It wouldn't allow me to play embedded animations so I had to create two separate
        //Instances so I could play sequentially and individually.

        showAnimation = setupShowAnimation();
        dismissAnimation = setupDismissAnimation();

        sq = new SequentialTransition(setupShowAnimation(), setupDismissAnimation());
    }

    /**
     *
     * @return a constructed instance of a show fade animation
     */
    private Timeline setupShowAnimation() {

        Timeline tl = new Timeline();

        //Sets opacity to 0.0 instantly which is pretty much invisible
        KeyValue kvOpacity = new KeyValue(stage.opacityProperty(), 0.0);
        KeyFrame frame1 = new KeyFrame(Duration.ZERO, kvOpacity);

        //Sets opacity to 1.0 (fully visible) over the time of 3000 milliseconds.
        KeyValue kvOpacity2 = new KeyValue(stage.opacityProperty(), 1.0);
        KeyFrame frame2 = new KeyFrame(Duration.millis(3000), kvOpacity2);

        tl.getKeyFrames().addAll(frame1, frame2);

        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                trayIsShowing = true;
            }
        });

        return tl;
    }

    /**
     *
     * @return a constructed instance of a dismiss fade animation
     */
    private Timeline setupDismissAnimation() {

        Timeline tl = new Timeline();

        //At this stage the opacity is already at 1.0

        //Lowers the opacity to 0.0 within 2000 milliseconds
        KeyValue kv1 = new KeyValue(stage.opacityProperty(), 0.0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(2000), kv1);

        tl.getKeyFrames().addAll(kf1);

        //Action to be performed when the animation has finished
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

    /**
     * The type of animation this class plays
     *
     * @return The type of animation this class plays
     */
    @Override
    public AnimationType getAnimationType() {
        return AnimationType.FADE;
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