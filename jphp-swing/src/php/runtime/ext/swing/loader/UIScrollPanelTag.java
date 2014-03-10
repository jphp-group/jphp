package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JScrollPanel;

import java.awt.*;

@Tag("ui-scroll-panel")
public class UIScrollPanelTag extends BaseTag<JScrollPanel> {
    @Override
    public JScrollPanel create(ElementItem element, UIReader uiReader) {
        return new JScrollPanel();
    }

    @Override
    public void read(ElementItem element, JScrollPanel component, Node node) {

    }

    @Override
    public void addChildren(JScrollPanel component, Component child) {
        component.setViewportView(child);
    }
}
