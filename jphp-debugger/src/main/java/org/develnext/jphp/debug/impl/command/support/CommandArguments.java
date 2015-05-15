package org.develnext.jphp.debug.impl.command.support;

import org.apache.commons.codec.binary.Base64;

import java.util.LinkedHashMap;

public class CommandArguments extends LinkedHashMap<String, String> {
    public String getTransactionId() {
        return get("i");
    }

    public String getContent() {
        if (containsKey("-")) {
            return new String(Base64.decodeBase64(get("-")));
        }

        return null;
    }
}
