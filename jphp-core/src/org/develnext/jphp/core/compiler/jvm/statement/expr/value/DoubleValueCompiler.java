package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.DoubleExprToken;
import php.runtime.memory.DoubleMemory;

public class DoubleValueCompiler extends BaseExprCompiler<DoubleExprToken> {
    public DoubleValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(DoubleExprToken token, boolean returnValue) {
        expr.writePushMemory(new DoubleMemory(token.getValue()));
    }
}
