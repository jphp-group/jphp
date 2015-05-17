package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.develnext.jphp.debug.impl.command.support.ContextValueProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.memory.ArrayMemory;

public class PropertyValueCommand extends PropertyGetCommand {
    @Override
    public String getName() {
        return "property_value";
    }

    @Override
    protected ContextValueProvider getContextValueProvider(Debugger debugger, Document document) {
        ContextValueProvider valueProvider = new ContextValueProvider(debugger, document);
        valueProvider.setMaxData(0);
        return valueProvider;
    }
}
