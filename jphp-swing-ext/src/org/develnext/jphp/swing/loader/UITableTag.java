package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JTableX;

@Tag("ui-table")
public class UITableTag extends BaseTag<JTableX> {
    @Override
    public JTableX create(ElementItem element, UIReader uiReader) {
        return new JTableX();
    }
}
