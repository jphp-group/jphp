package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JImageX;

@Tag("ui-image")
public class UIImageTag extends BaseTag<JImageX> {
    @Override
    public JImageX create(ElementItem element, UIReader uiReader) {
        return new JImageX();
    }

    @Override
    public void read(ElementItem element, JImageX component, Node node) {

    }
}
