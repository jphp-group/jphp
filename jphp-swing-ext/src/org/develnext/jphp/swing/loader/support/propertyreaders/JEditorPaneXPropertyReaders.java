package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;
import org.develnext.jphp.swing.support.JEditorPaneX;

import java.util.HashMap;
import java.util.Map;

public class JEditorPaneXPropertyReaders extends PropertyReaders<JEditorPaneX> {
    protected final Map<String, PropertyReader<JEditorPaneX>> register = new HashMap<String, PropertyReader<JEditorPaneX>>(){{
        put("content-type", CONTENT_TYPE);
        put("selection-color", SELECTION_COLOR);
        put("selected-text-color", SELECTED_TEXT_COLOR);
        put("disabled-text-color", DISABLED_TEXT_COLOR);
    }};

    @Override
    protected Map<String, PropertyReader<JEditorPaneX>> getRegister() {
        return register;
    }

    @Override
    public Class<JEditorPaneX> getRegisterClass() {
        return JEditorPaneX.class;
    }


    public final static PropertyReader<JEditorPaneX> CONTENT_TYPE = new PropertyReader<JEditorPaneX>() {
        @Override
        public void read(JEditorPaneX component, Value value) {
            String text = component.getText();
            component.getContent().setContentType(value.asString());
            component.getContent().setText(text);
        }
    };

    public final static PropertyReader<JEditorPaneX> SELECTION_COLOR = new PropertyReader<JEditorPaneX>() {
        @Override
        public void read(JEditorPaneX component, Value value) {
            component.setSelectionColor(value.asColor());
        }
    };

    public final static PropertyReader<JEditorPaneX> SELECTED_TEXT_COLOR = new PropertyReader<JEditorPaneX>() {
        @Override
        public void read(JEditorPaneX component, Value value) {
            component.setSelectedTextColor(value.asColor());
        }
    };

    public final static PropertyReader<JEditorPaneX> DISABLED_TEXT_COLOR = new PropertyReader<JEditorPaneX>() {
        @Override
        public void read(JEditorPaneX component, Value value) {
            component.setDisabledTextColor(value.asColor());
        }
    };
}
