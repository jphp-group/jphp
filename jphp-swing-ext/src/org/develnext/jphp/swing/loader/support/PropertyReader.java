package org.develnext.jphp.swing.loader.support;

import java.awt.*;

abstract public class PropertyReader<T extends Component> {
    abstract public void read(T component, Value value);

    public boolean isTranslatable() { return false; }
    public boolean isArrayed() { return false; }
    public boolean isTrimArrayed() { return false; }
    public boolean isPostRead() { return false; }
}
