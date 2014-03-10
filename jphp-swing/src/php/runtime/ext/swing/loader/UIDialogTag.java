package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.XYLayout;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JDialogX;

@Tag("ui-dialog")
public class UIDialogTag extends UIWindowTag<JDialogX> {
    @Override
    public JDialogX create(ElementItem element, UIReader uiReader) {
        return new JDialogX();
    }

    @Override
    public void read(ElementItem element, JDialogX component, Node node) {
        component.setLayout(new XYLayout());
    }
}
