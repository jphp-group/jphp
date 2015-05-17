package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;

public class BodyCompiler extends BaseStatementCompiler<BodyStmtToken> {
    public BodyCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(BodyStmtToken token) {
        if (token!= null){
            for(ExprStmtToken line : token.getInstructions()){
                expr.writeTickTrigger(line);
                expr.writeExpression(line, false, false);
            }
        }
    }
}
