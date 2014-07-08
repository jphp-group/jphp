package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;
import org.develnext.jphp.swing.support.JTreeX;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.Map;

public class JTreeXPropertyReaders extends PropertyReaders<JTreeX> {
    protected final Map<String, PropertyReader<JTreeX>> register = new HashMap<String, PropertyReader<JTreeX>>(){{
        put("root", ROOT);
        put("drag-enabled", DRAG_ENABLED);
        put("root-visible", ROOT_VISIBLE);
        put("editable", EDITABLE);
        put("expands-selected-paths", EXPANDS_SELECTED_PATHS);
        put("row-height", ROW_HEIGHT);
        put("scrolls-on-expand", SCROLLS_ON_EXPAND);
        put("visible-row-count", VISIBLE_ROW_COUNT);
    }};

    @Override
    protected Map<String, PropertyReader<JTreeX>> getRegister() {
        return register;
    }

    @Override
    public Class<JTreeX> getRegisterClass() {
        return JTreeX.class;
    }

    public final static PropertyReader<JTreeX> ROOT = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(value.asString());
            component.setModel(new DefaultTreeModel(root));
        }

        @Override
        public boolean isTranslatable() {
            return true;
        }
    };

    public final static PropertyReader<JTreeX> DRAG_ENABLED = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            component.getContent().setDragEnabled(value.asBoolean());
        }
    };

    public final static PropertyReader<JTreeX> ROOT_VISIBLE = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            component.getContent().setRootVisible(value.asBoolean());
        }
    };

    public final static PropertyReader<JTreeX> EDITABLE = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            component.getContent().setEditable(value.asBoolean());
        }
    };

    public final static PropertyReader<JTreeX> EXPANDS_SELECTED_PATHS = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            component.getContent().setExpandsSelectedPaths(value.asBoolean());
        }
    };

    public final static PropertyReader<JTreeX> ROW_HEIGHT = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            component.getContent().setRowHeight(value.asInteger());
        }
    };

    public final static PropertyReader<JTreeX> SCROLLS_ON_EXPAND = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            component.getContent().setScrollsOnExpand(value.asBoolean());
        }
    };

    public final static PropertyReader<JTreeX> VISIBLE_ROW_COUNT = new PropertyReader<JTreeX>() {
        @Override
        public void read(JTreeX component, Value value) {
            component.getContent().setVisibleRowCount(value.asInteger());
        }
    };
}
