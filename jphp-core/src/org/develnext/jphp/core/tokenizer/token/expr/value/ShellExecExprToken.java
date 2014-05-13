package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.Token;

import java.util.List;

public class ShellExecExprToken extends StringBuilderExprToken implements CallableExprToken {
    public ShellExecExprToken(TokenMeta meta, List<Token> expression) {
        super(meta, expression);
    }
}
