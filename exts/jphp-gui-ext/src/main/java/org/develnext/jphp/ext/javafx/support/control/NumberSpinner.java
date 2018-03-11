package org.develnext.jphp.ext.javafx.support.control;

import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class NumberSpinner extends Spinner<Integer> {
    public enum ArrowsStyle {
        RIGHT_VERTICAL(""),
        RIGHT_HORIZONTAL("arrows-on-right-horizontal"),
        LEFT_VERTICAL("arrows-on-left-vertical"),
        LEFT_HORIZONTAL("arrows-on-left-horizontal"),
        SPLIT_VERTICAL("split-arrows-vertical"),
        SPLIT_HORIZONTAL("split-arrows-horizontal");

        private final String style;

        ArrowsStyle(String style) {
            this.style = style;
        }

        public String getStyleClass() {
            return style;
        }
    }

    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;
    private int step = 1;
    private int initial = 0;
    private ArrowsStyle arrowsStyle = ArrowsStyle.RIGHT_VERTICAL;

    public NumberSpinner() {
        super();
        setMinSize(0, 0);
        setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step));
    }

    public void setAlignment(Pos value) {
        getEditor().setAlignment(value);
    }

    public Pos getAlignment() {
        return getEditor().getAlignment();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;

        setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step));
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;

        setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step));
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;

        setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step));
    }

    public int getInitial() {
        return initial;
    }

    public void setInitial(int initial) {
        this.initial = initial;

        setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step));
    }

    public ArrowsStyle getArrowsStyle() {
        return arrowsStyle;
    }

    public void setArrowsStyle(ArrowsStyle arrowsStyle) {
        this.arrowsStyle = arrowsStyle;

        for (ArrowsStyle style : ArrowsStyle.values()) {
            getStyleClass().remove(style.getStyleClass());
        }

        if (arrowsStyle != null) {
            getStyleClass().add(arrowsStyle.getStyleClass());
        }
    }
}
