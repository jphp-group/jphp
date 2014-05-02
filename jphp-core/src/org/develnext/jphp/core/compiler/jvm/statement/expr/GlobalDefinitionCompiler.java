package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.GetVarExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.GlobalStmtToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;

public class GlobalDefinitionCompiler extends BaseStatementCompiler<GlobalStmtToken> {
    public GlobalDefinitionCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(GlobalStmtToken token) {
        for(ValueExprToken variable : token.getVariables()){
            if (variable instanceof VariableExprToken) {
                LocalVariable local = method.getLocalVariable(((VariableExprToken) variable).getName());
                assert local != null;

                expr.writePushEnv();
                expr.writePushConstString(local.name);
                expr.writeSysDynamicCall(Environment.class, "getOrCreateGlobal", Memory.class, String.class);
                expr.writeVarStore(local, false, false);
            } else if (variable instanceof GetVarExprToken) {
                BaseExprCompiler<GetVarExprToken> compiler =
                        (BaseExprCompiler<GetVarExprToken>) expr.getCompiler(GetVarExprToken.class);

                if (compiler == null)
                    throw new CriticalException("Cannot find a valid compiler for " + GetVarExprToken.class.getName());

                compiler.write((GetVarExprToken)variable, true);

                expr.writePushEnv();
                Memory name = expr.writeExpression(((GetVarExprToken)variable).getName(), true, true, true);
                if (name != null) {
                    expr.writePushConstString(name.toString());
                } else {
                    expr.writePopString();
                }
                expr.writeSysDynamicCall(Environment.class, "getOrCreateGlobal", Memory.class, String.class);

                expr.writeSysDynamicCall(Memory.class, "assign", Memory.class, Memory.class);
                expr.writePopAll(1);
            }
        }
    }
}
