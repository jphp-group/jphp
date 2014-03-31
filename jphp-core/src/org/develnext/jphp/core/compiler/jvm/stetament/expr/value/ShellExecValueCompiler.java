package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.ShellExecExprToken;
import php.runtime.env.Environment;

public class ShellExecValueCompiler extends BaseExprCompiler<ShellExecExprToken> {
    public ShellExecValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ShellExecExprToken token, boolean returnValue) {
        expr.writePushEnv();
        expr.writePushConstString(token.getWord());
        expr.writeSysDynamicCall(Environment.class, "__shellExecute", String.class, String.class);
        if (!returnValue)
            expr.writePopAll(1);
    }
}
