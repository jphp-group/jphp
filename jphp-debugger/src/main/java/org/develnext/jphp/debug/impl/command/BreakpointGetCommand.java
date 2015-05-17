package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.breakpoint.Breakpoint;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BreakpointGetCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "breakpoint_get";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Breakpoint breakpoint = context.breakpointManager.get(args.get("d"));

        Element response = createResponse(args, result);

        if (breakpoint != null) {
            Element breakpointEl = result.createElement("breakpoint");
            breakpoint.output(breakpointEl);

            response.appendChild(breakpointEl);
        }
    }
}
