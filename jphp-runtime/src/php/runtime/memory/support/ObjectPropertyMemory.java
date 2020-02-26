package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;
import php.runtime.lang.exception.BaseError;
import php.runtime.lang.exception.BaseTypeError;
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

    protected void checkIterable() {
        switch (property.getType()) {
            case ITERABLE:
            case ARRAY:
                return;
            default:
                String mustBe = property.getTypeChecker().getSignature();
                if (property.isNullable()) {
                    mustBe = "?" + mustBe;
                }

                env.exception(trace,
                        BaseTypeError.class,
                        "Cannot auto-initialize an array inside property %s::$%s of type ?string",
                        property.getClazz().getName(), property.getName(), mustBe
                );
        }
    }

    @Override
    public Memory refOfPush(TraceInfo trace) {
        checkIterable();
        return super.refOfPush(trace);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, Memory index) {
        checkIterable();
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, long index) {
        checkIterable();
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, double index) {
        checkIterable();
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, String index) {
        checkIterable();
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, boolean index) {
        checkIterable();
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory toImmutable() {
        if (isUninitialized()) {
            env.exception(trace,
                    BaseError.class,
                    "Typed static property %s::$%s must not be accessed before initialization",
                    property.getClazz().getName(), property.getName()
            );
        }

        return super.toImmutable();
    }
}
