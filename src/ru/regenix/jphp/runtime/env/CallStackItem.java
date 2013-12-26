package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.runtime.lang.Closure;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.output.PlainPrinter;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import java.io.StringWriter;

public class CallStackItem {
    public TraceInfo trace;
    public IObject object;
    public Memory[] args;

    public String function;
    public String clazz;
    public ClassEntity classEntity;

    public CallStackItem(TraceInfo trace) {
        this.trace = trace;
    }

    public CallStackItem(CallStackItem copy){
        this.trace = copy.trace;
        this.object = copy.object;
        this.args = copy.args;
        this.function = copy.function;
        this.clazz = copy.clazz;
    }

    public CallStackItem(TraceInfo trace, IObject object, Memory[] args, String function, String clazz) {
        this.trace = trace;
        this.object = object;
        this.args = args;
        this.function = function;
        this.clazz = clazz;
    }

    public void setParameters(TraceInfo trace, IObject object, Memory[] args, String function, String clazz) {
        this.trace = trace;
        this.object = object;
        this.args = args;
        this.function = function;
        this.clazz = clazz;
        this.classEntity = null;
    }

    public void clear(){
        this.object = null;
        this.args = null;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public ArrayMemory toArray(){
        return toArray(true, false);
    }

    public ArrayMemory toArray(boolean provideObject, boolean ignoreArgs){
        ArrayMemory el = new ArrayMemory();

        if (trace != null) {
            if (trace.getFile() != null)
                el.refOfIndex("file").assign(trace.getFileName());

            el.refOfIndex("line").assign(trace.getStartLine() + 1);
        }

        el.refOfIndex("function").assign(function);

        if (clazz != null) {
            el.refOfIndex("class").assign(clazz);
            el.refOfIndex("type").assign("::");
        }

        if (object != null){
            if (provideObject){
                el.refOfIndex("object").assign(new ObjectMemory(object));
            }
            el.refOfIndex("type").assign("->");
        }

        if (!ignoreArgs){
            el.refOfIndex("args").assign(new ArrayMemory(true, args));
        }

        if (trace != null)
            el.refOfIndex("position").assign(trace.getStartPosition() + 1);

        return el;
    }

    public String toString(boolean withArgs) {
        StringBuilder sb = new StringBuilder();
        if (clazz != null){
            sb.append(clazz);
            if (object == null)
                sb.append("::");
            else
                sb.append("->");

            sb.append(function);
        } else if (function != null){
            sb.append(function);
        } else if (object instanceof Closure){
            sb.append("{closure}");
        } else
            sb.append("<internal>");

        sb.append("(");
        if (withArgs) {
            StringWriter writer = new StringWriter();
            PlainPrinter printer = new PlainPrinter(writer);
            int i = 0;
            if (args != null)
            for(Memory arg : args){
                printer.print(arg);
                if (i != args.length - 1)
                    writer.append(", ");

                i++;
            }
            sb.append(writer.toString());
        }
        sb.append(")");
        if (trace != null) {
            sb.append(" called at [");
            sb.append(trace.getFileName());
            sb.append(":");
            sb.append(trace.getStartLine() + 1);
            sb.append("]");
        }

        return sb.toString();
    }
}
