package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JImageX;

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
