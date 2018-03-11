package org.develnext.jphp.ext.javafx.support.tray.animations;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.support.tray.models.CustomStage;

public class SlideAnimation implements TrayAnimation {
    private final Timeline showAnimation, dismissAnimation;
    private final SequentialTransition sq;
    private final CustomStage stage;
    private boolean trayIsShowing;

    public SlideAnimation(CustomStage customStage) {

        this.stage = customStage;

        showAnimation = setupShowAnimation();
        dismissAnimation = setupDismissAnimation();

        //It wouldn't allow me to play embedded animations so I had to create two separate
        //Instances so I could play sequentially and individually.

        sq = new SequentialTransition(setupShowAnimation(), setupDismissAnimation());
    }

    private Timeline setupShowAnimation() {

        Timeline tl = new Timeline();

        //Sets the x location of the tray off the screen
        double offScreenX = stage.getOffScreenBounds().getX();
        KeyValue kvX = new KeyValue(stage.xLocationProperty(), offScreenX);
        KeyFrame frame1 = new KeyFrame(Duration.ZERO, kvX);

        //Animates the Tray onto the screen and interpolates at a tangent for 300 millis
        Interpolator interpolator = Interpolator.TANGENT(Duration.millis(300), 50);
        KeyValue kvInter = new KeyValue(stage.xLocationProperty(), stage.getLocation().getX(), interpolator);
        KeyFrame frame2 = new KeyFrame(Duration.millis(1300), kvInter);

        //Sets opacity to 0 instantly
        KeyValue kvOpacity = new KeyValue(stage.opacityProperty(), 0.0);
        KeyFrame frame3 = new KeyFrame(Duration.ZERO, kvOpacity);

        //Increases the opacity to fully visible whilst moving in the space of 1000 millis
        KeyValue kvOpacity2 = new KeyValue(stage.opacityProperty(), 1.0);
        KeyFrame frame4 = new KeyFrame(Duration.millis(1000), kvOpacity2);

        tl.getKeyFrames().addAll(frame1, frame2, frame3, frame4);

        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                trayIsShowing = true;
            }
        });

        return tl;
    }

    private Timeline setupDismissAnimation() {

        Timeline tl = new Timeline();

        double offScreenX = stage.getOffScreenBounds().getX();
        Interpolator interpolator = Interpolator.TANGENT(Duration.millis(300), 50);
        double trayPadding = 3;

        //The destination X location for the stage. Which is off the users screen
        //Since the tray has some padding, we want to hide that too
        KeyValue kvX = new KeyValue(stage.xLocationProperty(), offScreenX + trayPadding, interpolator);
        KeyFrame frame1 = new KeyFrame(Duration.millis(1400), kvX);

        //Change the opacity level to 0.4 over the duration of 2000 millis
        KeyValue kvOpacity = new KeyValue(stage.opacityProperty(), 0.4);
        KeyFrame frame2 = new KeyFrame(Duration.millis(2000), kvOpacity);

        tl.getKeyFrames().addAll(frame1, frame2);

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
        return AnimationType.SLIDE;
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