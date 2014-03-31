package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.BinaryExprValue;
import php.runtime.memory.LongMemory;

public class BinaryValueCompiler extends BaseExprCompiler<BinaryExprValue> {
    public BinaryValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(BinaryExprValue token, boolean returnValue) {
        expr.writePushMemory(new LongMemory(token.getValue()));
    }
}
