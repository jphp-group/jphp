package php.runtime.memory;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;

import java.nio.charset.Charset;

public class ReferenceMemory extends Memory {
    private Memory _value;

    protected ReferenceMemory(Type type, Memory value) {
        super(type);
        this._value = value == null ? NULL : value;
    }

    public ReferenceMemory(Memory value) {
        super(Type.REFERENCE);
        this._value = value == null ? Memory.NULL : value;
    }

    public static Memory valueOf(Memory value){
        return new ReferenceMemory(value);
    }

    public ReferenceMemory() {
        super(Type.REFERENCE);
        this._value = Memory.NULL;
    }

    public ReferenceMemory duplicate(){
        return new ReferenceMemory(getValue().toImmutable());
    }

    public Memory getValue() {
        return _value;
    }

    public Memory setValue(Memory value) {
        return this._value = value == null ? NULL : value;
    }

    @Override
    public int getPointer() {
        return getValue().getPointer();
    }

    @Override
    public int getPointer(boolean absolute) {
        return absolute ? getValue().getPointer() : getPointer();
    }

    @Override
    public long toLong() {
        return getValue().toLong();
    }

    @Override
    public double toDouble() {
        return getValue().toDouble();
    }

    @Override
    public boolean toBoolean() {
        return getValue().toBoolean();
    }

    @Override
    public Memory toNumeric() {
        return getValue().toNumeric();
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public char toChar() {
        return getValue().toChar();
    }

    @Override
    public boolean isNull() {
        return getValue().isNull();
    }

    @Override
    public boolean isObject() {
        return getValue().isObject();
    }

    @Override
    public boolean isResource() {
        return getValue().isResource();
    }

    @Override
    public boolean isArray() {
        return getValue().isArray();
    }

    @Override
    public boolean isString() {
        return getValue().isString();
    }

    @Override
    public boolean isNumber() {
        return getValue().isNumber();
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public Memory inc() {
        return getValue().inc();
    }

    @Override
    public Memory dec() {
        return getValue().dec();
    }

    @Override
    public Memory negative() {
        return getValue().negative();
    }

    @Override
    public Memory plus(Memory memory) {
        return getValue().plus(memory);
    }

    @Override
    public Memory plus(long value) {
        return this.getValue().plus(value);
    }

    @Override
    public Memory plus(double value) {
        return this.getValue().plus(value);
    }

    @Override
    public Memory plus(boolean value) {
        return this.getValue().plus(value);
    }

    @Override
    public Memory plus(String value) {
        return this.getValue().plus(value);
    }

    @Override
    public Memory minus(Memory memory) {
        return getValue().minus(memory);
    }

    @Override
    public Memory minus(double value) {
        return this.getValue().minus(value);
    }

    @Override
    public Memory minus(boolean value) {
        return this.getValue().minus(value);
    }

    @Override
    public Memory minus(String value) {
        return this.getValue().minus(value);
    }

    @Override
    public Memory minus(long value) {
        return this.getValue().minus(value);
    }

    @Override
    public Memory mul(Memory memory) {
        return getValue().mul(memory);
    }

    @Override
    public Memory mul(long value) {
        return this.getValue().mul(value);
    }

    @Override
    public Memory mul(double value) {
        return this.getValue().mul(value);
    }

    @Override
    public Memory mul(boolean value) {
        return this.getValue().mul(value);
    }

    @Override
    public Memory mul(String value) {
        return this.getValue().mul(value);
    }

    @Override
    public Memory pow(Memory memory) {
        return getValue().pow(memory);
    }

    @Override
    public Memory pow(long value) {
        return this.getValue().pow(value);
    }

    @Override
    public Memory pow(double value) {
        return this.getValue().pow(value);
    }

    @Override
    public Memory pow(boolean value) {
        return this.getValue().pow(value);
    }

    @Override
    public Memory pow(String value) {
        return this.getValue().pow(value);
    }

    @Override
    public Memory div(Memory memory) {
        return getValue().div(memory);
    }

    @Override
    public Memory div(long value) {
        return this.getValue().div(value);
    }

    @Override
    public Memory div(double value) {
        return this.getValue().div(value);
    }

    @Override
    public Memory div(boolean value) {
        return this.getValue().div(value);
    }

    @Override
    public Memory div(String value) {
        return this.getValue().div(value);
    }

    @Override
    public Memory mod(long value) {
        return this.getValue().mod(value);
    }

    @Override
    public Memory mod(double value) {
       return this.getValue().mod(value);
    }

    @Override
    public Memory mod(boolean value) {
        return this.getValue().mod(value);
    }

    @Override
    public Memory mod(String value) {
        return this.getValue().mod(value);
    }

    @Override
    public boolean equal(Memory memory) {
        return getValue().equal(memory);
    }

    @Override
    public boolean equal(long value) {
        return this.getValue().equal(value);
    }

    @Override
    public boolean equal(double value) {
        return this.getValue().equal(value);
    }

    @Override
    public boolean equal(boolean value) {
        return this.getValue().equal(value);
    }

    @Override
    public boolean equal(String value) {
        return this.getValue().equal(value);
    }

    @Override
    public boolean notEqual(Memory memory) {
        return getValue().notEqual(memory);
    }

    @Override
    public boolean notEqual(long value) {
        return this.getValue().notEqual(value);
    }

    @Override
    public boolean notEqual(double value) {
        return this.getValue().notEqual(value);
    }

    @Override
    public boolean notEqual(boolean value) {
        return this.getValue().notEqual(value);
    }

    @Override
    public boolean notEqual(String value) {
        return this.getValue().notEqual(value);
    }

    @Override
    public String concat(Memory memory) {
        return getValue().concat(memory);
    }

    @Override
    public String concat(long value) {
        return this.getValue().concat(value);
    }

    @Override
    public String concat(double value) {
        return this.getValue().concat(value);
    }

    @Override
    public String concat(boolean value) {
        return this.getValue().concat(value);
    }

    @Override
    public String concat(String value) {
        return this.getValue().concat(value);
    }

    @Override
    public boolean smaller(Memory memory) {
        return getValue().smaller(memory);
    }

    @Override
    public boolean smaller(long value) {
        return this.getValue().smaller(value);
    }

    @Override
    public boolean smaller(double value) {
        return this.getValue().smaller(value);
    }

    @Override
    public boolean smaller(boolean value) {
        return this.getValue().smaller(value);
    }

    @Override
    public boolean smaller(String value) {
        return this.getValue().smaller(value);
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return getValue().smallerEq(memory);
    }

    @Override
    public boolean smallerEq(long value) {
        return this.getValue().smallerEq(value);
    }

    @Override
    public boolean smallerEq(double value) {
        return this.getValue().smallerEq(value);
    }

    @Override
    public boolean smallerEq(boolean value) {
        return this.getValue().smallerEq(value);
    }

    @Override
    public boolean smallerEq(String value) {
        return this.getValue().smallerEq(value);
    }

    @Override
    public boolean greater(Memory memory) {
        return getValue().greater(memory);
    }

    @Override
    public boolean greater(long value) {
        return this.getValue().greater(value);
    }

    @Override
    public boolean greater(double value) {
        return this.getValue().greater(value);
    }

    @Override
    public boolean greater(boolean value) {
        return this.getValue().greater(value);
    }

    @Override
    public boolean greater(String value) {
        return this.getValue().greater(value);
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return getValue().greaterEq(memory);
    }

    @Override
    public boolean greaterEq(long value) {
        return this.getValue().greaterEq(value);
    }

    @Override
    public boolean greaterEq(double value) {
        return this.getValue().greaterEq(value);
    }

    @Override
    public boolean greaterEq(boolean value) {
        return this.getValue().greaterEq(value);
    }

    @Override
    public boolean greaterEq(String value) {
        return this.getValue().greaterEq(value);
    }

    @Override
    public Memory bitAnd(Memory memory) {
        return this.getValue().bitAnd(memory);
    }

    @Override
    public Memory bitAnd(long memory) {
        return this.getValue().bitAnd(memory);
    }

    @Override
    public Memory bitAnd(double memory) {
        return this.getValue().bitAnd(memory);
    }

    @Override
    public Memory bitAnd(boolean memory) {
        return this.getValue().bitAnd(memory);
    }

    @Override
    public Memory bitAnd(String memory) {
        return this.getValue().bitAnd(memory);
    }

    @Override
    public Memory bitOr(Memory memory) {
        return this.getValue().bitOr(memory);
    }

    @Override
    public Memory bitOr(long memory) {
        return this.getValue().bitOr(memory);
    }

    @Override
    public Memory bitOr(double memory) {
        return this.getValue().bitOr(memory);
    }

    @Override
    public Memory bitOr(boolean memory) {
        return this.getValue().bitOr(memory);
    }

    @Override
    public Memory bitOr(String memory) {
        return this.getValue().bitOr(memory);
    }

    @Override
    public Memory bitXor(Memory memory) {
        return this.getValue().bitXor(memory);
    }

    @Override
    public Memory bitXor(long memory) {
        return this.getValue().bitXor(memory);
    }

    @Override
    public Memory bitXor(double memory) {
        return this.getValue().bitXor(memory);
    }

    @Override
    public Memory bitXor(boolean memory) {
        return this.getValue().bitXor(memory);
    }

    @Override
    public Memory bitXor(String memory) {
        return this.getValue().bitXor(memory);
    }

    @Override
    public Memory bitNot() {
        return this.getValue().bitNot();
    }

    @Override
    public Memory bitShr(Memory memory) {
        return this.getValue().bitShr(memory);
    }

    @Override
    public Memory bitShr(long memory) {
        return this.getValue().bitShr(memory);
    }

    @Override
    public Memory bitShr(double memory) {
        return this.getValue().bitShr(memory);
    }

    @Override
    public Memory bitShr(boolean memory) {
        return this.getValue().bitShr(memory);
    }

    @Override
    public Memory bitShr(String memory) {
        return this.getValue().bitShr(memory);
    }

    @Override
    public Memory bitShl(Memory memory) {
        return this.getValue().bitShl(memory);
    }

    @Override
    public Memory bitShl(long memory) {
        return this.getValue().bitShl(memory);
    }

    @Override
    public Memory bitShl(double memory) {
        return this.getValue().bitShl(memory);
    }

    @Override
    public Memory bitShl(boolean memory) {
        return this.getValue().bitShl(memory);
    }

    @Override
    public Memory bitShl(String memory) {
        return this.getValue().bitShl(memory);
    }

    @Override
    public Memory newKeyValue(Memory memory) {
        return this.getValue().newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(long memory) {
        return this.getValue().newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(double memory) {
        return this.getValue().newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(boolean memory) {
        return this.getValue().newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(String memory) {
        return this.getValue().newKeyValue(memory);
    }

    @Override
    public Memory toImmutable() {
        switch (getValue().type){
            case NULL:
            case REFERENCE:
            case ARRAY: return getValue().toImmutable();
            default: return getValue();
        }
    }

    @Override
    public <T extends Memory> T toValue(Class<T> clazz){
        switch (getValue().type){
            case REFERENCE: return getValue().toValue(clazz);
            default:
                return (T) getValue();
        }
    }

    @Override
    public Memory toValue(){
        switch (getValue().type){
            case REFERENCE: return getValue().toValue();
            default:
                return getValue();
        }
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Memory assign(Memory memory) {
        switch (getValue().type){
            case REFERENCE:
                return getValue().assign(memory);
            case ARRAY: //value.unset(); // do not need break!!
            default:
                return setValue(memory);
        }
    }

    public ReferenceMemory getReference(){
        if (getValue().type == Type.REFERENCE){
            return ((ReferenceMemory)getValue()).getReference();
        } else
            return this;
    }

    @Override
    public Memory assignRef(Memory reference){
        if (reference.isReference()){
            reference = ((ReferenceMemory)reference).getReference();
        }

        if (reference == this) {
            return reference;
        }

        setValue(reference);
        return reference;
    }

    @Override
    public Memory assign(long memory) {
        switch (getValue().type){
            case REFERENCE: return getValue().assign(memory);
            case ARRAY: getValue().unset(); // do not need break!!
            default:
                return setValue(LongMemory.valueOf(memory));
        }
    }

    @Override
    public Memory assign(String memory) {
        switch (getValue().type){
            case REFERENCE: return getValue().assign(memory);
            case ARRAY: getValue().unset(); // do not need break!!
            default:
                return setValue(StringMemory.valueOf(memory));
        }
    }

    @Override
    public Memory assign(boolean memory) {
        switch (getValue().type){
            case REFERENCE: return getValue().assign(memory);
            case ARRAY: getValue().unset(); // do not need break!!
            default:
                return setValue(memory ? TRUE : FALSE);
        }
    }

    @Override
    public Memory assign(double memory) {
        switch (getValue().type){
            case REFERENCE: return getValue().assign(memory);
            case ARRAY: getValue().unset(); // do not need break!!
            default:
                return setValue(new DoubleMemory(memory));
        }
    }

    private StringMemory typeString(){
        if (toValue().type != Type.STRING){
            assign(new StringMemory(getValue().toString()));
        }

        return (StringMemory)toImmutable();
    }

    @Override
    public byte[] getBinaryBytes(Charset charset) {
        return getValue().getBinaryBytes(charset);
    }

    @Override
    public int hashCode(){
        return getValue().hashCode();
    }

    @Override
    public void unset() {
        this.setValue(UNDEFINED);
    }

    @Override
    public void manualUnset(Environment env){
        if (this.getValue().isObject())
            this.getValue().manualUnset(env);

        this.unset();
    }

    public void needArray(){
        Type type = getRealType();
        if (type != Type.STRING && type != Type.ARRAY && type != Type.OBJECT){
            assign(new ArrayMemory());
        }
    }

    public StringBuilderMemory needStringBuilder(){
        Memory value = toValue();

        if (value instanceof StringBuilderMemory)
            return (StringBuilderMemory)value;

        StringBuilderMemory builderMemory = new StringBuilderMemory(getValue().toString());
        assign(builderMemory);
        return builderMemory;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, Memory index) {
        return getValue().valueOfIndex(trace, index);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, long index) {
        return getValue().valueOfIndex(trace, index);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, double index) {
        return getValue().valueOfIndex(trace, index);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, String index) {
        return getValue().valueOfIndex(trace, index);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, boolean index) {
        return getValue().valueOfIndex(trace, index);
    }

    @Override
    public Memory refOfPush(TraceInfo trace) {
        needArray();
        return getValue().refOfPush(trace);
    }

    @Override
    public Memory refOfIndexAsShortcut(TraceInfo trace, Memory index){
        needArray();
        switch (getValue().type){
            case STRING:
                return refOfIndex(trace, index);
            default: return getValue().refOfIndexAsShortcut(trace, index);
        }
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, Memory index) {
        needArray();
        switch (getValue().type){
            case STRING:
                if (index.isString()){
                    int _index = -1;
                    Memory tmp = StringMemory.toLong(index.toString());
                    if (tmp != null)
                        _index = tmp.toInteger();

                    return CharMemory.valueOf(this, (StringMemory)this.getValue(), _index);
                } else
                    return CharMemory.valueOf(this, (StringMemory)this.getValue(), (int)index.toNumeric().toLong());
            default: return getValue().refOfIndex(trace, index);
        }
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, long index) {
        needArray();
        switch (getValue().type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.getValue(), (int)index);
            default: return getValue().refOfIndex(trace, index);
        }
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, double index) {
        needArray();
        switch (getValue().type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.getValue(), (int)index);
            default: return getValue().refOfIndex(trace, index);
        }
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, String index) {
        needArray();
        int _index = -1;
        Memory tmp = StringMemory.toLong(index);
        if (tmp != null)
            _index = tmp.toInteger();

        switch (getValue().type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.getValue(), _index);
            default: return getValue().refOfIndex(trace, index);
        }
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, boolean index) {
        needArray();
        switch (getValue().type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.getValue(), index ? 1 : 0);
            default: return getValue().refOfIndex(trace, index);
        }
    }

    @Override
    public void unsetOfIndex(TraceInfo trace, Memory index) {
        getValue().unsetOfIndex(trace, index);
    }

    @Override
    public Memory issetOfIndex(TraceInfo trace, Memory index) {
        return getValue().issetOfIndex(trace, index);
    }

    @Override
    public Memory emptyOfIndex(TraceInfo trace, Memory index) {
        return getValue().emptyOfIndex(trace, index);
    }

    @Override
    public boolean isShortcut(){
        return getValue().isReference();
    }

    @Override
    public boolean identical(Memory memory) {
        return getValue().identical(memory);
    }

    @Override
    public boolean identical(long value) {
        return this.getValue().identical(value);
    }

    @Override
    public boolean identical(double value) {
        return this.getValue().identical(value);
    }

    @Override
    public boolean identical(boolean value) {
        return this.getValue().identical(value);
    }

    @Override
    public boolean identical(String value) {
        return this.getValue().identical(value);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return getValue().getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public Type getRealType() {
        if (getValue().type == Type.REFERENCE)
            return getValue().getRealType();

        return getValue().type;
    }

    @Override
    public Memory assignConcat(Memory memory) {
        needStringBuilder().append(memory);
        return this;
    }

    @Override
    public Memory assignConcat(long memory) {
        needStringBuilder().append(memory);
        return this;
    }

    @Override
    public Memory assignConcat(double memory) {
        needStringBuilder().append(memory);
        return this;
    }

    @Override
    public Memory assignConcat(boolean memory) {
        needStringBuilder().append(memory);
        return this;
    }

    @Override
    public Memory assignConcat(String memory) {
        needStringBuilder().append(memory);
        return this;
    }

    @Override
    public boolean instanceOf(String className, String lowerClassName) {
        return getValue().instanceOf(className, lowerClassName);
    }

    @Override
    public boolean instanceOf(String name) {
        return getValue().instanceOf(name);
    }

    @Override
    public Memory toArray() {
        return getValue().toArray();
    }

    @Override
    public Memory toObject(Environment env) {
        return getValue().toObject(env);
    }

    @Override
    public Memory clone(Environment env, TraceInfo trace) throws Throwable {
        return getValue().clone(env, trace);
    }

    @Override
    public boolean isClosure() {
        return getValue().isClosure();
    }

    @Override
    public String toBinaryString() {
        return getValue().toBinaryString();
    }

    @Override
    public boolean instanceOf(Class<? extends IObject> clazz) {
        return getValue().instanceOf(clazz);
    }

    @Override
    public <T extends IObject> T toObject(Class<T> clazz) {
        return getValue().toObject(clazz);
    }

    @Override
    public int compareTo(Memory o) {
        return getValue().compareTo(o);
    }
}
