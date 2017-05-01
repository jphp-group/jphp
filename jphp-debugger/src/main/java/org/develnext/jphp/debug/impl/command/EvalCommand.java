package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.develnext.jphp.debug.impl.command.support.ContextValueProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.Memory;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.core.LangFunctions;

public class EvalCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "eval";
    }

    public static Memory getValue(Debugger context, String value) {
        DebugTick tick = context.getRegisteredTick();
        Environment environment = new Environment(new CompileScope(tick.getEnvironment().getScope()));

        try {
            return LangFunctions.eval(environment, tick.getTrace(), tick.getLocals(), "return " + value + ";");
        } catch (Throwable throwable) {
            return null;
        }
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        String content = args.getContent();

        Element response = createResponse(args, result);
        response.setAttribute("success", "1");

        ContextValueProvider valueProvider = new ContextValueProvider(context, result);
        valueProvider.setMaxData(0);

        Memory value = getValue(context, content);

        if (value == null) {
            response.setAttribute("success", "0");
        } else {
            response.appendChild(valueProvider.getProperty(null, value));
        }
    }
}
