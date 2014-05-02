package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.SelfExprToken;

public class SelfValueCompiler extends BaseExprCompiler<SelfExprToken> {
    public SelfValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(SelfExprToken token, boolean returnValue) {
        expr.writePushSelf(false);
    }
}
