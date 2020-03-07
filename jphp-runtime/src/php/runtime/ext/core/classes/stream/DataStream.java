package php.runtime.ext.core.classes.stream;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Reflection.Name("php\\io\\DataStream")
public class DataStream extends BaseObject {
    private Stream stream;

    private volatile DataInputStream dataInput;
    private volatile DataOutputStream dataOutput;

    public DataStream(Environment env) {
        super(env);
    }

    public DataStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "stream", nativeType = Stream.class),
    })
    public Memory __construct(Environment env, Memory... args) {
        stream = args[0].toObject(Stream.class);
        return Memory.NULL;
    }

    public DataInputStream getDataInput(Environment env) {
        if (dataInput != null) return dataInput;

        synchronized (this) {
            if (dataInput != null) return dataInput;

            return dataInput = new DataInputStream(Stream.getInputStream(env, stream));
        }
    }

    public DataOutputStream getDataOutput(Environment env) {
        if (dataOutput != null) return dataOutput;

        synchronized (this) {
            if (dataOutput != null) return dataOutput;

            return dataOutput = new DataOutputStream(Stream.getOutputStream(env, stream));
        }
    }

    @Signature
    public Memory readByte(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(getDataInput(env).readByte());
    }

    @Signature
    public Memory readShort(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(getDataInput(env).readShort());
    }

    @Signature
    public Memory readUnsignedShort(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(getDataInput(env).readUnsignedShort());
    }

    @Signature
    public Memory readInt(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(getDataInput(env).readInt());
    }

    @Signature
    public Memory readLong(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(getDataInput(env).readLong());
    }

    @Signature
    public Memory readFloat(Environment env, Memory... args) throws IOException {
        return DoubleMemory.valueOf(getDataInput(env).readFloat());
    }

    @Signature
    public Memory readDouble(Environment env, Memory... args) throws IOException {
        return DoubleMemory.valueOf(getDataInput(env).readDouble());
    }

    @Signature
    public Memory readUnsignedByte(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(getDataInput(env).readUnsignedByte());
    }

    @Signature
    public Memory readBool(Environment env, Memory... args) throws IOException {
        return TrueMemory.valueOf(getDataInput(env).readBoolean());
    }

    @Signature
    public Memory readUTF(Environment env, Memory... args) throws IOException {
        return StringMemory.valueOf(getDataInput(env).readUTF());
    }

    @Signature
    public Memory readChar(Environment env, Memory... args) throws IOException {
        return StringMemory.valueOf(getDataInput(env).readChar());
    }

    @Signature
    public Memory read(Environment env, Memory... args) throws IOException {
        return LongMemory.valueOf(getDataInput(env).read());
    }

    @Signature(@Arg(value = "value", type = HintType.INT))
    public Memory write(Environment env, Memory... args) throws IOException {
        getDataOutput(env).write(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.INT))
    public Memory writeByte(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeByte(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.INT))
    public Memory writeShort(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeShort(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.INT))
    public Memory writeInt(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeInt(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.INT))
    public Memory writeLong(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeLong(args[0].toLong());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.DOUBLE))
    public Memory writeFloat(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeFloat(args[0].toFloat());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.DOUBLE))
    public Memory writeDouble(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeDouble(args[0].toDouble());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.STRING))
    public Memory writeChar(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeChar(args[0].toChar());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.STRING))
    public Memory writeBinary(Environment env, Memory... args) throws IOException {
        getDataOutput(env).write(args[0].getBinaryBytes(env.getDefaultCharset()));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.BOOLEAN))
    public Memory writeBool(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeBoolean(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.STRING))
    public Memory writeUTF(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeUTF(args[0].toString());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "value", type = HintType.STRING))
    public Memory writeChars(Environment env, Memory... args) throws IOException {
        getDataOutput(env).writeChars(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    public Memory close(Environment env, Memory... args) throws IOException {
        if (dataInput != null) {
            dataInput.close();
        }

        if (dataOutput != null) {
            dataOutput.close();
        }

        return Memory.NULL;
    }
}
