package ru.regenix.jphp.common;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.tokenizer.token.Token;

public class Directive {
    public final String value;
    public final Token token;

    public Directive(String value, Token token) {
        this.value = value;
        this.token = token;
    }

    public TraceInfo getTraceInfo(Context context){
        return token.toTraceInfo(context);
    }
}
