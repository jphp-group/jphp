package php.runtime.ext.core.stream;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.io.IOException;
import java.util.Arrays;

import static php.runtime.annotation.Reflection.*;

@Name("php\\io\\FileStream")
public class FileStream extends Stream {
    private final static String MSG_FILE_NOT_FOUND = "File '%s' not found";

    protected RandomAccessFile accessFile;
    protected boolean canRead = true;
    protected long position = 0;

    public FileStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    private void throwFileNotFound(Environment env){
        exception(env, MSG_FILE_NOT_FOUND, getPath());
    }

    private void throwCannotRead(Environment env){
        exception(env, "Cannot read file");
    }

    @Override
    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Reflection.Optional("r"))})
    public Memory __construct(Environment env, Memory... args) {
        super.__construct(env, args);

        try {
            if (getMode().equals("r")) {
                accessFile = new RandomAccessFile(getPath(), "r");
            } else if (getMode().equals("r+")){
                if (!new File(getPath()).getAbsoluteFile().exists())
                    throwFileNotFound(env);
                accessFile = new RandomAccessFile(getPath(), "rw");
            } else if (getMode().equals("w")){
                accessFile = new RandomAccessFile(getPath(), "rw");
                accessFile.setLength(0);
                canRead = false;
            } else if (getMode().equals("w+")){
                accessFile = new RandomAccessFile(getPath(), "rw");
                accessFile.setLength(0);
            } else if (getMode().equals("a")){
                accessFile = new RandomAccessFile(getPath(), "rw");
                File file = new File(getPath());
                if (file.getAbsoluteFile().exists()) {
                    accessFile.seek(file.length());
                    position = file.length();
                }

                canRead = false;
            } else if (getMode().equals("a+")){
                accessFile = new RandomAccessFile(getPath(), "rw");
                File file = new File(getPath());
                if (file.getAbsoluteFile().exists()){
                    accessFile.seek(file.length());
                    position = file.length();
                }
            } else if (getMode().equals("x") || getMode().equals("x+")){
                File file = new File(getPath());
                if (file.getAbsoluteFile().exists())
                    exception(env, "File '%s' already exists (mode: %s)", getMode());

                accessFile = new RandomAccessFile(getPath(), "rw");
                if (getMode().equals("x"))
                    canRead = false;
            } else if (getMode().equals("c") || getMode().equals("c+")){
                accessFile = new RandomAccessFile(getPath(), "rw");
                if (getMode().equals("c"))
                    canRead = false;
            } else
                exception(env, "Unsupported mode - '%s'", getMode());
        } catch (FileNotFoundException e){
            throwFileNotFound(env);
        } catch (java.io.IOException e) {
            exception(env, e.getMessage());
        }

        return Memory.NULL;
    }


    @Signature({@Arg("value"), @Arg(value = "length", optional = @Optional("NULL"))})
    public Memory write(Environment env, Memory... args){
        int len = args[1].toInteger();
        byte[] bytes = args[0].getBinaryBytes();

        try {
            accessFile.write(bytes, 0, len == 0 ? bytes.length : len);
            return LongMemory.valueOf(len == 0 ? bytes.length : len);
        } catch (IOException e) {
            exception(env, e.getMessage());
        }

        return Memory.FALSE;
    }

    @Signature(@Arg("length"))
    public Memory read(Environment env, Memory... args){
        if (!canRead)
            throwCannotRead(env);

        int len = args[0].toInteger();
        if (len < 1)
            exception(env, "Length must be greater than zero, %s given", len);

        byte[] buff = new byte[len];
        try {
            int read = accessFile.read(buff, 0, len);
            position += read;
            if (read != buff.length){
                buff = Arrays.copyOf(buff, len);
            }
            return new BinaryMemory(buff);
        } catch (IOException e) {
            exception(env, e.getMessage());
            return Memory.FALSE;
        }
    }

    @Signature
    public Memory readFully(Environment env, Memory... args){
        if (!canRead)
            throwCannotRead(env);
        long len = 0;
        try {
            len = accessFile.length() - position;

            if (len <= 0)
                return Memory.FALSE;

            byte[] buff = new byte[(int)len];
            accessFile.readFully(buff);
            position += len;

            return new BinaryMemory(buff);
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }

    @Signature
    public Memory eof(Environment env, Memory... args){
        try {
            return position >= accessFile.length() ? Memory.TRUE : Memory.FALSE;
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }

    @Signature
    public Memory close(Environment env, Memory... args){
        try {
            accessFile.close();
        } catch (IOException e) {
            exception(env, e.getMessage());
        }
        return Memory.NULL;
    }

    @Signature
    public Memory getPosition(Environment env, Memory... args){
        return LongMemory.valueOf(position);
    }

    @Signature(@Arg("position"))
    public Memory seek(Environment env, Memory... args){
        try {
            accessFile.seek(args[0].toLong());
        } catch (IOException e) {
            exception(env, e.getMessage());
        }
        return Memory.NULL;
    }
}
