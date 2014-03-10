package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;
import php.runtime.ext.swing.support.JTextAreaX;

import java.util.HashMap;
import java.util.Map;

public class JTextAreaPropertyReaders extends PropertyReaders<JTextAreaX> {
    protected final Map<String, PropertyReader<JTextAreaX>> register = new HashMap<String, PropertyReader<JTextAreaX>>(){{
        put("rows", ROWS);
        put("columns", COLUMNS);
        put("tab-size", TAB_SIZE);
        put("line-wrap", LINE_WRAP);
        put("wrap-style-word", WRAP_STYLE_WORD);
    }};

    @Override
    protected Map<String, PropertyReader<JTextAreaX>> getRegister() {
        return register;
    }

    @Override
    public Class<JTextAreaX> getRegisterClass() {
        return JTextAreaX.class;
    }

    public final static PropertyReader<JTextAreaX> ROWS = new PropertyReader<JTextAreaX>() {
        @Override
        public void read(JTextAreaX component, Value value) {
            component.getContent().setRows(value.asInteger());
        }
    };

    public final static PropertyReader<JTextAreaX> COLUMNS = new PropertyReader<JTextAreaX>() {
        @Override
        public void read(JTextAreaX component, Value value) {
            component.getContent().setColumns(value.asInteger());
        }
    };

    public final static PropertyReader<JTextAreaX> TAB_SIZE = new PropertyReader<JTextAreaX>() {
        @Override
        public void read(JTextAreaX component, Value value) {
            component.getContent().setTabSize(value.asInteger());
        }
    };

    public final static PropertyReader<JTextAreaX> LINE_WRAP = new PropertyReader<JTextAreaX>() {
        @Override
        public void read(JTextAreaX component, Value value) {
            component.getContent().setLineWrap(value.asBoolean());
        }
    };

    public final static PropertyReader<JTextAreaX> WRAP_STYLE_WORD = new PropertyReader<JTextAreaX>() {
        @Override
        public void read(JTextAreaX component, Value value) {
            component.getContent().setWrapStyleWord(value.asBoolean());
        }
    };
}
