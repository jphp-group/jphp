package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.IntegerExprToken;
import php.runtime.memory.LongMemory;

public class IntValueCompiler extends BaseExprCompiler<IntegerExprToken> {
    public IntValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(IntegerExprToken token, boolean returnValue) {
        expr.writePushMemory(new LongMemory(token.getValue()));
    }
}
