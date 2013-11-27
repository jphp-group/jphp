package ru.regenix.jphp.runtime.ext.core;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;
import ru.regenix.jphp.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO:
 *    - addcslashes
 *    - bin2hex ?
 *    - chunk_split
 */
public class StringFunctions extends FunctionsContainer {

    @Runtime.Immutable
    public static String addslashes(String source) {
        StringBuilder sb = new StringBuilder();

        int length = source.length();
        for (int i = 0; i < length; i++) {
            char ch = source.charAt(i);

            switch (ch) {
                case 0x0:
                    sb.append("\\0");
                    break;
                case '\'':
                    sb.append("\\'");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(ch);
                    break;
            }
        }
        return sb.toString();
    }

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
        String delimiter;
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

    public static Memory explode(String delimiter, String string, int limit){
        if (limit == 0)
            limit = 1;

        String[] result = StringUtils.split(string, delimiter, limit);
        return new ArrayMemory(result);
    }

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
        char[] buffer = value.toCharArray();

        boolean prevSpace = false;
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (Character.isSpaceChar(ch)){
                prevSpace = true;
                continue;
            }
            if (prevSpace){
                buffer[i] = Character.toUpperCase(ch);
                prevSpace = false;
            } else {
                buffer[i] = Character.toLowerCase(ch);
            }
        }
        return new String(buffer);
    }

    private static final ThreadLocal<MessageDigest> md5Digest = new ThreadLocal<MessageDigest>(){
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private static final ThreadLocal<MessageDigest> sha1Digest = new ThreadLocal<MessageDigest>(){
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Runtime.Immutable
    public static String md5(Memory value)  {
        MessageDigest md = md5Digest.get();
        md.reset();
        md.update(value.getBinaryBytes());
        return DigestUtils.bytesToHex(md.digest());
    }

    @Runtime.Immutable
    public static String sha1(Memory value)  {
        MessageDigest md = sha1Digest.get();
        md.reset();
        md.update(value.getBinaryBytes());
        return DigestUtils.bytesToHex(md.digest());
    }
}
