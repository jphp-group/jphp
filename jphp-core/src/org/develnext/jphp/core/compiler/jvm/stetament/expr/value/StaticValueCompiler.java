package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.StaticExprToken;

public class StaticValueCompiler extends BaseExprCompiler<StaticExprToken> {
    public StaticValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(StaticExprToken token, boolean returnValue) {
        expr.writePushStatic();
    }
}
