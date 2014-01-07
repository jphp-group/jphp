package ru.regenix.jphp.runtime.lang.spl;

import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.BaseException;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

@Reflection.Name("ErrorException")
@Reflection.Signature(value =
{
    @Reflection.Arg(value = "severity", modifier = Modifier.PROTECTED, type = HintType.INT)
})
public class ErrorException extends BaseException {
    public ErrorException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature({
            @Reflection.Arg(value = "message", optional = @Reflection.Optional(value = "", type = HintType.STRING)),
            @Reflection.Arg(value = "code", optional = @Reflection.Optional(value = "0", type = HintType.INT)),
            @Reflection.Arg(value = "severity", optional = @Reflection.Optional(value = "1", type = HintType.INT)),
            @Reflection.Arg(value = "filename", optional = @Reflection.Optional(value = "NULL")),
            @Reflection.Arg(value = "line", optional = @Reflection.Optional(value = "NULL")),
            @Reflection.Arg(value = "previous", optional = @Reflection.Optional(value = "NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        super.__construct(env, args[0], args[1], args[5]);

        clazz.refOfProperty(props, "severity").assign(args[2].toInteger());

        // filename
        if (!args[3].isNull()){
            clazz.refOfProperty(props, "file").assign(args[3].toString());
        }

        // line
        if (!args[4].isNull())
            clazz.refOfProperty(props, "line").assign(args[4].toInteger());

        return Memory.NULL;
    }

    @Override
    public void setTraceInfo(Environment env, TraceInfo trace) {
        super.setTraceInfo(env, trace);
        Memory line = clazz.refOfProperty(props, "line");
        Memory file = clazz.refOfProperty(props, "file");
        if (!line.isNull() || !file.isNull()){
            this.trace = new TraceInfo(
                    file.isNull() ? trace.getFileName() : file.toString(),
                    line.isNull() ? trace.getStartLine() : line.toInteger() - 1, trace.getStartPosition()
            );
        }
    }

    @Reflection.Signature
    final public Memory getSeverity(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "severity").toValue();
    }
}
