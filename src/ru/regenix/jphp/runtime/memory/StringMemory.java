package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.OperatorUtils;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class StringMemory extends Memory {

    String value = "";
    StringBuilder builder = null;

    public StringMemory(String value) {
        super(Type.STRING);
        this.value = value;
    }

    public StringMemory(char ch){
        this(String.valueOf(ch));
    }

    public static Memory valueOf(String value){
        return new StringMemory(value);
    }

    @Override
    public long toLong() {
        return toNumeric().toLong();
    }

    @Override
    public double toDouble() {
        return toNumeric().toDouble();
    }

    @Override
    public boolean toBoolean() {
        return (value != null && !value.isEmpty() && !value.equals("0"));
    }

    @Override
    public String toString() {
        if (builder != null){
            value = builder.toString();
            builder = null;
        }
        return value;
    }

    public static Memory toLong(String value){
        int len = value.length();
        int i = 0;
        int start = i;
        for(; i < len; i++){
            char ch = value.charAt(i);
            if (!('9' >= ch && ch >= '0')){
                if (ch == '-'){
                    if (i == start)
                        continue;
                }
                return null;
            }
        }

        return LongMemory.valueOf(Long.parseLong(value));
    }

    public static Memory toNumeric(String value){
        int len = value.length();
        boolean real = false;
        int i = 0;
        for(; i < len; i++){
            char ch = value.charAt(i);
            if (ch > 32)
                break;
        }

        int start = i;
        boolean e_char = false;
        for(; i < len; i++){
            char ch = value.charAt(i);
            if (!('9' >= ch && ch >= '0')){
                if (ch == '-'){
                    if (i == start)
                        continue;
                }

                /*if (!e_char && ch == 'e' || ch == 'E'){
                    e_char = true;
                    continue;
                }*/

                if (ch == '.'){
                    if (real)
                        break;
                    real = true;
                    continue;
                }
                if (i == 0)
                    return CONST_INT_0;
                else
                    break;
            }
        }
        if (real) {
            if (len == i && start == 0)
                return new DoubleMemory(Double.parseDouble(value));
            else
                return new DoubleMemory(Double.parseDouble(value.substring(start, i)));
        } else {
            if (len == i && start == 0)
                return LongMemory.valueOf(Long.parseLong(value));
            else
                return LongMemory.valueOf(Long.parseLong(value.substring(start, i)));
        }
    }

    @Override
    public Memory toNumeric(){
        if (builder != null){
            value = builder.toString();
            builder = null;
        }
        return toNumeric(value);
    }

    @Override
    public Memory inc() {
        return toNumeric().inc();
    }

    @Override
    public Memory dec() {
        return toNumeric().dec();
    }

    @Override
    public Memory negative() {
        return toNumeric().negative();
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
    public Memory div(long value) {
        return toNumeric().div(value);
    }

    @Override
    public Memory div(boolean value) {
        return div(value ? 1 : 0);
    }

    @Override
    public Memory mod(Memory memory) {
        return toNumeric().mod(memory);
    }

    @Override
    public boolean identical(Memory memory) {
        return memory.type == Type.STRING && toString().equals(memory.toString());
    }

    @Override
    public boolean equal(Memory memory) {
        if (builder != null){
            value = builder.toString();
            builder = null;
        }
        switch (memory.type){
            case NULL: return value.equals("");
            case DOUBLE:
            case INT: return toNumeric().equal(memory);
            case STRING: return value.equals(memory.toString());
            case OBJECT:
            case ARRAY: return false;
            default: return equal(memory.toImmutable());
        }
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equal(memory);
    }

    @Override
    public Memory minus(long value) {
        return toNumeric().minus(value);
    }

    @Override
    public String concat(Memory memory) {
        if (builder != null){
            value = builder.toString();
            builder = null;
        }
        switch (memory.type){
            case STRING: return value.concat(memory.toString());
            case REFERENCE: return concat(memory.toImmutable());
            default:
                return (value + memory.toString());
        }
    }

    @Override
    public boolean smaller(Memory memory) {
        switch (memory.type){
            case STRING: return toString().compareTo(memory.toString()) < 0;
            case REFERENCE: return smaller(memory.toImmutable());
        }
        return toNumeric().smaller(memory);
    }

    @Override
    public boolean smaller(String value) {
        return toString().compareTo(value) < 0;
    }

    @Override
    public boolean smallerEq(Memory memory) {
        switch (memory.type){
            case STRING: return toString().compareTo(memory.toString()) <= 0;
            case REFERENCE: return smaller(memory.toImmutable());
        }
        return toNumeric().smallerEq(memory);
    }

    @Override
    public boolean smallerEq(String value) {
        return toString().compareTo(value) <= 0;
    }

    @Override
    public boolean greater(Memory memory) {
        switch (memory.type){
            case STRING: return toString().compareTo(memory.toString()) > 0;
            case REFERENCE: return smaller(memory.toImmutable());
        }
        return toNumeric().greater(memory);
    }

    @Override
    public boolean greater(String value) {
        return toString().compareTo(value) > 0;
    }

    @Override
    public boolean greaterEq(Memory memory) {
        switch (memory.type){
            case STRING: return toString().compareTo(memory.toString()) >= 0;
            case REFERENCE: return smaller(memory.toImmutable());
        }
        return toNumeric().greaterEq(memory);
    }

    @Override
    public boolean greaterEq(String value) {
        return toString().compareTo(value) >= 0;
    }

    @Override
    public String concat(String value) {
        return toString().concat(value);
    }

    @Override
    public Memory bitAnd(Memory memory) {
        if(memory.isString())
            return OperatorUtils.binaryAnd(this, memory);
        else
            return super.bitAnd(memory);
    }

    @Override
    public Memory bitAnd(String memory) {
        return OperatorUtils.binaryAnd(this, new StringMemory(memory));
    }

    @Override
    public Memory bitOr(Memory memory) {
        if(memory.isString())
            return OperatorUtils.binaryOr(this, memory);
        else
            return super.bitOr(memory);
    }

    @Override
    public Memory bitOr(String memory) {
        return OperatorUtils.binaryOr(this, new StringMemory(memory));
    }

    @Override
    public Memory bitXor(Memory memory) {
        if(memory.isString())
            return OperatorUtils.binaryXor(this, memory);
        else
            return super.bitXor(memory);
    }

    @Override
    public Memory bitXor(String memory) {
        return OperatorUtils.binaryXor(this, new StringMemory(memory));
    }

    @Override
    public Memory bitNot() {
        return OperatorUtils.binaryNot(this);
    }

    @Override
    public Memory bitShr(Memory memory) {
        if(memory.isString())
            return OperatorUtils.binaryShr(this, memory);
        else
            return super.bitShr(memory);
    }

    @Override
    public Memory bitShr(String memory) {
        return OperatorUtils.binaryShr(this, new StringMemory(memory));
    }

    @Override
    public Memory bitShl(Memory memory) {
        if(memory.isString())
            return OperatorUtils.binaryShl(this, memory);
        else
            return super.bitShl(memory);
    }

    @Override
    public Memory bitShl(String memory) {
        return OperatorUtils.binaryShl(this, new StringMemory(memory));
    }

    @Override
    public Memory bitShrRight(String value) {
        return OperatorUtils.binaryShr(new StringMemory(value), this);
    }

    @Override
    public Memory bitShlRight(String value) {
        return OperatorUtils.binaryShl(new StringMemory(value), this);
    }

    private void resolveBuilder(){
        if (builder == null){
            builder = new StringBuilder(this.value);
            this.value = null;
        }
    }

    public void append(Memory memory){
        resolveBuilder();
        switch (memory.type){
            case BOOL:
                if (memory instanceof FalseMemory)
                    break;
                else
                    builder.append(memory.toString());
                break;
            case NULL: break;
            case INT: builder.append(((LongMemory)memory).value); break;
            case DOUBLE: builder.append(((DoubleMemory)memory).value); break;
            case STRING: builder.append(memory.toString()); break;
            case REFERENCE: append(memory.toImmutable()); break;
            default:
                builder.append(memory.toString());
        }
    }

    public void append(String value){
        resolveBuilder();
        builder.append(value);
    }

    public void append(long value){
        resolveBuilder();
        builder.append(value);
    }

    public void append(double value){
        resolveBuilder();
        builder.append(value);
    }

    public void append(boolean value){
        if (value){
            resolveBuilder();
            builder.append(boolToString(value));
        }
    }

    @Override
    public Memory valueOfIndex(Memory index) {
        int _index = -1;

        switch (index.type){
            case STRING:
                Memory tmp = StringMemory.toLong(index.toString());
                if (tmp != null)
                    _index = tmp.toInteger();
                break;
            case REFERENCE: return valueOfIndex(index.toValue());
            default:
                _index = index.toInteger();
        }

        if (_index < toString().length() && _index >= 0)
            return new StringMemory(value.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(long index) {
        int _index = (int)index;
        String string = toString();
        if (_index >= 0 && _index < string.length())
            return new StringMemory(string.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(double index) {
        int _index = (int)index;
        String string = toString();
        if (_index >= 0 && _index < string.length())
            return new StringMemory(string.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(boolean index) {
        int _index = index ? 1 : 0;
        String string = toString();
        if (_index >= 0 && _index < string.length())
            return new StringMemory(string.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(String index) {
        int _index = -1;

        Memory tmp = StringMemory.toLong(index);
        if (tmp != null)
            _index = tmp.toInteger();

        String string = toString();
        if (_index >= 0 && _index < string.length())
            return new StringMemory(string.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public byte[] getBinaryBytes() {
        return toString().getBytes();
    }

    @Override
    public boolean identical(long value) {
        return false;
    }

    @Override
    public boolean identical(double value) {
        return false;
    }

    @Override
    public boolean identical(boolean value) {
        return false;
    }
}
