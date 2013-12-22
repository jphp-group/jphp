package ru.regenix.jphp.runtime.memory.output;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.runtime.lang.Closure;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.PropertyEntity;

import java.io.Writer;
import java.util.Set;

public class PrintR extends Printer {

    protected int PRINT_INDENT = 4;

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

    protected void writeArrayHeader(){
        printer.write("Array\n");
    }

    protected void writeClose(){
        printer.write(")\n");
    }

    protected void writeSeparator(boolean isLast){
        printer.write('\n');
    }

    protected void writeOpen(){
        printer.write("(\n");
    }

    @Override
    protected void printArray(ArrayMemory value, int level, Set<Integer> used) {
        writeArrayHeader();

        if (used.contains(value.getPointer())){
            printer.write(" *RECURSION*");
        } else {
            printer.write(StringUtils.repeat(' ', level));
            writeOpen();

            level += PRINT_INDENT;

            used.add(value.getPointer());
            ForeachIterator iterator = value.foreachIterator(false, false);
            int i = 0;
            int size = value.size();
            while (iterator.next()){
                printer.write(StringUtils.repeat(' ', level));
                Memory key = iterator.getMemoryKey();

                printer.write('[');
                printer.write(key.toString());

                printer.write("] => ");
                print(iterator.getValue(), level + 1, used);
                writeSeparator(i == size - 1);
                i++;
            }

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            writeClose();

            used.remove(value.getPointer());
        }
    }

    protected void writeObjectHeader(String name){
        printer.write(name);
        printer.write(" Object\n");
    }

    @Override
    protected void printClosure(Closure closure, int level, Set<Integer> used) {
        ClassEntity classEntity = closure.__class__;

        writeObjectHeader(Closure.class.getSimpleName());
        if (used.contains(closure.getPointer())){
            printer.write(" *RECURSION*");
        } else {
            printer.write(StringUtils.repeat(' ', level));
            writeOpen();
            level += PRINT_INDENT;

            used.add(closure.getPointer());

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            writeClose();

            used.remove(closure.getPointer());
        }
    }

    @Override
    protected void printObject(ObjectMemory value, int level, Set<Integer> used) {
        PHPObject object = value.value;
        ClassEntity classEntity = object.__class__;

        writeObjectHeader(classEntity.getName());

        if (used.contains(value.getPointer())){
            printer.write(" *RECURSION*");
        } else {
            printer.write(StringUtils.repeat(' ', level));
            writeOpen();
            level += PRINT_INDENT;

            used.add(value.getPointer());

            if (object.__dynamicProperties__ != null){
                ForeachIterator iterator = object.__dynamicProperties__.foreachIterator(false, false);
                int i = 0;
                int size = classEntity.properties.size();
                while (iterator.next()){
                    printer.write(StringUtils.repeat(' ', level));

                    Object key = iterator.getKey();
                    PropertyEntity propertyEntity = classEntity.properties.get(key.toString());
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
                    print(iterator.getValue(), level + 1, used);
                    writeSeparator(i == size - 1);
                    i++;
                }
            }

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            writeClose();

            used.remove(value.getPointer());
        }
    }
}
