package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
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
