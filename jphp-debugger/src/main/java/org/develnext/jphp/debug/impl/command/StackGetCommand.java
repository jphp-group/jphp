package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.env.CallStackItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StackGetCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "stack_get";
    }

    @Override
    public boolean afterExecutionContinueNeeded() {
        return false;
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document result) {
        DebugTick tick = context.getRegisteredTick();

        Element response = createResponse(args, result);

        CallStackItem[] stackSnapshot = tick.getEnvironment().getCallStackSnapshot();

        List<CallStackItem> list = new ArrayList<>();
        Collections.addAll(list, stackSnapshot);

        if (list.isEmpty()) {
            list.add(new CallStackItem(tick.getTrace()));
        }

        Collections.reverse(list);
        stackSnapshot = list.toArray(new CallStackItem[list.size()]);

        int depth = args.containsKey("d") ? Integer.parseInt(args.get("d")) : -1;

        if (depth > -1) {
            stackSnapshot = Arrays.copyOf(stackSnapshot, depth + 1);
        }

        int i = 0;

        for (CallStackItem stackItem : stackSnapshot) {
            Element stack = result.createElement("stack");

            stack.setAttribute("level", String.valueOf(i));
            stack.setAttribute("type", "file");

            stack.setAttribute("filename", context.getFileName(stackItem.trace.getFileName()));
            stack.setAttribute("lineno", String.valueOf(stackItem.trace.getStartLine() + 1));

            stack.setAttribute("where", "{main}");

            response.appendChild(stack);

            i++;
        }
    }
}
