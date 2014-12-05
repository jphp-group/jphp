package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import php.runtime.launcher.StandaloneLauncher;

import java.awt.*;

@Tag("include")
public class IncludeTag extends BaseTag<Component> {
    @Override
    public Component create(ElementItem element, UIReader uiReader) {
        String src = element.getAttr("src");
        if (src == null)
            throw new IllegalArgumentException("Attribute `src` is required");

        Component cmp = uiReader.read(StandaloneLauncher.class.getClassLoader().getResourceAsStream(src));
        return cmp;
    }

    @Override
    public void read(ElementItem element, Component component, Node node, UIReader uiReader) {

    }

    @Override
    public boolean isNeedRegister() {
        return false;
    }
}
