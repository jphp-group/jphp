package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RunCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "run";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        DebugTick tick = context.runTicks();

        Element response = createResponse(args, result);

        response.setAttribute("reason", "ok");

        if (tick.getBreakpoint() != null) {
            response.setAttribute("status", "break");
        } else {
            response.setAttribute("status", "stopping");
        }

        if (tick.getTrace() != null) {
            Element message = result.createElement("xdebug:message");
            message.setAttribute("filename", context.getFileName(tick.getTrace().getFileName()));
            message.setAttribute("lineno", String.valueOf(tick.getTrace().getStartLine() + 1));

            response.appendChild(message);
        }
    }
}
