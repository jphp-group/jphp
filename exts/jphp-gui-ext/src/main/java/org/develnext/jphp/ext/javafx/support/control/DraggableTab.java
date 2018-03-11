package org.develnext.jphp.ext.javafx.support.control;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.develnext.jphp.ext.javafx.classes.UXImage;
import org.develnext.jphp.ext.javafx.support.ImageViewEx;


public class DraggableTab extends Tab {
    private static final Set<TabPane> tabPanes = new HashSet<>();
    private Label nameLabel;
    private Text dragText;
    private static final Stage markerStage;
    private Stage dragStage;
    private boolean detachable;
    private boolean draggable = true;

    private Stage detachedStage = null;

    private boolean disableDragFirst;
    private boolean disableDragLast;

    private ImageViewEx nameLabelSnapshot = new ImageViewEx();

    static {
        markerStage = new Stage();
        markerStage.initStyle(StageStyle.UNDECORATED);
        Rectangle dummy = new Rectangle(3, 10, Color.web("#555555"));
        StackPane markerStack = new StackPane();
        markerStack.getChildren().add(dummy);
        markerStage.setScene(new Scene(markerStack));
    }

    public DraggableTab(String text) {
        nameLabel = new Label(text);
        updateGraphic();

        tabPaneProperty().addListener(new ChangeListener<TabPane>() {
            @Override
            public void changed(ObservableValue<? extends TabPane> observable, TabPane oldValue, final TabPane newValue) {
                if (newValue == null) {
                    return;
                }

                newValue.getTabs().addListener(new ListChangeListener<Tab>() {
                    @Override
                    public void onChanged(Change<? extends Tab> c) {
                        ObservableList<Tab> tabs = newValue.getTabs();

                        for (Tab tab : tabs) {
                            if (tab instanceof DraggableTab) {
                                ((DraggableTab) tab).updateGraphic();
                            }
                        }
                    }
                });
            }
        });

        detachable = true;
        dragStage = new Stage();
        dragStage.initStyle(StageStyle.UNDECORATED);
        StackPane dragStagePane = new StackPane();
        dragStagePane.setStyle("-fx-background-color:#DDDDDD;");
        dragText = new Text(text);
        StackPane.setAlignment(dragText, Pos.CENTER);
        dragStagePane.getChildren().add(dragText);
        dragStage.setScene(new Scene(dragStagePane));
        nameLabelSnapshot.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (!draggable) {
                    return;
                }

                if (t.getButton() != MouseButton.PRIMARY) {
                    return;
                }

                dragStage.setWidth(nameLabelSnapshot.getWidth() + 10);
                dragStage.setHeight(nameLabelSnapshot.getHeight() + 10);
                dragStage.setX(t.getScreenX());
                dragStage.setY(t.getScreenY());
                dragStage.show();
                Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                tabPanes.add(getTabPane());
                InsertData data = getInsertData(screenPoint);

                if (data == null || data.getInsertPane().getTabs().isEmpty()) {
                    markerStage.hide();
                } else {
                    int index = data.getIndex();
                    boolean end = false;

                    if (index == data.getInsertPane().getTabs().size()) {
                        end = true;
                        index--;
                    }

                    Rectangle2D rect = getAbsoluteRect(data.getInsertPane().getTabs().get(index));
                    if (end) {
                        markerStage.setX(rect.getMaxX() + 13);
                    } else {
                        markerStage.setX(rect.getMinX());
                    }

                    markerStage.setY(rect.getMaxY() + 10);

                    if (disableDragFirst && index <= 0) {
                        markerStage.hide();
                        return;
                    }

                    if (disableDragLast && index >= data.getInsertPane().getTabs().size() - 1) {
                        markerStage.hide();
                        return;
                    }

                    markerStage.show();
                }
            }
        });

        nameLabelSnapshot.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                markerStage.hide();
                dragStage.hide();

                if (!draggable) {
                    return;
                }

                if (!t.isStillSincePress()) {
                    Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                    TabPane oldTabPane = getTabPane();
                    int oldIndex = oldTabPane.getTabs().indexOf(DraggableTab.this);
                    tabPanes.add(oldTabPane);
                    InsertData insertData = getInsertData(screenPoint);
                    if (insertData != null) {
                        int addIndex = insertData.getIndex();
                        if (oldTabPane == insertData.getInsertPane() && oldTabPane.getTabs().size() == 1) {
                            return;
                        }

                        if (disableDragLast && addIndex >= insertData.getInsertPane().getTabs().size()) {
                            return;
                        }

                        if (disableDragFirst && addIndex == 0) {
                            return;
                        }

                        oldTabPane.getTabs().remove(DraggableTab.this);

                        if (oldIndex < addIndex && oldTabPane == insertData.getInsertPane()) {
                            addIndex--;
                        }
                        if (addIndex > insertData.getInsertPane().getTabs().size()) {
                            addIndex = insertData.getInsertPane().getTabs().size();
                        }
                        insertData.getInsertPane().getTabs().add(addIndex, DraggableTab.this);
                        insertData.getInsertPane().selectionModelProperty().get().select(addIndex);
                        detachedStage = null;
                        return;
                    }

                    if (!detachable) {
                        return;
                    }

                    final Stage newStage = new Stage();
                    final TabPane pane = new TabPane();
                    tabPanes.add(pane);
                    newStage.setOnHiding(new EventHandler<WindowEvent>() {

                        @Override
                        public void handle(WindowEvent t) {
                            tabPanes.remove(pane);
                        }
                    });
                    getTabPane().getTabs().remove(DraggableTab.this);
                    pane.getTabs().add(DraggableTab.this);
                    pane.getTabs().addListener(new ListChangeListener<Tab>() {

                        @Override
                        public void onChanged(ListChangeListener.Change<? extends Tab> change) {
                            if (pane.getTabs().isEmpty()) {
                                newStage.hide();
                            }
                        }
                    });
                    newStage.setScene(new Scene(pane));
                    newStage.setX(t.getScreenX());
                    newStage.setY(t.getScreenY());
                    newStage.show();
                    newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            detachedStage = null;

                            if (DraggableTab.this.getOnCloseRequest() != null) {
                                ActionEvent actionEvent = new ActionEvent(DraggableTab.this, DraggableTab.this);
                                DraggableTab.this.getOnCloseRequest().handle(actionEvent);

                                if (actionEvent.isConsumed()) {
                                    event.consume();
                                }
                            }

                            if (DraggableTab.this.getOnClosed() != null) {
                                ActionEvent actionEvent = new ActionEvent(DraggableTab.this, DraggableTab.this);
                                DraggableTab.this.getOnClosed().handle(actionEvent);
                            }
                        }
                    });

                    detachedStage = newStage;

                    pane.requestLayout();
                    pane.requestFocus();
                }
            }

        });
    }

    /**
     * Set whether it's possible to detach the tab from its pane and move it to
     * another pane or another window. Defaults to true.
     * <p/>
     *
     * @param detachable true if the tab should be detachable, false otherwise.
     */
    public void setDetachable(boolean detachable) {
        this.detachable = detachable;
    }

    public boolean isDetachable() {
        return detachable;
    }

    public void updateGraphic() {
        setGraphic(nameLabel);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SnapshotParameters snapParams = new SnapshotParameters();
                snapParams.setFill(Color.TRANSPARENT);

                WritableImage snapshot = nameLabel.snapshot(snapParams, null);

                nameLabelSnapshot.setAutoSize(true);
                nameLabelSnapshot.setImage(snapshot);

                setGraphic(nameLabelSnapshot);
            }
        });
    }

    public void setLabelText(String text) {
        nameLabel.setText(text);
        dragText.setText(text);
        updateGraphic();
    }

    public void setLabelGraphic(Node node) {
        nameLabel.setGraphic(node);
        updateGraphic();
    }

    public String getLabelText() {
        return nameLabel.getText();
    }

    public Node getLabelGraphic() {
        return nameLabel.getGraphic();
    }

    public void toFront() {
        if (detachedStage != null) {
            detachedStage.toFront();
        }
    }

    public void toBack() {
        if (detachedStage != null) {
            detachedStage.toBack();
        }
    }

    public Stage getDetachedStage() {
        return detachedStage;
    }

    public boolean isDisableDragFirst() {
        return disableDragFirst;
    }

    public void setDisableDragFirst(boolean disableDragFirst) {
        this.disableDragFirst = disableDragFirst;
    }

    public boolean isDisableDragLast() {
        return disableDragLast;
    }

    public void setDisableDragLast(boolean disableDragLast) {
        this.disableDragLast = disableDragLast;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    private InsertData getInsertData(Point2D screenPoint) {
        for (TabPane tabPane : tabPanes) {
            Rectangle2D tabAbsolute = getAbsoluteRect(tabPane);
            if (tabAbsolute.contains(screenPoint)) {
                int tabInsertIndex = 0;
                if (!tabPane.getTabs().isEmpty()) {
                    Rectangle2D firstTabRect = getAbsoluteRect(tabPane.getTabs().get(0));
                    if (firstTabRect.getMaxY() + 60 < screenPoint.getY() || firstTabRect.getMinY() > screenPoint.getY()) {
                        return null;
                    }
                    Rectangle2D lastTabRect = getAbsoluteRect(tabPane.getTabs().get(tabPane.getTabs().size() - 1));
                    if (screenPoint.getX() < (firstTabRect.getMinX() + firstTabRect.getWidth() / 2)) {
                        tabInsertIndex = 0;
                    } else if (screenPoint.getX() > (lastTabRect.getMaxX() - lastTabRect.getWidth() / 2)) {
                        tabInsertIndex = tabPane.getTabs().size();
                    } else {
                        for (int i = 0; i < tabPane.getTabs().size() - 1; i++) {
                            Tab leftTab = tabPane.getTabs().get(i);
                            Tab rightTab = tabPane.getTabs().get(i + 1);
                            if (leftTab instanceof DraggableTab && rightTab instanceof DraggableTab) {
                                Rectangle2D leftTabRect = getAbsoluteRect(leftTab);
                                Rectangle2D rightTabRect = getAbsoluteRect(rightTab);
                                if (betweenX(leftTabRect, rightTabRect, screenPoint.getX())) {
                                    tabInsertIndex = i + 1;
                                    break;
                                }
                            }
                        }
                    }
                }
                return new InsertData(tabInsertIndex, tabPane);
            }
        }
        return null;
    }

    private Rectangle2D getAbsoluteRect(Control node) {
        return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                node.getWidth(),
                node.getHeight());
    }

    private Rectangle2D getAbsoluteRect(Canvas node) {
        return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                node.getWidth(),
                node.getHeight());
    }

    private Rectangle2D getAbsoluteRect(Tab tab) {
        Canvas node = ((DraggableTab) tab).nameLabelSnapshot;
        return getAbsoluteRect(node);
    }

    private Label getLabel() {
        return nameLabel;
    }

    private boolean betweenX(Rectangle2D r1, Rectangle2D r2, double xPoint) {
        double lowerBound = r1.getMinX() + r1.getWidth() / 2;
        double upperBound = r2.getMaxX() - r2.getWidth() / 2;
        return xPoint >= lowerBound && xPoint <= upperBound;
    }

    private static class InsertData {

        private final int index;
        private final TabPane insertPane;

        public InsertData(int index, TabPane insertPane) {
            this.index = index;
            this.insertPane = insertPane;
        }

        public int getIndex() {
            return index;
        }

        public TabPane getInsertPane() {
            return insertPane;
        }

    }
}