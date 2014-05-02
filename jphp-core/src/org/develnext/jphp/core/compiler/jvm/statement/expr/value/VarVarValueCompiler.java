package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.GetVarExprToken;
import php.runtime.Memory;

public class VarVarValueCompiler extends BaseExprCompiler<GetVarExprToken> {
    public VarVarValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(GetVarExprToken getVar, boolean returnValue) {
        if (!methodStatement.isDynamicLocal())
            throw new ExpressionStmtCompiler.UnsupportedTokenException(getVar);

        Memory result = expr.writeExpression(getVar.getName(), true, true, false);
        if (result != null && result.isString()){
            String name = result.toString();
            LocalVariable variable = method.getLocalVariable(name);
            if (variable != null){
                if (returnValue)
                    expr.writeVarLoad(variable);
                return;
            }
        }

        expr.writePushLocal();
        expr.writeExpression(getVar.getName(), true, false);
        expr.writePopBoxing();

        expr.writeSysDynamicCall(Memory.class, "refOfIndex", Memory.class, Memory.class);
        if (!returnValue)
            expr.writePopAll(1);
    }
}
