package php.runtime.memory.support;

import php.runtime.Memory;

public class MemoryStringUtils {
    public static byte[] getBytes(Memory memory){
        return memory.toString().getBytes();
    }

    public static byte[] getBinaryBytes(char ch){
        return new byte[]{ (byte) (ch & 0xFF) };
    }

    public static byte[] getBinaryBytes(String value){
        int len = value.length();

        byte[] bytes = new byte[len];
        for(int i = 0; i < len; i++){
            char ch = value.charAt(i);
            bytes[i] = (byte) (ch & 0xFF);
        }

        return bytes;
    }

    public static byte[] getBinaryBytes(Memory memory){
        return getBinaryBytes(memory.toString());
    }
}
