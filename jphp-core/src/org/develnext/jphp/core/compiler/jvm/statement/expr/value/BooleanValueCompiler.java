package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.BooleanExprToken;
import php.runtime.Memory;

public class BooleanValueCompiler extends BaseExprCompiler<BooleanExprToken> {
    public BooleanValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(BooleanExprToken token, boolean returnValue) {
        expr.writePushMemory(token.getValue() ? Memory.TRUE : Memory.FALSE);
    }
}
