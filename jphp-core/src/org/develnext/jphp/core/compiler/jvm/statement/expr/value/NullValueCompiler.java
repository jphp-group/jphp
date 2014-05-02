package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.NullExprToken;

public class NullValueCompiler extends BaseExprCompiler<NullExprToken> {
    public NullValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(NullExprToken token, boolean returnValue) {
        expr.writePushNull();
    }
}
