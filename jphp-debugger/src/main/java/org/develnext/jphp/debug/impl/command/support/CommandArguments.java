package org.develnext.jphp.debug.impl.command.support;

import java.util.LinkedHashMap;

public class CommandArguments extends LinkedHashMap<String, String> {
    public String getTransactionId() {
        return get("i");
    }
}
