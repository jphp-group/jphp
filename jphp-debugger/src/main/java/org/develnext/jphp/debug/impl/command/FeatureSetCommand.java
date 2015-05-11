package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FeatureSetCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "feature_set";
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        context.ideFeatures.set(args.get("n"), args.get("v"));

        Element response = createResponse(args, result);
        response.setAttribute("success", "1");
    }
}
