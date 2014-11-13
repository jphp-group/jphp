package php.runtime.memory.output;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.lang.Closure;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.io.Writer;
import java.util.Set;

public class VarExport extends Printer {

    private final static int PRINT_INDENT = 2;

    public VarExport(Environment env, Writer writer) {
        super(env, writer);
    }

    @Override
    protected void printNull() {
        printer.append("NULL");
    }

    @Override
    protected void printFalse() {
        printer.append("false");
    }

    @Override
    protected void printTrue() {
        printer.append("true");
    }

    @Override
    protected void printLong(LongMemory value) {
        printer.print(value.toLong() + "");
    }

    @Override
    protected void printDouble(DoubleMemory value) {
        printer.print(value.toDouble() + "");
    }

    @Override
    protected void printString(StringMemory value) {
        String v = value.toString();
        int length = v.length();

        printer.append('\'');
        for(int i = 0; i < length; i++){
            char ch = v.charAt(i);
            if (ch == '\'')
                printer.append("\\'");
            else if (ch == '\0'){
                printer.append("' . \"\\0\" . '");
            } else
                printer.append(ch);
        }

        printer.append('\'');
    }

    @Override
    protected void printArray(ArrayMemory value, int level, Set<Integer> used) {
        printArray(value, level, used, false);
    }

    protected void printArray(ArrayMemory value, int level, Set<Integer> used, boolean stripNulls) {
        if (!used.add(value.getPointer())){
            recursionExists = true;
            printNull();
        } else {
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
            printer.write("array (\n");

            ForeachIterator iterator = value.foreachIterator(false, false);

            level += 1;
            while (iterator.next()){
                Memory el = iterator.getValue();
                if (el == Memory.UNDEFINED) continue;

                printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
                Memory key = iterator.getMemoryKey();

                if (key.isString()){
                    String k = key.toString();
                    if (stripNulls){
                        int pos = k.lastIndexOf('\0');
                        if (pos > -1)
                            k = k.substring(pos + 1);
                    }

                    printString(new StringMemory(k));
                } else {
                    printer.write(key.toString());
                }

                printer.write(" =>");

                if (el.isArray()) {
                    if (!used.contains(el.getPointer()))
                        printer.write("\n");
                    else
                        printer.write(" ");
                    print(el, level, used);
                } else {
                    printer.write(" ");
                    print(el, level + 1, used);
                }

                printer.append(",\n");
            }
            level -= 1;
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
            printer.append(")");
            used.remove(value.getPointer());
        }
    }

    @Override
    protected void printObject(ObjectMemory value, int level, Set<Integer> used) {
        if (used.contains(value.getPointer())){
            recursionExists = true;
            printNull();
        } else {
            used.add(value.getPointer());

            ClassEntity entity = value.getReflection();
            printer.write(entity.getName());
            printer.write("::");
            printer.write("__set_state(");
            printArray(value.value.getProperties(), 0, used, true);
            printer.write(")");

            used.remove(value.getPointer());
        }
    }

    @Override
    protected void printClosure(Closure value, int level, Set<Integer> used) {
        printObject(new ObjectMemory(value), level, used);
    }
}
