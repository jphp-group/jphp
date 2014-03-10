package php.runtime.ext.swing.loader.support;

import java.awt.*;

abstract public class PropertyReader<T extends Component> {
    abstract public void read(T component, Value value);

    public boolean isTranslatable() { return false; }
}
