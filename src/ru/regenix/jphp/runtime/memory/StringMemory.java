package ru.regenix.jphp.runtime.memory;

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
        for(; i < len; i++){
            char ch = value.charAt(i);
            if (!('9' >= ch && ch >= '0')){
                if (ch == '-'){
                    if (i == start)
                        continue;
                }

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
            //case OBJECT:
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
        if (builder != null){
            value = builder.toString();
            builder = null;
        }
        return this.value.concat(value);
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
        return new StringMemory(toString().charAt((int)index.toLong()));
    }

    @Override
    public Memory valueOfIndex(long index) {
        return new StringMemory(toString().charAt((int)index));
    }

    @Override
    public Memory valueOfIndex(double index) {
        return new StringMemory(toString().charAt((int)index));
    }

    @Override
    public Memory valueOfIndex(boolean index) {
        return new StringMemory(toString().charAt(index ? 0 : 1));
    }

    @Override
    public Memory valueOfIndex(String index) {
        return new StringMemory(toString().charAt((int)toNumeric(index).toLong()));
    }
}
