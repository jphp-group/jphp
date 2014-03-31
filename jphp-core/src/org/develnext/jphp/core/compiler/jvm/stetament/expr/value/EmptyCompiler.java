package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.EmptyExprToken;
import php.runtime.Memory;
import php.runtime.OperatorUtils;

public class EmptyCompiler extends BaseExprCompiler<EmptyExprToken> {
    public EmptyCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(EmptyExprToken token, boolean returnValue) {
        expr.writeExpression(token.getValue(), true, false);
        expr.writeSysStaticCall(OperatorUtils.class, "empty", Boolean.TYPE, Memory.class);

        if (!returnValue)
            expr.writePopAll(1);
    }
}
