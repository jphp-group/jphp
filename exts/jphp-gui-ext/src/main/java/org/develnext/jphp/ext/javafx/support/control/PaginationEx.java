package org.develnext.jphp.ext.javafx.support.control;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class PaginationEx extends FlowPane {
    protected ObjectProperty<Font> font = new SimpleObjectProperty<>(Font.getDefault());
    protected ObjectProperty<Color> textColor = new SimpleObjectProperty<>(Color.BLACK);

    protected SimpleIntegerProperty total = new SimpleIntegerProperty(0);
    protected SimpleIntegerProperty pageSize;
    protected SimpleIntegerProperty selectedPage = new SimpleIntegerProperty(0);
    protected SimpleIntegerProperty maxPageCount = new SimpleIntegerProperty(9);
    protected BooleanProperty showPrevNext = new SimpleBooleanProperty(true);

    protected Button previousButton = new Button("<");
    protected Button nextButton = new Button(">");
    protected List<Button> pageButtons = new ArrayList<>();

    protected SimpleStringProperty hintText = new SimpleStringProperty("");
    protected SimpleBooleanProperty showTotal = new SimpleBooleanProperty(false);

    public PaginationEx() {
        getStyleClass().addAll("nav");
        previousButton.getStyleClass().addAll("nav-item", "nav-item-prev");
        nextButton.getStyleClass().addAll("nav-item", "nav-item-next");

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                updateUi();
            }
        };

        total.addListener(changeListener);
        pageSizeProperty().addListener(changeListener);
        maxPageCount.addListener(changeListener);
        selectedPage.addListener(changeListener);
        showTotal.addListener(changeListener);
        hintText.addListener(changeListener);
        font.addListener(changeListener);
        textColor.addListener(changeListener);
        showPrevNext.addListener(changeListener);

        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (selectedPage.get() > 0) {
                    selectedPage.set(selectedPage.get() - 1);
                }
            }
        });

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (selectedPage.get() < getPageCount() - 1) {
                    selectedPage.set(selectedPage.get() + 1);
                }
            }
        });
    }

    public Font getFont() {
        return font.get();
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }

    public void setFont(Font font) {
        this.font.set(font);
    }

    public Color getTextColor() {
        return textColor.get();
    }

    public ObjectProperty<Color> textColorProperty() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor.set(textColor);
    }

    public boolean getShowPrevNext() {
        return showPrevNext.get();
    }

    public BooleanProperty showPrevNextProperty() {
        return showPrevNext;
    }

    public void setShowPrevNext(boolean showPrevNext) {
        this.showPrevNext.set(showPrevNext);
    }

    public int getTotal() {
        return total.get();
    }

    public SimpleIntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public SimpleIntegerProperty pageSizeProperty() {
        if (pageSize == null) {
            pageSize = new SimpleIntegerProperty(this, "pageSize", 20);
        }

        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    public int getPageCount() {
        int pages = (int) Math.ceil(total.get() / (float)pageSize.get());
        return pages;
    }

    public int getSelectedPage() {
        return selectedPage.get();
    }

    public SimpleIntegerProperty selectedPageProperty() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage.set(selectedPage);
    }

    public int getMaxPageCount() {
        return maxPageCount.get();
    }

    public SimpleIntegerProperty maxPageCountProperty() {
        return maxPageCount;
    }

    public void setMaxPageCount(int maxPageCount) {
        this.maxPageCount.set(maxPageCount);
    }

    public Button getPreviousButton() {
        return previousButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public String getHintText() {
        return hintText.get();
    }

    public SimpleStringProperty hintTextProperty() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText.set(hintText);
    }

    public boolean isShowTotal() {
        return showTotal.get();
    }

    public SimpleBooleanProperty showTotalProperty() {
        return showTotal;
    }

    public void setShowTotal(boolean showTotal) {
        this.showTotal.set(showTotal);
    }

    protected void updateSelected() {
        updateUi();
    }

    protected void updateUi() {
        ObservableList<Node> children = this.getChildren();

        children.clear();
        pageButtons.clear();

        int selectedPage = getSelectedPage();
        final int pages = getPageCount();

        if (getShowPrevNext()) {
            children.add(previousButton);
        }

        int from = selectedPage - maxPageCount.get() / 2;
        if (from < 0) from = 0;

        int to   = from + maxPageCount.get();
        if (to > pages - 1) to = pages;

        if (to - from < maxPageCount.get()) {
            from -= maxPageCount.get() - (to - from);

            if (from < 0) from = 0;
        }

        boolean lastSkip = to < pages;
        boolean firstSkip = from > 0;

        if (firstSkip) {
            Button button = new Button(String.valueOf(1));
            button.getStyleClass().addAll("nav-item", "nav-item-first");
            button.setFont(getFont());
            button.setTextFill(getTextColor());
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setSelectedPage(0);
                }
            });

            pageButtons.add(button);
            children.add(button);

            Label label = new Label("...");
            label.setFont(getFont());
            label.getStyleClass().addAll("nav-item-skip");
            children.add(label);
        }

        for (int i = from; i < to; i++) {
            final Button button = new Button(String.valueOf(i + 1));
            button.getStyleClass().addAll("nav-item");
            button.setFont(getFont());
            button.setTextFill(getTextColor());

            if (selectedPage == i) {
                button.getStyleClass().addAll("selected");

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        button.requestFocus();
                    }
                });
            }

            final int page = i;
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setSelectedPage(page);
                }
            });

            pageButtons.add(button);
            children.addAll(button);
        }

        if (lastSkip) {
            Label label = new Label("...");
            label.getStyleClass().addAll("nav-item-skip");
            label.setFont(getFont());
            children.add(label);

            Button button = new Button(String.valueOf(pages));
            button.getStyleClass().addAll("nav-item", "nav-item-last");
            button.setFont(getFont());
            button.setTextFill(getTextColor());

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setSelectedPage(pages - 1);
                }
            });

            pageButtons.add(button);
            children.add(button);
        }

        if (getShowPrevNext()) {
            children.add(nextButton);
        }

        previousButton.setFont(getFont());
        previousButton.setTextFill(getTextColor());

        nextButton.setFont(getFont());
        nextButton.setTextFill(getTextColor());

        previousButton.setDisable(selectedPage == 0);
        nextButton.setDisable(selectedPage == getPageCount() - 1);

        if (hintText.get() != null && !hintText.get().isEmpty()) {
            Label label = new Label(hintText.get());
            label.getStyleClass().add("nav-hint-text");
            label.setFont(getFont());

            children.add(label);
        }

        if (showTotal.get()) {
            Label label = new Label(String.valueOf(total.get()));
            label.getStyleClass().addAll("nav-hint-total");
            label.setFont(getFont());

            children.add(label);
        }
    }
}
