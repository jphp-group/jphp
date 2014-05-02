package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.OpenEchoTagToken;
import php.runtime.Memory;
import php.runtime.env.Environment;

public class OpenEchoTagCompiler extends BaseStatementCompiler<OpenEchoTagToken> {
    public OpenEchoTagCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(OpenEchoTagToken token) {
        expr.writePushEnv();
        expr.writeExpression(token.getValue(), true, false);
        expr.writePopBoxing();
        expr.writeSysDynamicCall(Environment.class, "echo", void.class, Memory.class);
    }
}
