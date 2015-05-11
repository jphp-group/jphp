package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.breakpoint.Breakpoint;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;

public class BreakpointRemoveCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "breakpoint_remove";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Breakpoint breakpoint = context.breakpointManager.remove(args.get("d"));

        createResponse(args, result);
    }
}
