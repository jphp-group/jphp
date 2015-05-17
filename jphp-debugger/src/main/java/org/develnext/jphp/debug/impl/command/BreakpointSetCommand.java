package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.breakpoint.Breakpoint;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BreakpointSetCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "breakpoint_set";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Breakpoint breakpoint = Breakpoint.build(args);

        context.breakpointManager.set(breakpoint);

        Element response = createResponse(args, result);
        response.setAttribute("id", String.valueOf(breakpoint.getId()));
        response.setAttribute("state", breakpoint.getState());
    }
}
