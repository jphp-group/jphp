package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DynamicAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.ListExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.PropertyCallCache;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.support.MemoryUtils;

public class ListCompiler extends BaseExprCompiler<ListExprToken> {

    public static final String VALUE_FOR_LIST_METHOD = "valueForList";
    public static final String REF_VALUE_FOR_LIST_METHOD = "refValueForList";

    public ListCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ListExprToken list, boolean returnValue) {
        write(list, returnValue, true);
    }

    public void write(ListExprToken list, boolean returnValue, boolean writeValue) {
        if (writeValue) {
            expr.writeExpression(list.getValue(), true, false);
            expr.writePopBoxing();
        }

        if (list.isHasRef() && (list.getValue() != null && list.getValue().isSingle() && ValueExprToken.isConstable(list.getValue().getSingle(), true))) {
            compiler.getEnvironment().error(
                    list.getValue().toTraceInfo(compiler.getContext()),
                    Messages.ERR_CANNOT_ASSIGN_REF_TO_NON_REF_VALUE.fetch()
            );
        }

        int i, length = list.getVariables().size();
        for(i = 0; i < length; i++) { // desc order as in PHP
            ListExprToken.Variable v = list.getVariables().get(i);
            expr.writePushDup();

            if (v.indexes != null) {
                for(Object index : v.indexes) {
                    expr.writePushTraceInfo(v.var);
                    if (v.isRef) {
                        expr.writePushConstBoolean(true);
                    }

                    if (index instanceof Integer) {
                        expr.writePushConstLong((Integer) index);
                    } else {
                        expr.writeExpression((ExprStmtToken) index, true, false);
                        switch (expr.stackPeek().type) {
                            case INT:
                            case STRING:
                                break;

                            default:
                                expr.writePopBoxing();
                                break;
                        }
                    }

                    //expr.writePushTraceInfo(list);
                    if (v.isRef) {
                        expr.writeSysStaticCall(
                                MemoryUtils.class,
                                REF_VALUE_FOR_LIST_METHOD,
                                Memory.class, Memory.class, TraceInfo.class, Boolean.TYPE, expr.stackPeek().type.toClass()
                        );
                    } else {
                        expr.writeSysStaticCall(
                                MemoryUtils.class,
                                VALUE_FOR_LIST_METHOD,
                                Memory.class, Memory.class, TraceInfo.class, expr.stackPeek().type.toClass()
                        );
                    }
                    //expr.writeSysDynamicCall(Memory.class, "valueOfIndex", Memory.class, expr.stackPeek().type.toClass());
                }

                expr.writePushTraceInfo(v.var);
            } else {
                expr.writePushTraceInfo(v.var);
            }

            if (v.isWithKey()) {
                expr.writeExpression(v.exprIndex, true, false);
                switch (expr.stackPeek().type) {
                    case INT:
                    case STRING:
                        break;

                    default:
                        expr.writePopBoxing();
                        break;
                }
            } else {
                expr.writePushConstLong(v.index);
            }

            expr.writeSysStaticCall(
                    MemoryUtils.class,
                    v.isRef ? REF_VALUE_FOR_LIST_METHOD : VALUE_FOR_LIST_METHOD,
                    Memory.class, Memory.class, TraceInfo.class, expr.stackPeek().type.toClass()
            );

            if (v.isVariable()){
                LocalVariable variable = method.getLocalVariable(v.getVariableName());
                if (v.isRef) {
                    variable.setReference(false);
                    expr.writePushMemory(new ReferenceMemory());
                    expr.writeVarAssign(variable, (VariableExprToken) v.var.getSingle(), false, true);
                    variable.setReference(true);
                }

                expr.writeVarAssign(variable, (VariableExprToken) v.var.getSingle(), false, !v.isRef);
            } else if (v.isArray() || v.isStaticProperty() || v.isArrayPush()) {
                expr.writeExpression(v.var, true, false);
                if (expr.stackPeek().immutable || expr.stackPeek().isConstant())
                    expr.unexpectedToken(v.var.getSingle());

                expr.writeSysStaticCall(Memory.class, "assignRight", Memory.class, Memory.class, Memory.class);
                expr.writePopAll(1);
            } else if (v.isDynamicProperty()){
                DynamicAccessExprToken dynamic = (DynamicAccessExprToken)v.var.getLast();
                ExprStmtToken var = new ExprStmtToken(this.env, this.compiler.getContext(), v.var.getTokens());
                var.getTokens().remove(var.getTokens().size() - 1);
                var.updateAsmExpr(this.env, this.compiler.getContext());

                expr.writeDynamicAccessInfo(dynamic, false);
                expr.writeExpression(var, true, false);
                expr.writePopBoxing(false);

                expr.writeGetStatic("$CALL_PROP_CACHE", PropertyCallCache.class);
                expr.writePushConstInt(method.clazz.getAndIncCallPropCount());

                expr.writeSysStaticCall(ObjectInvokeHelper.class,
                        "assignPropertyRight", Memory.class,
                        Memory.class, String.class, Environment.class, TraceInfo.class, Memory.class,
                        PropertyCallCache.class, int.class
                );
                expr.writePopAll(1);
            }
        }

        if (!returnValue)
            expr.writePopAll(1);
    }
}
