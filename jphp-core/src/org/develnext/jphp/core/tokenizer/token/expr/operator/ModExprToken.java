package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class ModExprToken extends OperatorExprToken {
    public ModExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MOD);
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getCode() {
        return "mod";
    }

    @Override
    public String getCheckerCode() {
        return "modCheckResult";
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.mod(o2).modCheckResult(env, trace);
    }
}
