package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.XYLayout;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JFrameX;

@Tag("ui-form")
public class UIFormTag extends UIWindowTag<JFrameX> {
    @Override
    public JFrameX create(ElementItem element, UIReader uiReader) {
        return new JFrameX();
    }

    @Override
    public void read(ElementItem element, JFrameX component, Node node) {
        component.setLayout(new XYLayout());
    }
}
