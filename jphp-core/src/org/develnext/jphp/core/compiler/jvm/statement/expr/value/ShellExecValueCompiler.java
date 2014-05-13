package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.ShellExecExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringBuilderExprToken;
import php.runtime.env.Environment;

public class ShellExecValueCompiler extends BaseExprCompiler<ShellExecExprToken> {
    public ShellExecValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ShellExecExprToken token, boolean returnValue) {
        expr.writePushEnv();
        expr.getCompiler(StringBuilderExprToken.class).write(token);
        expr.writePopString();

        expr.writeSysDynamicCall(Environment.class, "__shellExecute", String.class, String.class);
        if (!returnValue)
            expr.writePopAll(1);
    }
}
