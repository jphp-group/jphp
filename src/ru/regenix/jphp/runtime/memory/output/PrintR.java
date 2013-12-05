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

public class PrintR extends Printer {

    private final static int PRINT_INDENT = 4;

    public PrintR(Writer writer) {
        super(writer);
    }

    @Override
    protected void printNull() {
        // nop;
    }

    @Override
    protected void printFalse() {
        // nop;
    }

    @Override
    protected void printTrue() {
        printer.write("1");
    }

    @Override
    protected void printLong(LongMemory value) {
        printer.write(value.toString());
    }

    @Override
    protected void printDouble(DoubleMemory value) {
        printer.write(value.toString());
    }

    @Override
    protected void printString(StringMemory value) {
        printer.write(value.toString());
    }

    @Override
    protected void printArray(ArrayMemory value, int level, Set<Integer> used) {
        printer.write("Array\n");

        if (used.contains(value.getPointer())){
            printer.write(" *RECURSION*");
        } else {
            printer.write(StringUtils.repeat(' ', level));
            printer.write("(\n");
            level += PRINT_INDENT;

            used.add(value.getPointer());
            ForeachIterator iterator = value.foreachIterator(false, false);
            while (iterator.next()){
                printer.write(StringUtils.repeat(' ', level));
                Memory key = iterator.getCurrentMemoryKey();

                printer.write('[');
                printer.write(key.toString());

                printer.write("] => ");
                print(iterator.getCurrentValue(), level + 1, used);
                printer.write('\n');
            }

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            printer.write(")\n");

            used.remove(value.getPointer());
        }
    }

    @Override
    protected void printObject(ObjectMemory value, int level, Set<Integer> used) {
        PHPObject object = value.value;
        ClassEntity classEntity = object.__class__;

        printer.write(classEntity.getName());
        printer.write(" Object\n");

        if (used.contains(value.getPointer())){
            printer.write(" *RECURSION*");
        } else {
            printer.write(StringUtils.repeat(' ', level));
            printer.write("(\n");
            level += PRINT_INDENT;

            used.add(value.getPointer());

            ForeachIterator iterator = object.__dynamicProperties__.foreachIterator(false, false);
            while (iterator.next()){
                printer.write(StringUtils.repeat(' ', level));

                Object key = iterator.getCurrentKey();
                PropertyEntity propertyEntity = classEntity.properties.get(key);
                printer.write('[');

                printer.write(key.toString());

                if (propertyEntity != null){
                    switch (propertyEntity.getModifier()) {
                        case PRIVATE:
                            printer.write(":" + propertyEntity.getClazz().getName() + ":private");
                            break;
                        case PROTECTED:
                            printer.write(":protected");
                    }
                }

                printer.write("] => ");
                print(iterator.getCurrentValue(), level + 1, used);
                printer.write('\n');
            }

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            printer.write(")\n");

            used.remove(value.getPointer());
        }
    }
}
