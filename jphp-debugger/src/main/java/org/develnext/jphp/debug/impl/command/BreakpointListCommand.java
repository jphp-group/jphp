package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.breakpoint.Breakpoint;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BreakpointListCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "breakpoint_list";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Element response = createResponse(args, result);

        for (Breakpoint breakpoint : context.breakpointManager.all()) {
            Element breakpointEl = result.createElement("breakpoint");
            breakpoint.output(breakpointEl);

            response.appendChild(breakpointEl);
        }
    }
}
