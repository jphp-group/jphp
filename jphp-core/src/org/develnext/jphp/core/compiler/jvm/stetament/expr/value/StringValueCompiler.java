package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;
import php.runtime.memory.StringMemory;

public class StringValueCompiler extends BaseExprCompiler<StringExprToken> {
    public StringValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(StringExprToken token, boolean returnValue) {
        expr.writePushMemory(new StringMemory(token.getValue()));
    }
}
