package php.runtime.env;

import php.runtime.Memory;
import php.runtime.lang.Closure;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.output.PlainPrinter;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ModuleEntity;

import java.io.StringWriter;

public class CallStackItem {
    public TraceInfo trace;
    public IObject object;
    public Memory[] args;

    public String function;
    public String clazz;
    public String staticClazz;

    public ClassEntity classEntity;
    public ClassEntity staticClassEntity;

    public int flags;

    public CallStackItem(TraceInfo trace) {
        this.trace = trace;
    }

    public CallStackItem(CallStackItem copy){
        this.trace = copy.trace;
        this.object = copy.object;
        this.args = copy.args;
        this.function = copy.function;
        this.clazz = copy.clazz;
        this.staticClazz = copy.staticClazz;
    }

    public CallStackItem(TraceInfo trace, IObject object, Memory[] args, String function, String clazz,
                         String staticClazz) {
        this.trace = trace;
        this.object = object;
        this.args = args;
        this.function = function;
        this.clazz = clazz;
        this.staticClazz = staticClazz;
    }

    public TraceInfo getTrace() {
        return trace;
    }

    public void setTrace(TraceInfo trace) {
        this.trace = trace;
    }

    public void setParameters(TraceInfo trace, IObject object, Memory[] args, String function, String clazz,
                              String staticClazz) {
        this.trace = trace;
        this.object = object;
        this.args = args;
        this.function = function;
        this.clazz = clazz;
        this.staticClazz = staticClazz;
        this.classEntity = null;
        this.staticClassEntity = null;
    }

    public void clear(){
        this.object = null;
        this.args = null;
        this.flags = 0;
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
            el.refOfIndex("args").assign(ArrayMemory.of(args));
        }

        if (trace != null)
            el.refOfIndex("position").assign(trace.getStartPosition() + 1);

        return el;
    }

    public static String toString(CallStackItem[] items, boolean withArgs) {
        int i = 0;

        StringBuilder sb = new StringBuilder();
        if (items != null){
            for (CallStackItem e : items){
                if (i != 0)
                    sb.append("\n");

                sb.append("#").append(i).append(" ").append(e.toString(withArgs));
                i++;
            }
            if (i != 0)
                sb.append("\n");

            sb.append("#").append(i).append(" {main}");
        }

        return sb.toString();
    }

    public String getWhere() {
        StringBuilder sb = new StringBuilder();
        if (object instanceof Closure)
            sb.append("{closure}");
        else if (clazz != null){
            sb.append(clazz);
            if (object == null)
                sb.append("::");
            else
                sb.append("->");

            sb.append(function);
        } else if (function != null){
            sb.append(function);
        } else
            sb.append("<internal>");

        return sb.toString();
    }

    public String toString(boolean withArgs) {
        StringBuilder sb = new StringBuilder();
        if (object instanceof Closure)
            sb.append("{closure}");
        else if (clazz != null){
            sb.append(clazz);
            if (object == null)
                sb.append("::");
            else
                sb.append("->");

            sb.append(function);
        } else if (function != null){
            sb.append(function);
        } else
            sb.append("<internal>");

        sb.append("(");
        if (withArgs) {
            StringWriter writer = new StringWriter();
            PlainPrinter printer = new PlainPrinter(null, writer);
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
        if (trace != null && trace != TraceInfo.UNKNOWN) {
            sb.append(" called at [");
            sb.append(trace.getFileName());
            sb.append(":");
            sb.append(trace.getStartLine() + 1);
            sb.append("]");
        }

        return sb.toString();
    }
}
