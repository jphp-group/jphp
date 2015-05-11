package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;

public class StepOutCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "step_out";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        DebugTick tick = context.waitTick(Debugger.Step.OUT);
    }
}
