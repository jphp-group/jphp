package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.develnext.jphp.debug.impl.command.support.ContextValueProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.Memory;
import php.runtime.lang.ForeachIterator;

public class ContextGetCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "context_get";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Element response = createResponse(args, result);

        String contextId = args.get("c");

        response.setAttribute("context", contextId);

        ContextValueProvider contextValueProvider = new ContextValueProvider(context, result);

        switch (contextId) {
            case "0":
                DebugTick tick = context.getRegisteredTick();

                ForeachIterator iterator = tick.getLocals().foreachIterator(true, false);

                while (iterator.next()) {
                    Memory value = iterator.getValue().toValue();

                    if (value.isUndefined()) {
                        continue;
                    }

                    response.appendChild(contextValueProvider.getProperty(iterator.getKey().toString(), value));
                }

                break;
        }
    }
}
