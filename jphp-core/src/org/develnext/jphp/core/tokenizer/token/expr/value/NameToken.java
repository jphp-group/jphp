package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

public class NameToken extends ValueExprToken {

    private String name;

    public NameToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
        this.name = meta.getWord();
    }

    public String getName() {
        return name;
    }

    public static NameToken valueOf(String name){
        NameToken token = new NameToken(TokenMeta.empty());
        token.name = name;
        return token;
    }
}
