package ru.regenix.jphp.compiler.jvm.runtime.memory;

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
    public Memory inc(Memory memory) {
        return value.inc(memory);
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
        return value;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public void assign(Memory memory) {
        if(!value.isImmutable())
            value.assign(memory);
        else
            value = memory;
    }

    @Override
    public void assign(long value) {
        this.value = new LongMemory(value);
    }

    @Override
    public void assign(String value) {
        this.value = new StringMemory(value);
    }

    @Override
    public void assignRef(Memory memory) {
        value = memory;
    }

    @Override
    public Memory minus(long value) {
        return this.value.minus(value);
    }

    private StringMemory typeString(){
        if (value.type != Type.STRING)
            value = new StringMemory(value.toString());

        return (StringMemory)value;
    }

    @Override
    public void concatAssign(Memory memory) {
        typeString().concat(memory);
    }

    @Override
    public void concatAssign(String value) {
        typeString().concat(value);
    }

    @Override
    public void concatAssign(long value) {
        typeString().concat(value);
    }

    @Override
    public void concatAssign(double value) {
        typeString().concat(value);
    }

    @Override
    public void concatAssign(boolean value) {
        typeString().concat(value);
    }
}
