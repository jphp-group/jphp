package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.ThrowStmtToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class ThrowCompiler extends BaseStatementCompiler<ThrowStmtToken> {
    public ThrowCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ThrowStmtToken token) {
        expr.writePushEnv();
        expr.writePushTraceInfo(token.getException());
        expr.writeExpression(token.getException(), true, false, true);
        expr.writePopBoxing();
        expr.writeSysDynamicCall(Environment.class, "__throwException", void.class, TraceInfo.class, Memory.class);
    }
}
