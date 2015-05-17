package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

abstract public class AbstractCommand {
    abstract public String getName();
    abstract public void run(Debugger context, CommandArguments args, Document result);

    public boolean afterExecutionContinueNeeded() {
        return false;
    }

    protected Element createResponse(CommandArguments args, Document document) {
        Element response = document.createElement("response");

        response.setAttribute("xmlns", "urn:debugger_protocol_v1");
        response.setAttribute("xmlns:xdebug", "http://xdebug.org/dbgp/xdebug");
        response.setAttribute("command", getName());
        response.setAttribute("transaction_id", args.getTransactionId());

        document.appendChild(response);

        return response;
    }
}
