package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class ReferenceMemory extends Memory {

    public Memory value;

    protected ReferenceMemory(Type type, Memory value) {
        super(type);
        this.value = value;
    }

    public ReferenceMemory(Memory value) {
        super(Type.REFERENCE);
        this.value = value == null ? Memory.NULL : value;
    }

    public static Memory valueOf(Memory value){
        return new ReferenceMemory(value);
    }

    public ReferenceMemory() {
        super(Type.REFERENCE);
        this.value = Memory.NULL;
    }

    public ReferenceMemory duplicate(){
        return new ReferenceMemory(value);
    }

    @Override
    public int getPointer(boolean absolute) {
        return absolute ? value.getPointer() : getPointer();
    }

    @Override
    public long toLong() {
        return value.toLong();
    }

    @Override
    public double toDouble() {
        return value.toDouble();
    }

    @Override
    public boolean toBoolean() {
        return value.toBoolean();
    }

    @Override
    public Memory toNumeric() {
        return value.toNumeric();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public char toChar() {
        return value.toChar();
    }

    @Override
    public boolean isNull() {
        return value.isNull();
    }

    @Override
    public boolean isObject() {
        return value.isObject();
    }

    @Override
    public boolean isArray() {
        return value.isArray();
    }

    @Override
    public boolean isString() {
        return value.isString();
    }

    @Override
    public boolean isNumber() {
        return value.isNumber();
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public Memory inc() {
        return value.inc();
    }

    @Override
    public Memory dec() {
        return value.dec();
    }

    @Override
    public Memory negative() {
        return value.negative();
    }

    @Override
    public Memory plus(Memory memory) {
        return value.plus(memory);
    }

    @Override
    public Memory plus(long value) {
        return this.value.plus(value);
    }

    @Override
    public Memory plus(double value) {
        return this.value.plus(value);
    }

    @Override
    public Memory plus(boolean value) {
        return this.value.plus(value);
    }

    @Override
    public Memory plus(String value) {
        return this.value.plus(value);
    }

    @Override
    public Memory minus(Memory memory) {
        return value.minus(memory);
    }

    @Override
    public Memory minus(double value) {
        return this.value.minus(value);
    }

    @Override
    public Memory minus(boolean value) {
        return this.value.minus(value);
    }

    @Override
    public Memory minus(String value) {
        return this.value.minus(value);
    }

    @Override
    public Memory minus(long value) {
        return this.value.minus(value);
    }

    @Override
    public Memory mul(Memory memory) {
        return value.mul(memory);
    }

    @Override
    public Memory mul(long value) {
        return this.value.mul(value);
    }

    @Override
    public Memory mul(double value) {
        return this.value.mul(value);
    }

    @Override
    public Memory mul(boolean value) {
        return this.value.mul(value);
    }

    @Override
    public Memory mul(String value) {
        return this.value.mul(value);
    }

    @Override
    public Memory div(Memory memory) {
        return value.div(memory);
    }

    @Override
    public Memory div(long value) {
        return this.value.div(value);
    }

    @Override
    public Memory div(double value) {
        return this.value.div(value);
    }

    @Override
    public Memory div(boolean value) {
        return this.value.div(value);
    }

    @Override
    public Memory div(String value) {
        return this.value.div(value);
    }

    @Override
    public Memory mod(Memory memory) {
        return value.mod(memory);
    }

    @Override
    public Memory mod(long value) {
        return this.value.mod(value);
    }

    @Override
    public Memory mod(double value) {
       return this.value.mod(value);
    }

    @Override
    public Memory mod(boolean value) {
        return this.value.mod(value);
    }

    @Override
    public Memory mod(String value) {
        return this.value.mod(value);
    }

    @Override
    public boolean equal(Memory memory) {
        return value.equal(memory);
    }

    @Override
    public boolean equal(long value) {
        return this.value.equal(value);
    }

    @Override
    public boolean equal(double value) {
        return this.value.equal(value);
    }

    @Override
    public boolean equal(boolean value) {
        return this.value.equal(value);
    }

    @Override
    public boolean equal(String value) {
        return this.value.equal(value);
    }

    @Override
    public boolean notEqual(Memory memory) {
        return value.notEqual(memory);
    }

    @Override
    public boolean notEqual(long value) {
        return this.value.notEqual(value);
    }

    @Override
    public boolean notEqual(double value) {
        return this.value.notEqual(value);
    }

    @Override
    public boolean notEqual(boolean value) {
        return this.value.notEqual(value);
    }

    @Override
    public boolean notEqual(String value) {
        return this.value.notEqual(value);
    }

    @Override
    public String concat(Memory memory) {
        return value.concat(memory);
    }

    @Override
    public String concat(long value) {
        return this.value.concat(value);
    }

    @Override
    public String concat(double value) {
        return this.value.concat(value);
    }

    @Override
    public String concat(boolean value) {
        return this.value.concat(value);
    }

    @Override
    public String concat(String value) {
        return this.value.concat(value);
    }

    @Override
    public boolean smaller(Memory memory) {
        return value.smaller(memory);
    }

    @Override
    public boolean smaller(long value) {
        return this.value.smaller(value);
    }

    @Override
    public boolean smaller(double value) {
        return this.value.smaller(value);
    }

    @Override
    public boolean smaller(boolean value) {
        return this.value.smaller(value);
    }

    @Override
    public boolean smaller(String value) {
        return this.value.smaller(value);
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return value.smallerEq(memory);
    }

    @Override
    public boolean smallerEq(long value) {
        return this.value.smallerEq(value);
    }

    @Override
    public boolean smallerEq(double value) {
        return this.value.smallerEq(value);
    }

    @Override
    public boolean smallerEq(boolean value) {
        return this.value.smallerEq(value);
    }

    @Override
    public boolean smallerEq(String value) {
        return this.value.smallerEq(value);
    }

    @Override
    public boolean greater(Memory memory) {
        return value.greater(memory);
    }

    @Override
    public boolean greater(long value) {
        return this.value.greater(value);
    }

    @Override
    public boolean greater(double value) {
        return this.value.greater(value);
    }

    @Override
    public boolean greater(boolean value) {
        return this.value.greater(value);
    }

    @Override
    public boolean greater(String value) {
        return this.value.greater(value);
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return value.greaterEq(memory);
    }

    @Override
    public boolean greaterEq(long value) {
        return this.value.greaterEq(value);
    }

    @Override
    public boolean greaterEq(double value) {
        return this.value.greaterEq(value);
    }

    @Override
    public boolean greaterEq(boolean value) {
        return this.value.greaterEq(value);
    }

    @Override
    public boolean greaterEq(String value) {
        return this.value.greaterEq(value);
    }

    @Override
    public Memory bitAnd(Memory memory) {
        return this.value.bitAnd(memory);
    }

    @Override
    public Memory bitAnd(long memory) {
        return this.value.bitAnd(memory);
    }

    @Override
    public Memory bitAnd(double memory) {
        return this.value.bitAnd(memory);
    }

    @Override
    public Memory bitAnd(boolean memory) {
        return this.value.bitAnd(memory);
    }

    @Override
    public Memory bitAnd(String memory) {
        return this.value.bitAnd(memory);
    }

    @Override
    public Memory bitOr(Memory memory) {
        return this.value.bitOr(memory);
    }

    @Override
    public Memory bitOr(long memory) {
        return this.value.bitOr(memory);
    }

    @Override
    public Memory bitOr(double memory) {
        return this.value.bitOr(memory);
    }

    @Override
    public Memory bitOr(boolean memory) {
        return this.value.bitOr(memory);
    }

    @Override
    public Memory bitOr(String memory) {
        return this.value.bitOr(memory);
    }

    @Override
    public Memory bitXor(Memory memory) {
        return this.value.bitXor(memory);
    }

    @Override
    public Memory bitXor(long memory) {
        return this.value.bitXor(memory);
    }

    @Override
    public Memory bitXor(double memory) {
        return this.value.bitXor(memory);
    }

    @Override
    public Memory bitXor(boolean memory) {
        return this.value.bitXor(memory);
    }

    @Override
    public Memory bitXor(String memory) {
        return this.value.bitXor(memory);
    }

    @Override
    public Memory bitNot() {
        return this.value.bitNot();
    }

    @Override
    public Memory bitShr(Memory memory) {
        return this.value.bitShr(memory);
    }

    @Override
    public Memory bitShr(long memory) {
        return this.value.bitShr(memory);
    }

    @Override
    public Memory bitShr(double memory) {
        return this.value.bitShr(memory);
    }

    @Override
    public Memory bitShr(boolean memory) {
        return this.value.bitShr(memory);
    }

    @Override
    public Memory bitShr(String memory) {
        return this.value.bitShr(memory);
    }

    @Override
    public Memory bitShl(Memory memory) {
        return this.value.bitShl(memory);
    }

    @Override
    public Memory bitShl(long memory) {
        return this.value.bitShl(memory);
    }

    @Override
    public Memory bitShl(double memory) {
        return this.value.bitShl(memory);
    }

    @Override
    public Memory bitShl(boolean memory) {
        return this.value.bitShl(memory);
    }

    @Override
    public Memory bitShl(String memory) {
        return this.value.bitShl(memory);
    }

    @Override
    public Memory newKeyValue(Memory memory) {
        return this.value.newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(long memory) {
        return this.value.newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(double memory) {
        return this.value.newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(boolean memory) {
        return this.value.newKeyValue(memory);
    }

    @Override
    public Memory newKeyValue(String memory) {
        return this.value.newKeyValue(memory);
    }



    @Override
    public Memory toImmutable() {
        switch (value.type){
            case REFERENCE:
            case ARRAY: return value.toImmutable();
            default: return value;
        }
    }

    public <T extends Memory> T toValue(Class<T> clazz){
        switch (value.type){
            case REFERENCE: return value.toValue(clazz);
            default:
                return (T) value;
        }
    }

    public Memory toValue(){
        switch (value.type){
            case REFERENCE: return value.toValue();
            default:
                return value;
        }
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Memory assign(Memory memory) {
        switch (value.type){
            case REFERENCE: return value.assign(memory);
            case ARRAY: value.unset(); // do not need break!!
            default:
                return value = memory;
        }
    }

    private ReferenceMemory getReference(){
        if (value.type == Type.REFERENCE){
            return ((ReferenceMemory)value).getReference();
        } else
            return this;
    }

    @Override
    public Memory assignRef(Memory reference){
        if (reference.isReference()){
            reference = ((ReferenceMemory)reference).getReference();
        }
        value = reference;
        return reference;
    }

    @Override
    public Memory assign(long memory) {
        switch (value.type){
            case REFERENCE: return value.assign(memory);
            case ARRAY: value.unset(); // do not need break!!
            default:
                return value = LongMemory.valueOf(memory);
        }
    }

    @Override
    public Memory assign(String memory) {
        switch (value.type){
            case REFERENCE: return value.assign(memory);
            case ARRAY: value.unset(); // do not need break!!
            default:
                return value = new StringMemory(memory);
        }
    }

    @Override
    public Memory assign(boolean memory) {
        switch (value.type){
            case REFERENCE: return value.assign(memory);
            case ARRAY: value.unset(); // do not need break!!
            default:
                return value = memory ? TRUE : FALSE;
        }
    }

    @Override
    public Memory assign(double memory) {
        switch (value.type){
            case REFERENCE: return value.assign(memory);
            case ARRAY: value.unset(); // do not need break!!
            default:
                return value = new DoubleMemory(memory);
        }
    }

    private StringMemory typeString(){
        if (toImmutable().type != Type.STRING){
            assign(new StringMemory(value.toString()));
        }

        return (StringMemory)toImmutable();
    }

    @Override
    public byte[] getBinaryBytes() {
        return value.getBinaryBytes();
    }

    @Override
    public int hashCode(){
        return value.hashCode();
    }

    @Override
    public void unset() {
        this.value = NULL;
    }

    public void needArray(){
        if (value.type == Type.NULL){
            value = new ArrayMemory();
        }
    }

    @Override
    public Memory valueOfIndex(Memory index) {
        return value.valueOfIndex(index);
    }

    @Override
    public Memory valueOfIndex(long index) {
        return value.valueOfIndex(index);
    }

    @Override
    public Memory valueOfIndex(double index) {
        return value.valueOfIndex(index);
    }

    @Override
    public Memory valueOfIndex(String index) {
        return value.valueOfIndex(index);
    }

    @Override
    public Memory valueOfIndex(boolean index) {
        return value.valueOfIndex(index);
    }

    @Override
    public Memory refOfPush() {
        needArray();
        return value.refOfPush();
    }

    @Override
    public Memory refOfIndex(Memory index) {
        needArray();
        switch (value.type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.value, (int)index.toNumeric().toLong());
            default: return value.refOfIndex(index);
        }
    }

    @Override
    public Memory refOfIndex(long index) {
        needArray();
        switch (value.type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.value, (int)index);
            default: return value.refOfIndex(index);
        }
    }

    @Override
    public Memory refOfIndex(double index) {
        needArray();
        switch (value.type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.value, (int)index);
            default: return value.refOfIndex(index);
        }
    }

    @Override
    public Memory refOfIndex(String index) {
        needArray();
        switch (value.type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.value, (int)value.toNumeric().toLong());
            default: return value.refOfIndex(index);
        }
    }

    @Override
    public Memory refOfIndex(boolean index) {
        needArray();
        switch (value.type){
            case STRING: return CharMemory.valueOf(this, (StringMemory)this.value, index ? 1 : 0);
            default: return value.refOfIndex(index);
        }
    }

    @Override
    public boolean isShortcut(){
        return value.isReference();
    }

    @Override
    public boolean identical(Memory memory) {
        return value.identical(memory);
    }

    @Override
    public boolean identical(long value) {
        return this.value.identical(value);
    }

    @Override
    public boolean identical(double value) {
        return this.value.identical(value);
    }

    @Override
    public boolean identical(boolean value) {
        return this.value.identical(value);
    }

    @Override
    public boolean identical(String value) {
        return this.value.identical(value);
    }

    @Override
    public ForeachIterator getNewIterator(boolean getReferences, boolean getKeyReferences) {
        return value.getNewIterator(getReferences, getKeyReferences);
    }

    @Override
    public Type getRealType() {
        return value.type;
    }
}
