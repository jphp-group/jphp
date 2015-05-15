package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.env.CallStack;
import php.runtime.env.CallStackItem;
import php.runtime.env.TraceInfo;

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

        CallStack callStack = tick.getCallStack();

        List<CallStackItem> list = new ArrayList<>();
        Collections.addAll(list, callStack.getSnapshot());

        CallStackItem last = list.get(list.size() - 1);

        if (list.size() > 1) {
            CallStackItem prevLast = list.get(list.size() - 2);
            TraceInfo trace = prevLast.getTrace();
            prevLast.setTrace(new TraceInfo(
                    trace.getContext(),
                    last.getTrace().getStartLine(),
                    trace.getEndLine(),
                    last.getTrace().getStartPosition(),
                    trace.getEndLine()
            ));
        }

        last.setTrace(tick.getTrace());

        Collections.reverse(list);

        int depth = args.containsKey("d") ? Integer.parseInt(args.get("d")) : -1;

        if (depth > -1) {
            list = list.subList(0, depth + 1);
        }

        int i = 0;

        for (CallStackItem stackItem : list) {
            Element stack = result.createElement("stack");

            stack.setAttribute("level", String.valueOf(i));
            stack.setAttribute("type", "file");

            stack.setAttribute("filename", context.getFileName(stackItem.trace.getFileName()));
            stack.setAttribute("lineno", String.valueOf(stackItem.trace.getStartLine() + 1));

            stack.setAttribute("where", stackItem.getWhere());

            response.appendChild(stack);

            i++;
        }
    }
}
