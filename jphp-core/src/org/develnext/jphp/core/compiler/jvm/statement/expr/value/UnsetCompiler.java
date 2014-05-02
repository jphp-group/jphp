package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.GetVarExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.UnsetExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import php.runtime.Memory;
import php.runtime.env.Environment;

public class UnsetCompiler extends BaseExprCompiler<UnsetExprToken> {
    public UnsetCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(UnsetExprToken token, boolean returnValue) {
        method.getEntity().setImmutable(false);
        for(ExprStmtToken param : token.getParameters()){
            if (param.isSingle() && param.getSingle() instanceof VariableExprToken){
                VariableExprToken variable = (VariableExprToken)param.getSingle();
                expr.checkAssignableVar(variable);
                LocalVariable local = method.getLocalVariable(variable.getName());

                expr.writeVarLoad(local);
                expr.writePushEnv();
                expr.writeSysDynamicCall(Memory.class, "manualUnset", void.class, Environment.class);
                if (!local.isReference()) {
                    expr.writePushNull();
                    expr.writeVarAssign(local, null, false, false);
                }

                local.setValue(null);
            } else if (param.isSingle() && param.getSingle() instanceof GetVarExprToken){
                expr.writeValue((ValueExprToken) param.getSingle(), true);
                expr.writePushEnv();
                expr.writeSysDynamicCall(Memory.class, "manualUnset", void.class, Environment.class);
            } else {
                expr.writeExpression(param, false, false, true);
            }
        }

        if (returnValue)
            expr.writePushNull();
    }
}
