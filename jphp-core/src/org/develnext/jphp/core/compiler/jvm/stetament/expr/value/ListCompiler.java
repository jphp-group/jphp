package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DynamicAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.ListExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.ObjectInvokeHelper;

public class ListCompiler extends BaseExprCompiler<ListExprToken> {
    public ListCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ListExprToken list, boolean returnValue) {
        expr.writeExpression(list.getValue(), true, false);
        int i, length = list.getVariables().size();
        for(i = length - 1; i >= 0; i--){ // desc order as in PHP
            ListExprToken.Variable v = list.getVariables().get(i);
            expr.writePushDup();

            if (v.indexes != null){
                for(int index : v.indexes){
                    expr.writePushConstLong(index);
                    expr.writeSysDynamicCall(Memory.class, "valueOfIndex", Memory.class, expr.stackPeek().type.toClass());
                }
            }
            expr.writePushConstLong(v.index);
            expr.writeSysDynamicCall(Memory.class, "valueOfIndex", Memory.class, expr.stackPeek().type.toClass());

            if (v.isVariable()){
                LocalVariable variable = method.getLocalVariable(v.getVariableName());
                expr.writeVarAssign(variable, (VariableExprToken) v.var.getSingle(), false, true);
            } else if (v.isArray() || v.isStaticProperty() || v.isArrayPush()) {
                expr.writeExpression(v.var, true, false);
                if (expr.stackPeek().immutable || expr.stackPeek().isConstant())
                    expr.unexpectedToken(v.var.getSingle());

                expr.writeSysStaticCall(Memory.class, "assignRight", Memory.class, Memory.class, Memory.class);
                expr.writePopAll(1);
            } else if (v.isDynamicProperty()){
                DynamicAccessExprToken dynamic = (DynamicAccessExprToken)v.var.getLast();
                ExprStmtToken var = new ExprStmtToken(v.var.getTokens());
                var.getTokens().remove(var.getTokens().size() - 1);

                expr.writeDynamicAccessInfo(dynamic, false);
                expr.writeExpression(var, true, false);
                expr.writePopBoxing(false);

                expr.writeSysStaticCall(ObjectInvokeHelper.class,
                        "assignPropertyRight", Memory.class,
                        Memory.class, String.class, Environment.class, TraceInfo.class, Memory.class
                );
                expr.writePopAll(1);
            }
        }

        if (!returnValue)
            expr.writePopAll(1);
    }
}
