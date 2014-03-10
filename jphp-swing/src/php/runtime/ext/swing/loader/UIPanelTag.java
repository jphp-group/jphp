package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.XYLayout;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-panel")
public class UIPanelTag extends BaseTag<JPanel> {
    @Override
    public JPanel create(ElementItem element, UIReader uiReader) {
        return new JPanel();
    }

    @Override
    public void read(ElementItem element, JPanel component, Node node) {
        component.setLayout(new XYLayout());
    }
}
