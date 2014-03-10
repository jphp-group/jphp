package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;
import php.runtime.ext.swing.support.JImageX;

import java.util.HashMap;
import java.util.Map;

public class JImageXPropertyReaders extends PropertyReaders<JImageX> {

    protected final Map<String, PropertyReader<JImageX>> register = new HashMap<String, PropertyReader<JImageX>>(){{
        put("image", IMAGE);
        put("smooth", SMOOTH);
        put("centred", CENTRED);
        put("mosaic", MOSAIC);
        put("proportional", PROPORTIONAL);
        put("stretch", STRETCH);
    }};

    @Override
    protected Map<String, PropertyReader<JImageX>> getRegister() {
        return register;
    }

    @Override
    public Class<JImageX> getRegisterClass() {
        return JImageX.class;
    }

    public final static PropertyReader<JImageX> IMAGE = new PropertyReader<JImageX>() {
        @Override
        public void read(JImageX component, Value value) {
            component.setImage(value.asIcon());
        }
    };

    public final static PropertyReader<JImageX> SMOOTH = new PropertyReader<JImageX>() {
        @Override
        public void read(JImageX component, Value value) {
            component.setSmooth(value.asBoolean());
        }
    };

    public final static PropertyReader<JImageX> CENTRED = new PropertyReader<JImageX>() {
        @Override
        public void read(JImageX component, Value value) {
            component.setCentred(value.asBoolean());
        }
    };

    public final static PropertyReader<JImageX> PROPORTIONAL = new PropertyReader<JImageX>() {
        @Override
        public void read(JImageX component, Value value) {
            component.setProportional(value.asBoolean());
        }
    };

    public final static PropertyReader<JImageX> STRETCH = new PropertyReader<JImageX>() {
        @Override
        public void read(JImageX component, Value value) {
            component.setStretch(value.asBoolean());
        }
    };

    public final static PropertyReader<JImageX> MOSAIC = new PropertyReader<JImageX>() {
        @Override
        public void read(JImageX component, Value value) {
            component.setMosaic(value.asBoolean());
        }
    };

}
