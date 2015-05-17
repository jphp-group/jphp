package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.Memory;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

public class PropertySetCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "property_set";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        DebugTick tick = context.getRegisteredTick();

        Element response = createResponse(args, result);
        response.setAttribute("success", "1");

        String varName = args.get("n");

        if (varName.startsWith("$")) {
            varName = varName.substring(1);
        }

        Memory data = EvalCommand.getValue(context, args.getContent());

        ArrayMemory locals = tick.getLocals();

        if (!locals.containsKey(varName) || data == null) {
            response.setAttribute("success", "0");
            return;
        }

        Memory memory = locals.refOfIndex(varName);

        Memory.Type type = null;

        if (args.containsKey("t")) {
            switch (args.get("t")) {
                case "boolean":
                case "bool":
                    type = Memory.Type.BOOL;
                    break;
                case "int":
                case "integer":
                    type = Memory.Type.INT;
                    break;
                case "float":
                case "double":
                    type = Memory.Type.DOUBLE;
                    break;
                case "string":
                    type = Memory.Type.STRING;
                    break;
                case "array":
                    type = Memory.Type.ARRAY;
                    break;
                case "null":
                    type = Memory.Type.NULL;
                    break;
            }
        }

        if (type != null) {
            switch (type) {
                case BOOL:
                    data = data.toBoolean() ? Memory.TRUE : Memory.FALSE;
                    break;
                case INT:
                    data = LongMemory.valueOf(data.toLong());
                    break;
                case DOUBLE:
                    data = DoubleMemory.valueOf(data.toDouble());
                    break;
                case NULL:
                    data = Memory.NULL;
                    break;
                case ARRAY:
                    data = new ArrayMemory();
                    break;
            }
        }

        memory.assign(data);
    }
}
