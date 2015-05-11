package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StepIntoCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "step_into";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        DebugTick tick = context.waitTick(Debugger.Step.INTO);

        Element response = createResponse(args, result);
        response.setAttribute("reason", "ok");
        response.setAttribute("status", "break");

        if (tick.getTrace() != null) {
            Element message = result.createElement("xdebug:message");
            message.setAttribute("filename", context.getFileName(tick.getTrace().getFileName()));
            message.setAttribute("lineno", String.valueOf(tick.getTrace().getStartLine() + 1));

            response.appendChild(message);
        }
    }
}
