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

public class VarDump extends Printer {

    private final static int PRINT_INDENT = 2;

    public VarDump(Environment env, Writer writer) {
        super(env, writer);
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
    protected void printDouble(DoubleMemory avalue) {
        printer.write("float(");

        double value = avalue.toDouble();
        if (value == 0.0 && Double.doubleToRawLongBits(value) != Double.doubleToRawLongBits(0.0))
            printer.write("-");

        printer.write(avalue.toString());
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

            level += 1;

            used.add(value.getPointer());
            ForeachIterator iterator = value.foreachIterator(false, false);
            while (iterator.next()){
                Memory el = iterator.getValue();
                if (el == Memory.UNDEFINED) continue;

                printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
                Memory key = iterator.getMemoryKey();

                printer.write('[');
                if (key.isString()){
                    printer.write('"');
                    printer.write(key.toString());
                    printer.write('"');
                } else {
                    printer.write(key.toString());
                }

                printer.write("]=>\n");
                printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));

                print(el, level, used);
                //printer.write('\n');
            }

            level -= 1;
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
            printer.write("}\n");

            used.remove(value.getPointer());
        }
    }

    @Override
    protected void printClosure(Closure closure, int level, Set<Integer> used) {
        ClassEntity classEntity = closure.getReflection();

        if (used.contains(closure.getPointer())){
            printer.write("*RECURSION*\n");
        } else {
            printer.write("object(");
            printer.write(Closure.class.getSimpleName());
            printer.write(")#" + closure.getPointer());
            printer.write(" (" + closure.getUses().length + ") {\n");

            level += PRINT_INDENT;

            used.add(closure.getPointer());

            level -= PRINT_INDENT;
            printer.write(StringUtils.repeat(' ', level));
            printer.write("}\n");

            used.remove(closure.getPointer());
        }
    }

    @Override
    protected void printObject(ObjectMemory value, int level, Set<Integer> used) {
        ClassEntity classEntity = value.getReflection();

        if (used.contains(value.getPointer())){
            printer.write("*RECURSION*\n");
        } else {
            ArrayMemory arr;
            if (classEntity.methodMagicDebugInfo != null) {
                try {
                    Memory tmp = env.invokeMethod(value.value, classEntity.methodMagicDebugInfo.getName());
                    if (tmp.isArray()) {
                        arr = tmp.toValue(ArrayMemory.class);
                    } else {
                        arr = new ArrayMemory();
                    }
                } catch (RuntimeException e) {
                    throw e;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            } else
                arr = value.getProperties();

            printer.write("object(");
            printer.write(classEntity.getName());
            printer.write(")#" + value.getPointer());
            printer.write(" (" + arr.size() + ") {\n");

            level += 1;

            used.add(value.getPointer());

            if (classEntity.methodMagicDebugInfo == null) {
                for (PropertyEntity entity : classEntity.getProperties()) {
                    if (entity.getGetter() != null && !entity.isHiddenInDebugInfo()) {
                        printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));

                        printer.write("[\"");
                        printer.write(entity.getName());
                        printer.write('"');
                        printer.write(":getter]=>\n");

                        printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));

                        try {
                            print(entity.getValue(env, TraceInfo.UNKNOWN, value.value), level, used);
                        } catch (RuntimeException e) {
                            throw e;
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }
                }
            }

            ForeachIterator iterator = arr.foreachIterator(false, false);

            while (iterator.next()){
                printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
                Memory key = iterator.getMemoryKey();

                printer.write('[');
                if (key.isString()){
                    String realKey = key.toString();
                    int pos;
                    Modifier modifier = Modifier.PUBLIC;
                    String className = "";
                    if ((pos = realKey.lastIndexOf("\0")) > -1){
                        if (realKey.startsWith("\0*\0")){
                            modifier = Modifier.PROTECTED;
                        } else {
                            modifier = Modifier.PRIVATE;
                            className = realKey.substring(1, pos);
                        }
                        realKey = realKey.substring(pos + 1);
                    }

                    printer.write('"');
                    printer.write(realKey);
                    printer.write('"');

                    switch (modifier) {
                        case PRIVATE:
                            printer.write(":\"" + className + "\":private");
                            break;
                        case PROTECTED:
                            printer.write(":protected");
                    }

                } else {
                    printer.write(key.toString());
                }

                printer.write("]=>\n");
                printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));

                print(iterator.getValue(), level, used);
                //printer.write('\n');
            }

            level -= 1;
            printer.write(StringUtils.repeat(' ', level * PRINT_INDENT));
            printer.write("}\n");

            used.remove(value.getPointer());
        }
    }
}
