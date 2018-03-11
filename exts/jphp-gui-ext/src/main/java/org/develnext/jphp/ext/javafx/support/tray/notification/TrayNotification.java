package org.develnext.jphp.ext.javafx.support.tray.notification;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.support.tray.animations.*;
import org.develnext.jphp.ext.javafx.support.tray.models.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class TrayNotification {
    @FXML
    private Label lblTitle, lblMessage, lblClose;

    @FXML
    private ImageView imageIcon;

    @FXML
    private Rectangle rectangleColor;

    @FXML
    private AnchorPane rootNode;

    private static Stage tmpStage;

    private CustomStage stage;
    private NotificationType notificationType;
    private AnimationType animationType;
    private EventHandler<ActionEvent> onDismissedCallBack, onShownCallback, onClickCallback;
    private NotificationLocation location;

    private double horGap = 5;
    private double verGap = 5;

    private int viewIndex = 0;
    private TrayAnimation __animator;

    private static Map<NotificationLocation, AtomicInteger> trayCounts = new HashMap<>();

    /**
     * Initializes an instance of the tray notification object
     * @param title The title text to assign to the tray
     * @param body The body text to assign to the tray
     * @param img The image to show on the tray
     * @param rectangleFill The fill for the rectangle
     */
    public TrayNotification(String title, String body, Image img, Paint rectangleFill) {
        initTrayNotification(title, body, NotificationType.CUSTOM);

        setImage(img);
        setRectangleFill(rectangleFill);
    }

    /**
     * Initializes an instance of the tray notification object
     * @param title The title text to assign to the tray
     * @param body The body text to assign to the tray
     * @param notificationType The notification type to assign to the tray
     */
    public TrayNotification(String title, String body, NotificationType notificationType ) {
        initTrayNotification(title, body, notificationType);
    }

    /**
     * Initializes an empty instance of the tray notification
     */
    public TrayNotification() {
        initTrayNotification("", "", NotificationType.CUSTOM);
    }

    private void initTrayNotification(String title, String message, NotificationType type) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tray/views/TrayNotification.fxml"));

            fxmlLoader.setController(this);
            fxmlLoader.load();

            initStage();
            initAnimations();

            setTray(title, message, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAnimations() {
        //Default animation type
        setAnimationType(AnimationType.POPUP);
    }

    private void initStage() {
        stage = new CustomStage(rootNode, horGap, verGap);
        rootNode.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        stage.getScene().setRoot(rootNode);

        setLocation(NotificationLocation.BOTTOM_RIGHT);

        EventHandler<MouseEvent> value = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (onClickCallback != null) {
                    onClickCallback.handle(new ActionEvent(this, null));
                }

                dismiss();
            }
        };
        lblClose.setOnMouseClicked(value);
        stage.getScene().setOnMouseClicked(value);
    }

    public NotificationLocation getLocation() {
        return location;
    }

    public void setLocation(NotificationLocation location) {
        stage.setHorGap(horGap);
        stage.setVerGap(verGap);

        switch (location) {
            case BOTTOM_RIGHT:
                stage.setLocation(stage.getBottomRight(viewIndex));
                break;
            case BOTTOM_LEFT:
                stage.setLocation(stage.getBottomLeft(viewIndex));
                break;
            case TOP_LEFT:
                stage.setLocation(stage.getTopLeft(viewIndex));
                break;
            case TOP_RIGHT:
                stage.setLocation(stage.getTopRight(viewIndex));
                break;
        }

        this.location = location;
    }

    public void setNotificationType(NotificationType nType) {

        notificationType = nType;

        URL imageLocation = null;
        String paintHex = null;

        switch (nType) {

            case INFORMATION:
                imageLocation = getClass().getResource("/tray/resources/info.png");
                paintHex = "#2C54AB";
                break;

            case NOTICE:
                imageLocation = getClass().getResource("/tray/resources/notice.png");
                paintHex = "#8D9695";
                break;

            case SUCCESS:
                imageLocation = getClass().getResource("/tray/resources/success.png");
                paintHex = "#009961";
                break;

            case WARNING:
                imageLocation = getClass().getResource("/tray/resources/warning.png");
                paintHex = "#E23E0A";
                break;

            case ERROR:
                imageLocation = getClass().getResource("/tray/resources/error.png");
                paintHex = "#CC0033";
                break;

            case CUSTOM:
                return;
        }

        setRectangleFill(Paint.valueOf(paintHex));
        setImage(new Image(imageLocation.toString()));
        setTrayIcon(imageIcon.getImage());
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setTray(String title, String message, NotificationType type) {
        setTitle(title);
        setMessage(message);
        setNotificationType(type);
    }

    public void setTray(String title, String message, Image img, Paint rectangleFill, AnimationType animType) {
        setTitle(title);
        setMessage(message);
        setImage(img);
        setRectangleFill(rectangleFill);
        setAnimationType(animType);
    }

    public TrayAnimation animator() {
        if (__animator == null || !__animator.isShowing()) {
            switch (animationType) {
                case FADE:
                    __animator = new FadeAnimation(stage);
                    break;
                case POPUP:
                    __animator = new PopupAnimation(stage);
                    break;
                case SLIDE:
                    __animator = new SlideAnimation(stage);
                    break;
            }
        }

        if (__animator != null) {
            __animator.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    onDismissed();
                }
            });
        }

        return __animator;
    }

    public boolean isTrayShowing() {
        return animator().isShowing();
    }

    private Stage getTmpStage() {
        if (tmpStage == null) {
            tmpStage = new Stage(StageStyle.UTILITY);
            tmpStage.setWidth(2);
            tmpStage.setHeight(2);
            tmpStage.setX(-999);
            tmpStage.setY(-999);
            tmpStage.show();
        }

        return tmpStage;
    }

    /**
     * Shows and dismisses the tray notification
     * @param dismissDelay How long to delay the start of the dismiss animation
     */
    public void showAndDismiss(Duration dismissDelay) {
        if (isTrayShowing()) {
            dismiss();
        } else {
            onShown();
            setLocation(getLocation());

            stage.show(getTmpStage());

            animator().playSequential(dismissDelay);
        }
    }

    /**
     * Displays the notification tray
     */
    public void showAndWait() {
        if (!isTrayShowing()) {
            onShown();

            setLocation(getLocation());

            stage.show(getTmpStage());

            animator().playShowAnimation();
        }
    }

    /**
     * Dismisses the notifcation tray
     */
    public void dismiss() {
        if (isTrayShowing()) {
            animator().playDismissAnimation();
            //onDismissed();
        }
    }

    public int getViewIndex() {
        return viewIndex;
    }

    private void onShown() {
        if (onShownCallback != null)
            onShownCallback.handle(new ActionEvent(this, null));

        synchronized (this) {
            AtomicInteger count = trayCounts.get(location);

            if (count == null) {
                count = new AtomicInteger();
                trayCounts.put(location, count);
            }

            viewIndex = count.getAndIncrement();
        }
    }

    private void onDismissed() {
        if (onDismissedCallBack != null)
            onDismissedCallBack.handle(new ActionEvent(this, null));

        AtomicInteger count = trayCounts.get(location);

        if (count != null) {
            count.decrementAndGet();
        }

        viewIndex = 0;
    }

    /**
     * Sets an action event for when the tray has been dismissed
     * @param event The event to occur when the tray has been dismissed
     */
    public void setOnDismiss(EventHandler<ActionEvent> event) {
        onDismissedCallBack  = event;
    }

    public EventHandler<ActionEvent> getOnDismissed() {
        return onDismissedCallBack;
    }

    /**
     * Sets an action event for when the tray has been shown
     * @param event The event to occur after the tray has been shown
     */
    public void setOnShown(EventHandler<ActionEvent> event) {
        onShownCallback  = event;
    }

    public EventHandler<ActionEvent> getOnShown() {
        return onShownCallback;
    }

    public EventHandler<ActionEvent> getOnClick() {
        return onClickCallback;
    }

    public void setOnClick(EventHandler<ActionEvent> onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    /**
     * Sets a new task bar image for the tray
     * @param img The image to assign
     */
    public void setTrayIcon(Image img) {
        /*stage.getIcons().clear();
        stage.getIcons().add(img);*/
    }

    public Image getTrayIcon() {
        return null; // stage.getIcons().get(0);
    }

    /**
     * Sets a title to the tray
     * @param txt The text to assign to the tray icon
     */
    public void setTitle(String txt) {
        lblTitle.setText(txt);
        //stage.setTitle(txt);
    }

    public String getTitle() {
        return lblTitle.getText();
    }

    /**
     * Sets the message for the tray notification
     * @param txt The text to assign to the body of the tray notification
     */
    public void setMessage(String txt) {
        lblMessage.setText(txt);
    }

    public String getMessage() {
        return lblMessage.getText();
    }

    public void setImage (Image img) {
        imageIcon.setImage(img);

        setTrayIcon(img);
    }

    public Image getImage() {
        return imageIcon.getImage();
    }

    public void setRectangleFill(Paint value) {
        rectangleColor.setFill(value);
    }

    public Paint getRectangleFill() {
        return rectangleColor.getFill();
    }

    public void setAnimationType(final AnimationType type) {
        animationType = type;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public double getHorGap() {
        return horGap;
    }

    public void setHorGap(double horGap) {
        this.horGap = horGap;
        setLocation(getLocation());
    }

    public double getVerGap() {
        return verGap;
    }

    public void setVerGap(double verGap) {
        this.verGap = verGap;
        setLocation(getLocation());
    }
}