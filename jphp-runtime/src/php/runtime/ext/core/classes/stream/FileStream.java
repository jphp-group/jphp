package php.runtime.ext.core.classes.stream;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
        env.exception(WrapIOException.class, MSG_FILE_NOT_FOUND, getPath());
    }

    private void throwCannotRead(Environment env){
        env.exception(WrapIOException.class, "Cannot read file");
    }

    public RandomAccessFile getAccessFile() {
        return accessFile;
    }

    @Override
    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Reflection.Optional("r"))})
    public Memory __construct(Environment env, Memory... args) throws IOException {
        super.__construct(env, args);

        try {
            if (getMode().equals("r")) {
                accessFile = new RandomAccessFile(getPath(), "r");
                position = 0;
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
                    env.exception(WrapIOException.class, "File '%s' already exists (mode: %s)", getMode());

                accessFile = new RandomAccessFile(getPath(), "rw");
                if (getMode().equals("x"))
                    canRead = false;
            } else if (getMode().equals("c") || getMode().equals("c+")){
                accessFile = new RandomAccessFile(getPath(), "rw");
                if (getMode().equals("c"))
                    canRead = false;
            } else
                env.exception(WrapIOException.class, "Unsupported mode - '%s'", getMode());
        } catch (FileNotFoundException e){
            throwFileNotFound(env);
        } catch (IOException e) {
            env.exception(WrapIOException.class, e.getMessage());
        }

        return Memory.NULL;
    }

    @Signature({@Arg("value"), @Arg(value = "length", optional = @Optional("NULL"))})
    public Memory write(Environment env, Memory... args){
        int len = args[1].toInteger();
        byte[] bytes = args[0].getBinaryBytes(env.getDefaultCharset());

        try {
            accessFile.write(bytes, 0, len == 0 ? bytes.length : len);
            return LongMemory.valueOf(len == 0 ? bytes.length : len);
        } catch (IOException e) {
            env.exception(WrapIOException.class, e.getMessage());
        }

        return Memory.FALSE;
    }

    @Signature(@Arg("length"))
    public Memory read(Environment env, Memory... args){
        if (!canRead)
            throwCannotRead(env);

        int len = args[0].toInteger();
        if (len < 1)
            env.exception(WrapIOException.class, "Length must be greater than zero, %s given", len);

        byte[] buff = new byte[len];
        try {
            int read = accessFile.read(buff, 0, len);
            if (read == -1)
                return Memory.NULL;

            position += read;
            if (read != buff.length){
                buff = Arrays.copyOf(buff, read);
            }
            return new BinaryMemory(buff);
        } catch (IOException e) {
            env.exception(WrapIOException.class, e.getMessage());
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
    public Memory close(Environment env, Memory... args) throws IOException {
        accessFile.close();
        return Memory.NULL;
    }

    @Signature
    public Memory getPosition(Environment env, Memory... args){
        return LongMemory.valueOf(position);
    }

    @Signature(@Arg("position"))
    public Memory seek(Environment env, Memory... args) throws IOException {
        accessFile.seek(args[0].toLong());
        return Memory.NULL;
    }

    @Signature
    public Memory getFilePointer(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(accessFile.getFilePointer());
    }

    @Signature
    public Memory length(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(accessFile.length());
    }

    @Signature(@Arg("size"))
    public Memory truncate(Environment env, Memory... args) throws IOException {
        accessFile.setLength(args[0].toLong());
        return Memory.NULL;
    }
}
