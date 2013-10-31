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
        if (value instanceof ReferenceMemory)
            return value.toImmutable();
        else
            return value;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public void assign(Memory memory) {
        if (value.type == Type.REFERENCE)
            value.assign(memory);
        else
            value = memory.toImmutable();
    }

    @Override
    public void assign(long value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            this.value = new LongMemory(value);
    }

    @Override
    public void assign(String value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            this.value = new StringMemory(value);
    }

    @Override
    public void assign(boolean value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            this.value = value ? TRUE : FALSE;
    }

    @Override
    public void assign(double value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            this.value = new DoubleMemory(value);
    }

    @Override
    public void assignRef(Memory memory) {
        if (memory instanceof ReferenceMemory){
            ReferenceMemory reference = (ReferenceMemory)memory;
            if (reference.value instanceof ReferenceMemory)
                value = reference.value;
            else
                value = reference;
        } else
            value = memory;
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
}
