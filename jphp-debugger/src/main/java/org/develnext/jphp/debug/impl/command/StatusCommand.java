package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StatusCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "status";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Element response = createResponse(args, result);

        response.setAttribute("status", context.getStatus().name().toLowerCase());
        response.setAttribute("reason", "ok");
    }
}
