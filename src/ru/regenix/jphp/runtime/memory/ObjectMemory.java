package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.lang.PHPObject;

public class ObjectMemory extends Memory {

    public PHPObject value;

    public ObjectMemory() {
        super(Type.OBJECT);
    }

    public ObjectMemory(PHPObject object){
        this();
        this.value = object;
    }

    public static ObjectMemory valueOf(PHPObject object){
        return new ObjectMemory(object);
    }

    @Override
    public long toLong() {
        return 0;
    }

    @Override
    public double toDouble() {
        return 0;
    }

    @Override
    public boolean toBoolean() {
        return true;
    }

    @Override
    public Memory toNumeric() {
        return CONST_INT_0;
    }

    @Override
    public String toString() {
        return "Object";
    }

    @Override
    public Memory inc() {
        return CONST_INT_1;
    }

    @Override
    public Memory dec() {
        return CONST_INT_M1;
    }

    @Override
    public Memory negative() {
        return CONST_INT_0;
    }

    @Override
    public Memory plus(Memory memory) {
        return toNumeric().plus(memory);
    }

    @Override
    public Memory minus(Memory memory) {
        return toNumeric().minus(memory);
    }

    @Override
    public Memory mul(Memory memory) {
        return toNumeric().mul(memory);
    }

    @Override
    public Memory div(Memory memory) {
        return toNumeric().div(memory);
    }

    @Override
    public Memory mod(Memory memory) {
        return toNumeric().mod(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        return false;
    }

    @Override
    public boolean notEqual(Memory memory) {
        return false;
    }

    @Override
    public boolean smaller(Memory memory) {
        return false;
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return false;
    }

    @Override
    public boolean greater(Memory memory) {
        return false;
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return false;
    }
}
