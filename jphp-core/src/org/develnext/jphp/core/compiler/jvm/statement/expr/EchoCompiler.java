package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.EchoStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import php.runtime.Memory;
import php.runtime.env.Environment;

public class EchoCompiler extends BaseStatementCompiler<EchoStmtToken> {
    public EchoCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(EchoStmtToken token) {
        for(ExprStmtToken argument : token.getArguments()){
            expr.writePushEnv();
            expr.writeExpression(argument, true, false);
            expr.writePopBoxing();
            expr.writeSysDynamicCall(Environment.class, "echo", void.class, Memory.class);
        }
    }
}
