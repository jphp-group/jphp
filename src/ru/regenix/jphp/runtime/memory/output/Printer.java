package ru.regenix.jphp.runtime.memory.output;

import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

abstract public class Printer {

    protected PrintWriter printer;

    public Printer(Writer writer){
        printer = new PrintWriter(writer);
    }

    abstract protected void printNull();
    abstract protected void printFalse();
    abstract protected void printTrue();
    abstract protected void printLong(LongMemory value);
    abstract protected void printDouble(DoubleMemory value);
    abstract protected void printString(StringMemory value);
    abstract protected void printArray(ArrayMemory value, int level, Set<Integer> used);
    abstract protected void printObject(ObjectMemory value, int level, Set<Integer> used);

    protected void printReference(ReferenceMemory reference, int level, Set<Integer> used){
        print(reference.toValue(), level, used);
    }

    protected void print(Memory value, int level, Set<Integer> used){
        if (used == null){
            used = new HashSet<Integer>();
        }

        switch (value.type){
            case NULL: printNull(); break;
            case BOOL:
                if (value instanceof TrueMemory)
                    printTrue();
                else
                    printFalse();
                break;
            case INT: printLong((LongMemory)value); break;
            case DOUBLE: printDouble((DoubleMemory) value); break;
            case STRING: printString((StringMemory) value); break;
            case ARRAY:
                printArray((ArrayMemory) value, level, used);
                break;
            case OBJECT:
                printObject((ObjectMemory) value, level, used);
                break;
            case REFERENCE: printReference((ReferenceMemory)value, level, used); break;
            default:
                throw new IllegalArgumentException("Unsupported type for printing: " + value.type);
        }
    }

    public void print(Memory value){
        print(value, 0, null);
    }
}
