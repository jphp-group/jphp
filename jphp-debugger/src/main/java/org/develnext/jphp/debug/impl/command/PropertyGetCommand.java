package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.develnext.jphp.debug.impl.command.support.ContextValueProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.Memory;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

public class PropertyGetCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "property_get";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        DebugTick tick = context.getRegisteredTick();

        Element response = createResponse(args, result);
        response.setAttribute("encoding", "base64");

        String varName = args.get("n");

        if (varName.startsWith("$")) {
            varName = varName.substring(1);
        }

        ArrayMemory locals = tick.getLocals();

        if (!locals.containsKey(varName)) {
            return;
        }

        ContextValueProvider provider = getContextValueProvider(context, result);

        if (provider.getMaxData() != 0 && args.containsKey("m")) {
            try {
                provider.setMaxData(Integer.parseInt(args.get("m")));
            } catch (NumberFormatException e) {
                return;
            }
        }

        Element property = provider.getProperty(null, locals.valueOfIndex(varName));
        response.setAttribute("size", property.getAttribute("size"));
        response.appendChild(property.getFirstChild());
    }

    protected ContextValueProvider getContextValueProvider(Debugger debugger, Document document) {
        return new ContextValueProvider(debugger, document);
    }
}
