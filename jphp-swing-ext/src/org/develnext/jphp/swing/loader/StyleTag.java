package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.loader.support.Value;
import org.w3c.dom.Node;

@Tag("style")
public class StyleTag extends BaseTag<UIReader.Style> {
    @Override
    public UIReader.Style create(ElementItem element, UIReader uiReader) {
        return new UIReader.Style();
    }

    @Override
    public void onReadAttribute(ElementItem element, String property, Value value, UIReader.Style component,
                                UIReader uiReader) {
        component.set(property, value);
    }

    @Override
    public void afterRead(ElementItem element, UIReader.Style component, Node node, UIReader uiReader) {
        uiReader.registerStyle(component);
    }

    @Override
    public boolean isAllowsChildren() {
        return false;
    }
}
