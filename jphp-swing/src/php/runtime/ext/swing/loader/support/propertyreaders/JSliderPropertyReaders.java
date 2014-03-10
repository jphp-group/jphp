package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JSliderPropertyReaders extends PropertyReaders<JSlider> {
    protected final Map<String, PropertyReader<JSlider>> register = new HashMap<String, PropertyReader<JSlider>>(){{
        put("value", VALUE);
        put("max", MAX);
        put("min", MIN);
        put("extent", EXTENT);
        put("paint-labels", PAINT_LABELS);
        put("paint-ticks", PAINT_TICKS);
        put("paint-track", PAINT_TRACK);
        put("invert", INVERT);
        put("snap-to-ticks", SNAP_TO_TICKS);
        put("major-tick-spacing", MAJOR_TICK_SPACING);
        put("minor-tick-spacing", MINOR_TICK_SPACING);
        put("value-is-adjusting", VALUE_IS_ADJUSTING);
    }};

    @Override
    protected Map<String, PropertyReader<JSlider>> getRegister() {
        return register;
    }

    @Override
    public Class<JSlider> getRegisterClass() {
        return JSlider.class;
    }

    public final static PropertyReader<JSlider> VALUE = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setValue(value.asInteger());
        }
    };

    public final static PropertyReader<JSlider> MAX = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setMaximum(value.asInteger());
        }
    };

    public final static PropertyReader<JSlider> MIN = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setMinimum(value.asInteger());
        }
    };

    public final static PropertyReader<JSlider> EXTENT = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setExtent(value.asInteger());
        }
    };

    public final static PropertyReader<JSlider> PAINT_LABELS = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setPaintLabels(value.asBoolean());
        }
    };

    public final static PropertyReader<JSlider> PAINT_TICKS = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setPaintTicks(value.asBoolean());
        }
    };

    public final static PropertyReader<JSlider> PAINT_TRACK = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setPaintTrack(value.asBoolean());
        }
    };

    public final static PropertyReader<JSlider> INVERT = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setInverted(value.asBoolean());
        }
    };

    public final static PropertyReader<JSlider> SNAP_TO_TICKS = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setSnapToTicks(value.asBoolean());
        }
    };

    public final static PropertyReader<JSlider> VALUE_IS_ADJUSTING = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setValueIsAdjusting(value.asBoolean());
        }
    };

    public final static PropertyReader<JSlider> MAJOR_TICK_SPACING = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setMajorTickSpacing(value.asInteger());
        }
    };

    public final static PropertyReader<JSlider> MINOR_TICK_SPACING = new PropertyReader<JSlider>() {
        @Override
        public void read(JSlider component, Value value) {
            component.setMinorTickSpacing(value.asInteger());
        }
    };

}
