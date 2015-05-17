package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UnimplementedCommand extends AbstractCommand {
    protected final String name;

    public UnimplementedCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        Element response = createResponse(args, result);

        Element error = result.createElement("error");
        error.setAttribute("code", "4"); // unimplemented

        response.appendChild(error);
    }
}
