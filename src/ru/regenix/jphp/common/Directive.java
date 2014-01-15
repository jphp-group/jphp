package ru.regenix.jphp.common;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
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
