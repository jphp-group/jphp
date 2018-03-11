package org.develnext.jphp.ext.javafx.support.control;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FragmentPane extends VBox {
    private Parent root;

    public FragmentPane() {
        super();
        getStyleClass().add("fragment-pane");
    }

    public FragmentPane(double spacing) {
        super(spacing);
    }

    public FragmentPane(Node... children) {
        super(children);
    }

    public FragmentPane(double spacing, Node... children) {
        super(spacing, children);
    }

    public void applyFragment(Stage form) {
        this.getChildren().clear();

        if (form != null) {
            root = form.getScene().getRoot();
            form.getScene().setRoot(new Pane());

            double width = getPrefWidth(), height = getPrefHeight();

            SubScene scene = new SubScene(root, width, height);

            scene.widthProperty().bind(widthProperty());
            scene.heightProperty().bind(heightProperty());

            VBox.setVgrow(scene, Priority.ALWAYS);

            this.getChildren().add(scene);
        }
    }

    public Parent getLayout() {
        return root;
    }
}
