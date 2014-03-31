package org.develnext.jphp.core.compiler.jvm.stetament.expr;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.ExprToken;

abstract public class BaseExprCompiler<T extends ExprToken> extends BaseStatementCompiler<T> {
    public BaseExprCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    abstract public void write(T token, boolean returnValue);

    @Override
    public void write(T token) {
        write(token, false);
    }
}
