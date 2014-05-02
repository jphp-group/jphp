package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
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
        expr.writePushConstString(compiler.getModule().getInternalName());
        expr.writePushConstInt(token.getId());

        expr.writeSysDynamicCall(
                Environment.class,
                "__defineFunction",
                void.class,
                TraceInfo.class,
                String.class,
                Integer.TYPE
        );
    }
}
