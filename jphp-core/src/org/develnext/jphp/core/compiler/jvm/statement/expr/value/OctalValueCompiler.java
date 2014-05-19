package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.OctalExprValue;
import php.runtime.memory.LongMemory;

public class OctalValueCompiler extends BaseExprCompiler<OctalExprValue> {
    public OctalValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(OctalExprValue token, boolean returnValue) {
        expr.writePushMemory(new LongMemory(token.getValue()));
    }
}
