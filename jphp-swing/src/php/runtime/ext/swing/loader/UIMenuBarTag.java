package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-menu-bar")
public class UIMenuBarTag extends BaseTag<JMenuBar> {
    @Override
    public JMenuBar create(ElementItem element, UIReader uiReader) {
        return new JMenuBar();
    }

    @Override
    public void read(ElementItem element, JMenuBar component, Node node) {
    }
}
