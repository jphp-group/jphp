package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.loader.support.PropertyReader;

import java.awt.*;
import java.util.Map;

abstract public class PropertyReaders<T extends Component> {
    abstract protected Map<String, PropertyReader<T>> getRegister();
    abstract public Class<T> getRegisterClass();

    public PropertyReader<T> getReader(String propertyName) {
        return getRegister().get(propertyName);
    }
}
