package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.memory.support.Memory;

public class ReferenceMemory extends Memory {

    public Memory value;

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
    public Memory minus(Memory memory) {
        return value.minus(memory);
    }

    @Override
    public Memory mul(Memory memory) {
        return value.mul(memory);
    }

    @Override
    public Memory div(Memory memory) {
        return value.div(memory);
    }

    @Override
    public Memory mod(Memory memory) {
        return value.mod(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        return value.equal(memory);
    }

    @Override
    public boolean notEqual(Memory memory) {
        return value.notEqual(memory);
    }

    @Override
    public String concat(Memory memory) {
        return value.concat(memory);
    }

    @Override
    public boolean smaller(Memory memory) {
        return value.smaller(memory);
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return value.smallerEq(memory);
    }

    @Override
    public boolean greater(Memory memory) {
        return value.greater(memory);
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return value.greaterEq(memory);
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

    @Override
    public Memory minus(long value) {
        return this.value.minus(value);
    }

    private StringMemory typeString(){
        if (toImmutable().type != Type.STRING){
            assign(new StringMemory(value.toString()));
        }

        return (StringMemory)toImmutable();
    }

    @Override
    public void concatAssign(Memory memory) {
        typeString().concatAssign(memory);
    }

    @Override
    public void concatAssign(String value) {
        typeString().append(value);
    }

    @Override
    public void concatAssign(long value) {
        typeString().append(value);
    }

    @Override
    public void concatAssign(double value) {
        typeString().append(value);
    }

    @Override
    public void concatAssign(boolean value) {
        typeString().append(value);
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
}
