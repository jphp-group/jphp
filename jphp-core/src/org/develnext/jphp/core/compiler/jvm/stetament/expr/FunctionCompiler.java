package org.develnext.jphp.core.compiler.jvm.stetament.expr;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class FunctionCompiler extends BaseStatementCompiler<FunctionStmtToken> {
    public FunctionCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(FunctionStmtToken token) {
        expr.writePushEnv();
        expr.writePushTraceInfo(token);
        expr.writePushConstInt(compiler.getModule().getId());
        expr.writePushConstInt(token.getId());

        expr.writeSysDynamicCall(
                Environment.class, "__defineFunction", void.class, TraceInfo.class, Integer.TYPE, Integer.TYPE
        );
    }
}
