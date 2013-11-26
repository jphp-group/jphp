package ru.regenix.jphp.runtime.ext.core;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.MemoryUtils;

/**
 * TODO:
 *    - addcslashes
 *    - addslashes
 *    - bin2hex ?
 *    - chunk_split
 */
public class StringFunctions extends FunctionsContainer {

    @Runtime.Immutable
    public static String trim(String s){
        return s.trim();
    }

    @Runtime.Immutable
    public static String ltrim(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }

    @Runtime.Immutable
    public static String rtrim(String s) {
        int i = s.length() - 1;
        while (i > 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    @Runtime.Immutable
    public static String chop(String s){
        return rtrim(s);
    }

    @Runtime.Immutable
    public static String chr(int codePoint){
        return String.valueOf((char) codePoint);
    }

    @Runtime.Immutable
    public static int ord(String value){
        if (value.isEmpty())
            return 0;
        else
            return value.codePointAt(0);
    }

    @Runtime.Immutable
    public static String implode(Memory glue, Memory pieces){
        ArrayMemory array;
        String delimiter = "";
        if (glue.isArray()) {
            array = (ArrayMemory)glue;
            delimiter = pieces.toString();
        } else if (pieces.isArray()) {
            array = (ArrayMemory)pieces;
            delimiter = glue.toString();
        } else
            return "";

        StringBuilder builder = new StringBuilder();
        int i = 0, size = array.size();
        for(Memory el : array){
            builder.append(el.toString());
            if (i != size - 1)
                builder.append(delimiter);
            i++;
        }

        return builder.toString();
    }

    @Runtime.Immutable
    public static String implode(Memory pieces){
        return implode(Memory.NULL, pieces);
    }

    @Runtime.Immutable
    public static String join(Memory glue, Memory pieces){
        return implode(glue, pieces);
    }

    @Runtime.Immutable
    public static String join(Memory pieces){
        return implode(Memory.NULL, pieces);
    }

    @Runtime.Immutable
    public static Memory explode(String delimiter, String string, int limit){
        if (limit == 0)
            limit = 1;

        String[] result = StringUtils.split(string, delimiter, limit);
        return MemoryUtils.valueOf(result);
    }

    @Runtime.Immutable
    public static Memory explode(String delimiter, String string){
        return explode(delimiter, string, Integer.MAX_VALUE);
    }

    @Runtime.Immutable
    public static String lcfirst(String value){
        if (value.isEmpty())
            return "";

        return String.valueOf(Character.toLowerCase( value.charAt(0) )) + value.substring(1);
    }

    @Runtime.Immutable
    public static String ucfirst(String value){
        if (value.isEmpty())
            return "";
        return String.valueOf(Character.toUpperCase( value.charAt(0) )) + value.substring(1);
    }

    @Runtime.Immutable
    public static String ucwords(String value){
        return StringUtils.capitalize(value);
    }
}
