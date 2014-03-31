package org.develnext.jphp.core.compiler.jvm.stetament.expr;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.GlobalStmtToken;
import php.runtime.Memory;
import php.runtime.env.Environment;

public class GlobalDefinitionCompiler extends BaseStatementCompiler<GlobalStmtToken> {
    public GlobalDefinitionCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(GlobalStmtToken token) {
        for(VariableExprToken variable : token.getVariables()){
            LocalVariable local = method.getLocalVariable(variable.getName());
            assert local != null;

            expr.writePushEnv();
            expr.writePushConstString(local.name);
            expr.writeSysDynamicCall(Environment.class, "getOrCreateGlobal", Memory.class, String.class);
            expr.writeVarStore(local, false, false);
        }
    }
}
