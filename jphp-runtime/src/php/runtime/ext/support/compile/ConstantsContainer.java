package php.runtime.ext.support.compile;

import php.runtime.Memory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract public class ConstantsContainer {

    public Collection<CompileConstant> getConstants(){
        Field[] declaredFields = getClass().getDeclaredFields();

        List<CompileConstant> result = new ArrayList<>(declaredFields.length);

        for(Field field : declaredFields){
            field.setAccessible(true);
            try {
                Object value = field.get(this);

                if (value instanceof Memory) {
                    result.add(new CompileConstant(field.getName(), (Memory) value));
                } else {
                    result.add(new CompileConstant(field.getName(), value));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}
