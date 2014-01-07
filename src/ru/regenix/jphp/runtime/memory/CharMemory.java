package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.common.StringUtils;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class CharMemory extends StringMemory {
    public final ReferenceMemory origin;
    public final int index;

    public CharMemory(String value, ReferenceMemory origin, int index) {
        super(value);
        this.index = index;
        this.origin = origin;
    }

    public static Memory valueOf(ReferenceMemory origin, StringMemory memory, int index){
        if (index < 0 || index >= memory.value.length()){
            return new CharMemory("", origin, index);
        } else {
            return new CharMemory(String.valueOf(memory.value.charAt(index)), origin, index);
        }
    }

    private StringMemory makeString(String ch){
        String value = ((StringMemory)origin.value).value;

        int len = ch.length();
        if (len == 0){
            ch = "\\0";
        } else if (len > 1) {
            ch = String.valueOf(ch.charAt(0));
        }

        StringBuilder builder = new StringBuilder( value.substring(0, index) );
        int len2 = value.length();
        if (index > len2 - 1){
            builder.append(StringUtils.repeat('\32', index - len2));
        }

        builder.append(ch);

        if (index < len2)
            builder.append( value.substring(index + 1) );
        return new StringMemory(builder.toString());
    }

    @Override
    public Memory assign(Memory memory) {
        origin.assign(makeString(memory.toString()));
        return memory;
    }

    @Override
    public Memory assign(long value) {
        origin.assign(makeString(String.valueOf(value)));
        return LongMemory.valueOf(value);
    }

    @Override
    public Memory assign(double value) {
        origin.assign(makeString(String.valueOf(value)));
        return new DoubleMemory(value);
    }

    @Override
    public Memory assign(boolean value) {
        origin.assign(makeString(Memory.boolToString(value)));
        return value ? TRUE : FALSE;
    }

    @Override
    public Memory assign(String value) {
        origin.assign(makeString(value));
        return new StringMemory(value);
    }
}
