package php.runtime.memory.output.serialization;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.spl.Serializable;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.PropertyEntity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Serializer {

    protected final Environment env;
    protected final StringBuilder printer;
    protected final TraceInfo trace;

    public Serializer(Environment env, TraceInfo trace, StringBuilder writer) {
        this.env = env;
        this.trace = trace;
        this.printer = writer;
    }

    public void write(Memory memory){
        switch (memory.type){
            case NULL:
                writeNull();
                break;
            case BOOL:
                writeBoolean(memory);
                break;
            case INT:
                writeLong((LongMemory)memory);
                break;
            case DOUBLE:
                writeDouble((DoubleMemory)memory);
                break;
            case STRING:
                writeString((StringMemory)memory);
                break;
            case ARRAY:
                writeArray((ArrayMemory)memory);
                break;
            case OBJECT:
                writeObject((ObjectMemory)memory, new HashSet<Integer>());
                break;
            case REFERENCE:
                if (!memory.isShortcut())
                    write(memory.toValue());
                else
                    writeNull();
                break;
        }
    }

    public void writeNull(){
        printer.append("N;");
    }

    public void writeLong(LongMemory memory){
        printer.append("i:");
        printer.append(memory.toString());
        printer.append(";");
    }

    public void writeDouble(DoubleMemory memory){
        printer.append("d:");
        printer.append(memory.toString());
        printer.append(";");
    }

    public void writeBoolean(Memory memory){
        printer.append("b:")
                .append(memory.toBoolean() ? "1" : "0")
                .append(";");
    }

    public void writeString(StringMemory memory){
        String value = memory.toString();
        printer.append("s:")
                .append(value.length())
                .append(":\"")
                .append(value)
                .append("\";");
    }

    public void writeArray(ArrayMemory memory){
        writeArray(memory, new HashSet<Integer>(), true);
    }

    public void writeArray(ArrayMemory memory, Set<Integer> used, boolean appendType){
        if (used.add(memory.getPointer())){
            if (appendType)
                printer.append("a:");

            printer.append(String.valueOf(memory.size())).append(":{");

            ForeachIterator iterator = memory.foreachIterator(false, false);
            while (iterator.next()){
                Memory key = iterator.getMemoryKey();
                write(key);
                if (iterator.getValue().isReference())
                    writeNull();
                else
                    write(iterator.getValue());
            }

            printer.append("}");
            used.remove(memory.getPointer());
        } else
            writeNull();
    }

    public void writeObject(ObjectMemory memory, Set<Integer> used){
        if (used.add(memory.getPointer())){
            IObject object = memory.value;
            ClassEntity reflection = object.getReflection();
            if (object instanceof Serializable){
                Memory result;
                env.pushCall(trace, object, "serialize");
                try {
                    result = ((Serializable) object).serialize(env);

                    if (result.isNull()){
                        writeNull();
                        return;
                    }

                    if (result.isString()){
                        String value = result.toString();
                        printer.append("C:")
                                .append(reflection.getName().length())
                                .append(":\"")
                                .append(reflection.getName())
                                .append("\":")
                                .append(value.length())
                                .append(":{").append(value).append("}");

                        return;
                    } else {
                        env.exception(trace, reflection.getName() + "::serialize() must return a string or NULL");
                    }
                } finally {
                    env.popCall();
                }
            }

            ArrayMemory only = null;
            if (reflection.methodMagicSleep != null){
                env.pushCall(trace, object, reflection.methodMagicSleep.getName());
                try {
                    Memory result = reflection.methodMagicSleep.invokeDynamic(object, env, trace);
                    if (!result.isArray()){
                        env.error(
                                ErrorType.E_NOTICE,
                                "serialize(): __sleep() should return an array only containing the names of instance-variables to serialize"
                        );
                        writeNull();
                        return;
                    } else {
                        ForeachIterator iterator = result.getNewIterator(env, false, false);
                        only = new ArrayMemory(true);
                        ArrayMemory props = memory.getProperties();

                        Set<String> need = new LinkedHashSet<String>();
                        while (iterator.next()){
                            if (iterator.getValue().isNumber())
                                continue;
                            need.add(iterator.getValue().toString());
                        }

                        for(PropertyEntity e : reflection.getProperties()){
                            if (need.contains(e.getName())){
                                props.refOfIndex(e.getSpecificName());
                            }
                        }

                        iterator = result.getNewIterator(env, false, false);
                        while (iterator.next()){
                            Memory value = iterator.getValue().toValue();
                            PropertyEntity entity = reflection.findProperty(value.toString());
                            value = entity == null
                                    ? props.valueOfIndex(value).toValue()
                                    : props.valueOfIndex(entity.getSpecificName()).toValue();

                            if (value == Memory.UNDEFINED){
                                env.error(trace,
                                        ErrorType.E_NOTICE,
                                        "serialize(): \"%s\" returned as member variable from __sleep() but does not exist",
                                        iterator.getValue().toString()
                                );
                            }

                            if (entity != null)
                                only.put(entity.getSpecificName(), value);
                            else
                                only.refOfIndex(iterator.getValue()).assign(value);
                        }
                    }
                } catch (RuntimeException e){
                    throw e;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                } finally {
                    env.popCall();
                }
            }

            printer.append("O:");
            printer.append(String.valueOf(reflection.getName().length()));
            printer.append(":\"");
            printer.append(reflection.getName());
            printer.append("\":");
            if (reflection.getProperties() == null)
                writeArray(new ArrayMemory(), used, false);
            else
                writeArray(only == null ? object.getProperties() : only, used, false);
        } else
            writeNull();
    }
}
