package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.DieExprToken;
import php.runtime.Memory;
import php.runtime.env.Environment;

public class DieCompiler extends BaseExprCompiler<DieExprToken> {
    public DieCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(DieExprToken token, boolean returnValue) {
        expr.writePushEnv();
        if (token.getValue() == null)
            expr.writePushConstNull();
        else {
            expr.writeExpression(token.getValue(), true, false, true);
            expr.writePopBoxing(false);
        }

        expr.writeSysDynamicCall(Environment.class, "die", void.class, Memory.class);
        if (returnValue)
            expr.writePushConstBoolean(true);
    }
}
