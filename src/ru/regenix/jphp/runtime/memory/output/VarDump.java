package ru.regenix.jphp.runtime.memory.output;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.PropertyEntity;

import java.io.Writer;
import java.util.Set;

public class VarDump extends Printer {

    private final static int PRINT_INDENT = 2;

    public VarDump(Writer writer) {
        super(writer);
    }

    @Override
    protected void printReference(ReferenceMemory reference, int level, Set<Integer> used) {
        if (reference.isShortcut())
            printer.write("&");

        super.printReference(reference, level, used);
    }

    @Override
    protected void printNull() {
        printer.write("NULL\n");
    }

    @Override
    protected void printFalse() {
        printer.write("bool(false)\n");
    }

    @Override
    protected void printTrue() {
        printer.write("bool(true)\n");
    }

    @Override
    protected void printLong(LongMemory value) {
        printer.write("int(");
        printer.write(value.toString());
        printer.write(")\n");
    }

    @Override
    protected void printDouble(DoubleMemory value) {
        printer.write("float(");
        printer.write(value.toString());
        printer.write(")\n");
    }

    @Override
    protected void printString(StringMemory value) {
        String string = value.toString();
        printer.write("string("); printer.write(string.length() + ""); printer.write(") \"");
        printer.write(string);
        printer.write("\"\n");
    }

    @Override
    protected void printArray(ArrayMemory value, int level, Set<Integer> used) {
        if (used.contains(value.getPointer())){
            printer.write("*RECURSION*\n");
        } else {
            printer.write("array(");
            printer.write(value.size() + "");
            printer.write(") {\n");

            level += PRINT_INDENT;

            used.add(value.getPointer());
            ForeachIterator iterator = value.foreachIterator(false, false);
            while (iterator.next()){
                printer.write(StringUtils.repeat(' ', level));
                Memory key = iterator.getCurrentMemoryKey();

                printer.write('[');
                if (key.isString()){
                    printer.write('"');
                    printer.write(key.toString());
                    printer.write('"');
                } else {
                    printer.write(key.toString());
                }

                printer.write("]=>\n");
                printer.write(StringUtils.repeat(' ', level));

                print(iterator.getCurrentValue(), level + 1, used);
                printer.write('\n');
            }

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            printer.write("}\n");

            used.remove(value.getPointer());
        }
    }

    @Override
    protected void printObject(ObjectMemory value, int level, Set<Integer> used) {
        PHPObject object = value.value;
        ClassEntity classEntity = object.__class__;

        if (used.contains(value.getPointer())){
            printer.write("*RECURSION*\n");
        } else {
            ArrayMemory arr = object.__dynamicProperties__;

            printer.write("object(");
            printer.write(classEntity.getName());
            printer.write(")#" + value.getPointer());
            printer.write(" (" + arr.size() + ") {\n");

            level += PRINT_INDENT;

            used.add(value.getPointer());
            ForeachIterator iterator = arr.foreachIterator(false, false);
            while (iterator.next()){
                printer.write(StringUtils.repeat(' ', level));
                Memory key = iterator.getCurrentMemoryKey();

                printer.write('[');
                if (key.isString()){
                    printer.write('"');
                    printer.write(key.toString());
                    printer.write('"');

                    PropertyEntity entity = classEntity.properties.get(key.toString());
                    if (entity != null){
                        switch (entity.getModifier()){
                            case PRIVATE:
                                printer.write(":\"" + entity.getClazz().getName() + "\":private");
                                break;
                            case PROTECTED:
                                printer.write(":protected");
                        }
                    }
                } else {
                    printer.write(key.toString());
                }

                printer.write("]=>\n");
                printer.write(StringUtils.repeat(' ', level));

                print(iterator.getCurrentValue(), level + 1, used);
                printer.write('\n');
            }

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            printer.write("}\n");

            used.remove(value.getPointer());
        }
    }
}
