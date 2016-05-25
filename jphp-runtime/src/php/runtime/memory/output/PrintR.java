package php.runtime.memory.output;

import php.runtime.Memory;
import php.runtime.common.Modifier;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.Closure;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.PropertyEntity;

import java.io.Writer;
import java.util.Set;

public class PrintR extends Printer {

    protected int PRINT_INDENT = 4;

    public PrintR(Environment env, Writer writer) {
        super(env, writer);
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
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
            writeOpen();

            level += 1;

            used.add(value.getPointer());
            ForeachIterator iterator = value.foreachIterator(false, false);
            int i = 0;
            int size = value.size();
            while (iterator.next()){
                Memory el = iterator.getValue();
                if (el == Memory.UNDEFINED) continue;

                printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
                Memory key = iterator.getMemoryKey();

                printer.write('[');
                printer.write(key.toString());

                printer.write("] => ");
                print(el, level + 1, used);
                writeSeparator(i == size - 1);
                i++;
            }

            level -= 1;
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
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
        ClassEntity classEntity = closure.getReflection();

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
        ClassEntity classEntity = value.getReflection();

        writeObjectHeader(classEntity.getName());

        if (used.contains(value.getPointer())){
            printer.write(" *RECURSION*");
        } else {
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
            writeOpen();
            level += 1;

            used.add(value.getPointer());

            ArrayMemory props;
            if (env != null && classEntity.methodMagicDebugInfo != null) {
                try {
                    Memory tmp = env.invokeMethod(value.value, classEntity.methodMagicDebugInfo.getName());

                    if (tmp.isArray()) {
                        props = tmp.toValue(ArrayMemory.class);
                    } else {
                        props = new ArrayMemory();
                    }
                } catch (RuntimeException e) {
                    throw e;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            } else {
                props = value.getProperties();
            }

            if (classEntity.methodMagicDebugInfo != null) {
                for (PropertyEntity entity : classEntity.getProperties()) {
                    if (entity.getGetter() != null && !entity.isHiddenInDebugInfo()) {
                        printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));

                        printer.write("[");
                        printer.write(entity.getName());
                        printer.write(":getter] => ");

                        try {
                            print(entity.getValue(env, TraceInfo.UNKNOWN, value.value));
                        } catch (RuntimeException e) {
                            throw e;
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }

                        writeSeparator(false);
                    }
                }
            }

            if (props != null) {

                ForeachIterator iterator = props.foreachIterator(false, false);
                int i = 0;
                int size = classEntity.properties.size();

                while (iterator.next()){
                    printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));

                    Object key = iterator.getKey();
                    printer.write('[');

                    String realKey = key.toString();
                    int pos;
                    Modifier modifier = Modifier.PUBLIC;
                    String className = "";

                    if ((pos = realKey.lastIndexOf("\0")) > -1){
                        if (realKey.startsWith("\0*\0")) {
                            modifier = Modifier.PROTECTED;
                        } else {
                            modifier = Modifier.PRIVATE;
                            className = realKey.substring(1, pos);
                        }

                        realKey = realKey.substring(pos + 1);
                    }

                    printer.write(realKey);

                    switch (modifier) {
                        case PRIVATE:
                            printer.write(":" + className + ":private");
                            break;
                        case PROTECTED:
                            printer.write(":protected");
                    }

                    printer.write("] => ");
                    print(iterator.getValue(), level + 1, used);
                    writeSeparator(i == size - 1);
                    i++;
                }
            }

            level -= 1;
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
            writeClose();

            used.remove(value.getPointer());
        }
    }
}
