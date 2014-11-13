package php.runtime.loader.dump.io;

import php.runtime.Memory;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.helper.ClassConstantMemory;
import php.runtime.memory.helper.ConstantMemory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DumpOutputStream extends DataOutputStream {

    public DumpOutputStream(OutputStream out) {
        super(out);
    }

    public void writeName(String value) throws IOException {
        if (value == null)
            writeInt(-1);
        else {
            writeInt(value.length());
            writeUTF(value);
        }
    }

    public void writeTrace(TraceInfo trace) throws IOException {
        writeBoolean(trace != null);
        if (trace != null){
            writeInt(trace.getStartLine());
            writeInt(trace.getStartPosition());
            writeUTF(trace.getFileName());
        }
    }

    public void writeMemory(Memory memory) throws IOException {
        if (memory == null){
            writeInt(-1);
            return;
        }

        memory = memory.toValue();
        if (memory instanceof ConstantMemory){
            writeInt(-2);
            writeUTF(((ConstantMemory) memory).getName());
            return;
        } else if (memory instanceof ClassConstantMemory){
            writeInt(-3);
            writeUTF(((ClassConstantMemory) memory).getClassName());
            writeUTF(((ClassConstantMemory) memory).getName());
            return;
        }

        Memory.Type type = memory.getRealType();
        writeInt(type.ordinal());
        switch (type){
            case NULL:
                break;
            case INT:
                writeLong(memory.toLong());
                break;
            case STRING:
                writeUTF(memory.toString());
                break;
            case DOUBLE:
                writeDouble(memory.toDouble());
                break;
            case BOOL:
                writeBoolean(memory.toBoolean());
                break;
            case ARRAY:
                ArrayMemory array = memory.toValue(ArrayMemory.class);
                if (array.size() > Short.MAX_VALUE)
                    throw new DumpException("Array is too big");

                writeInt(array.size());
                ForeachIterator foreachIterator = array.foreachIterator(false, false);
                while (foreachIterator.next()){
                    Memory key = foreachIterator.getMemoryKey();
                    Memory value = foreachIterator.getValue();
                    if (value.isShortcut())
                        throw new DumpException("Cannot dump references");

                    if (value.toValue() != Memory.UNDEFINED){
                        writeMemory(key);
                        writeMemory(value.toValue());
                    }
                }
                break;
            case OBJECT:
            default:
                throw new DumpException("Cannot dump "+ type.toString() +" memory");
        }
    }

    public final static int MAX_LENGTH_OF_RAW_DATA = 1024 * 200; // 200 kb

    public void writeRawData(byte[] bytes, int max) throws IOException {
        if (bytes == null || bytes.length == 0)
            writeInt(0);
        else {
            if (bytes.length > max)
                throw new DumpException("Raw-size is too big, max " + MAX_LENGTH_OF_RAW_DATA);

            writeInt(bytes.length);
            write(bytes);
        }
    }

    public void writeRawData(byte[] bytes) throws IOException {
        writeRawData(bytes, MAX_LENGTH_OF_RAW_DATA);
    }

    public void writeEnum(Enum _enum) throws IOException {
        writeInt(_enum == null ? -1 : _enum.ordinal());
    }
}
