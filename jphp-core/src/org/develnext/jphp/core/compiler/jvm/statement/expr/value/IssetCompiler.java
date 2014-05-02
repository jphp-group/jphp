package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.IssetExprToken;
import php.runtime.Memory;
import php.runtime.OperatorUtils;

public class IssetCompiler extends BaseExprCompiler<IssetExprToken> {
    public IssetCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(IssetExprToken token, boolean returnValue) {
        expr.writePushParameters(token.getParameters());
        expr.writeSysStaticCall(OperatorUtils.class, "isset", Boolean.TYPE, Memory[].class);

        if (!returnValue)
            expr.writePopAll(1);
    }
}
