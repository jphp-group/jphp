package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ContextNamesCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "context_names";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Element response = createResponse(args, result);

        Element localContext = result.createElement("context");
        localContext.setAttribute("name", "Local");
        localContext.setAttribute("id", "0");
        response.appendChild(localContext);

        Element globalContext = result.createElement("context");
        globalContext.setAttribute("name", "Global");
        globalContext.setAttribute("id", "1");
        response.appendChild(localContext);

        Element classContext = result.createElement("context");
        classContext.setAttribute("name", "Class");
        classContext.setAttribute("id", "2");
        response.appendChild(localContext);
    }
}
