package php.runtime.memory.output;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.Closure;
import php.runtime.memory.*;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

abstract public class Printer {

    protected boolean recursionExists = false;
    protected PrintWriter printer;
    protected final Environment env;

    public Printer(Environment env, Writer writer){
        printer = new PrintWriter(writer);
        this.env = env;
    }

    public boolean isRecursionExists() {
        return recursionExists;
    }

    abstract protected void printNull();
    abstract protected void printFalse();
    abstract protected void printTrue();
    abstract protected void printLong(LongMemory value);
    abstract protected void printDouble(DoubleMemory value);
    abstract protected void printString(StringMemory value);
    abstract protected void printArray(ArrayMemory value, int level, Set<Integer> used);
    abstract protected void printObject(ObjectMemory value, int level, Set<Integer> used);
    abstract protected void printClosure(Closure value, int level, Set<Integer> used);

    protected void printReference(ReferenceMemory reference, int level, Set<Integer> used){
        Memory value = reference.toValue();
        //if (value != Memory.UNDEFINED)
            print(reference.toValue(), level, used);
    }

    protected void print(Memory value, int level, Set<Integer> used){
        if (used == null){
            used = new HashSet<Integer>();
        }

        switch (value.type){
            case NULL:
                //if (value != Memory.UNDEFINED)
                    printNull();
                break;
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
                ObjectMemory tmp = (ObjectMemory)value;
                if (tmp.value instanceof Closure)
                    printClosure((Closure)tmp.value, level, used);
                else
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

    public static class UnsupportedValue extends RuntimeException {

    }
}
