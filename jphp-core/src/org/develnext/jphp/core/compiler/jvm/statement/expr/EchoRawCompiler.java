package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.EchoRawToken;
import php.runtime.env.Environment;

public class EchoRawCompiler extends BaseStatementCompiler<EchoRawToken> {
    public EchoRawCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(EchoRawToken token) {
        if (!token.getMeta().getWord().isEmpty()){
            expr.writePushEnv();
            expr.writePushString(token.getMeta().getWord());
            expr.writeSysDynamicCall(Environment.class, "echo", void.class, String.class);
        }
    }
}
