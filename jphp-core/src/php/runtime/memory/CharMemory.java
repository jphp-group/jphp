package php.runtime.memory;

import php.runtime.common.StringUtils;
import php.runtime.Memory;
import php.runtime.memory.helper.CharArrayMemory;

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

    private void makeString(String ch){
        Memory v = origin.toValue();

        if (v instanceof CharArrayMemory) {
            CharArrayMemory charArray = (CharArrayMemory)v;
            charArray.put(index, ch);
        } else {
            String value = v.toString();

            int len = ch.length();
            if (len == 0){
                ch = "\0";
            } else if (len > 1) {
                ch = String.valueOf(ch.charAt(0));
            }

            int len2 = value.length();
            StringBuilder builder = new StringBuilder( index > len2 - 1 ? value : value.substring(0, index) );
            if (index > len2 - 1){
                builder.append(StringUtils.repeat('\32', index - len2));
            }

            builder.append(ch);

            if (index < len2)
                builder.append( value.substring(index + 1) );
            origin.assign(new CharArrayMemory(builder.toString()));
        }
    }

    @Override
    public Memory assign(Memory memory) {
        makeString(memory.toString());
        return memory;
    }

    @Override
    public Memory assign(long value) {
        makeString(String.valueOf(value));
        return LongMemory.valueOf(value);
    }

    @Override
    public Memory assign(double value) {
        makeString(String.valueOf(value));
        return new DoubleMemory(value);
    }

    @Override
    public Memory assign(boolean value) {
        makeString(Memory.boolToString(value));
        return value ? TRUE : FALSE;
    }

    @Override
    public Memory assign(String value) {
        makeString(value);
        return new StringMemory(value);
    }
}
