package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JProgressBarPropertyReaders extends PropertyReaders<JProgressBar> {
    protected final Map<String, PropertyReader<JProgressBar>> register = new HashMap<String, PropertyReader<JProgressBar>>(){{
        put("min", MIN);
        put("max", MAX);
        put("value", VALUE);
        put("border-painted", BORDER_PAINTED);
        put("indeterminate", INDETERMINATE);
        put("vertical", VERTICAL);
        put("text-painted", TEXT_PAINTED);
        put("text", TEXT);
    }};

    @Override
    protected Map<String, PropertyReader<JProgressBar>> getRegister() {
        return register;
    }

    @Override
    public Class<JProgressBar> getRegisterClass() {
        return JProgressBar.class;
    }

    public final static PropertyReader<JProgressBar> MAX = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setMaximum(value.asInteger());
        }
    };

    public final static PropertyReader<JProgressBar> MIN = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setMinimum(value.asInteger());
        }
    };

    public final static PropertyReader<JProgressBar> VALUE = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setValue(value.asInteger());
        }
    };

    public final static PropertyReader<JProgressBar> BORDER_PAINTED = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setBorderPainted(value.asBoolean());
        }
    };

    public final static PropertyReader<JProgressBar> INDETERMINATE = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setIndeterminate(value.asBoolean());
        }
    };

    public final static PropertyReader<JProgressBar> VERTICAL = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setOrientation(value.asBoolean() ? JProgressBar.VERTICAL : JProgressBar.HORIZONTAL);
        }
    };

    public final static PropertyReader<JProgressBar> TEXT_PAINTED = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setStringPainted(value.asBoolean());
        }
    };

    public final static PropertyReader<JProgressBar> TEXT = new PropertyReader<JProgressBar>() {
        @Override
        public void read(JProgressBar component, Value value) {
            component.setString(value.asString());
        }
    };
}
