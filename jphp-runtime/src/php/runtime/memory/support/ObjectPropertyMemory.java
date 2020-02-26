package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.memory.helper.ShortcutMemory;
import php.runtime.memory.support.operation.ObjectMemoryOperation;
import php.runtime.reflection.PropertyEntity;

public class ObjectPropertyMemory extends ShortcutMemory {
    private Environment env;
    private TraceInfo trace;
    private PropertyEntity property;

    public ObjectPropertyMemory(Environment env, TraceInfo trace, ReferenceMemory ref, PropertyEntity property) {
        super(ref);
        this.env = env;
        this.trace = trace;
        this.property = property;
    }

    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(PropertyEntity property) {
        this.property = property;
    }

    @Override
    public ReferenceMemory getReference() {
        env.error(
                trace, "Unable to assign by ref for typed property %s::$%s, jphp will not support this feature",
                property.getClazz().getName(),
                property.getName()
        );

        return this;
    }

    @Override
    public Memory assign(Memory memory) {
        memory = property.typedValue(env, trace, memory);
        return super.assign(memory);
    }

    @Override
    public Memory assign(double memory) {
        return assign(DoubleMemory.valueOf(memory));
    }

    @Override
    public Memory assign(long memory) {
        return assign(DoubleMemory.valueOf(memory));
    }

    @Override
    public Memory assign(String memory) {
        return assign(StringMemory.valueOf(memory));
    }

    @Override
    public Memory assign(boolean memory) {
        return assign(TrueMemory.valueOf(memory));
    }
}
