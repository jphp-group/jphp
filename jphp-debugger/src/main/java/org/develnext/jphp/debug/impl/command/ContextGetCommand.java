package org.develnext.jphp.debug.impl.command;

import org.apache.commons.codec.binary.Base64;
import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
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

        switch (contextId) {
            case "0":
                DebugTick tick = context.getRegisteredTick();

                ForeachIterator iterator = tick.getLocals().foreachIterator(true, false);

                while (iterator.next()) {
                    Memory value = iterator.getValue().toValue();

                    if (value.isUndefined()) {
                        continue;
                    }

                    Element property = result.createElement("property");

                    property.setAttribute("name", "$" + iterator.getKey().toString());
                    property.setAttribute("fullname", "$" + iterator.getKey().toString());
                    property.setAttribute("type", value.type.toString());
                    property.setAttribute("constant", "0");
                    property.setAttribute("children", "0");
                    property.setAttribute("size", "1");
                    property.setAttribute("address", String.valueOf(value.getPointer()));
                    property.setAttribute("encoding", "base64");

                    property.appendChild(result.createCDATASection(Base64.encodeBase64String(value.toString().getBytes())));

                    response.appendChild(property);
                }

                break;
        }
    }
}
