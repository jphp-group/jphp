package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.ClassExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.ConstantCallCache;
import php.runtime.invoke.cache.MethodCallCache;
import php.runtime.invoke.cache.PropertyCallCache;

public class StaticAccessValueCompiler extends BaseExprCompiler<StaticAccessExprToken> {
    public StaticAccessValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(StaticAccessExprToken token, boolean returnValue) {
        boolean isConstant = token.getField() instanceof NameToken;
        /*if (token.getField() == null)
            expr.unexpectedToken(token.getFieldExpr().getSingle());*/

        boolean lateStaticCall = false;

        if (token.getField() instanceof ClassExprToken) {
            if (token.getClazz() instanceof ParentExprToken) {
                expr.writePushParent(token.getClazz());
            } else if (token.getClazz() instanceof StaticExprToken) {
                expr.writePushStatic();
                lateStaticCall = true;
            } else if (token.getClazz() instanceof SelfExprToken) {
                expr.writePushSelf(false);
            } else
                expr.unexpectedToken(token);
        } else {
            ValueExprToken clazz = token.getClazz();

            if (clazz instanceof ParentExprToken){
                expr.writePushParent(clazz);
            } else if (clazz instanceof NameToken){
                expr.writePushConstString(((NameToken) clazz).getName());
                expr.writePushConstString(((NameToken) clazz).getName().toLowerCase());
            } else {
                if (clazz instanceof StaticExprToken){
                    expr.writePushStatic();
                    lateStaticCall = true;
                } else {
                    if (clazz != null) {
                        expr.writePush(clazz, true, false);
                    } else {
                        // it static access operator. skip.
                    }
                }

                expr.writePopString();
                expr.writePushDupLowerCase();
            }

            if (isConstant) {
                expr.writePushConstString(((NameToken) token.getField()).getName());
                expr.writePushEnv();
                expr.writePushTraceInfo(token);

                int cacheIndex = method.clazz.getAndIncCallConstCount();
                expr.writeGetStatic("$CALL_CONST_CACHE", ConstantCallCache.class);
                expr.writePushConstInt(cacheIndex);
                expr.writePushConstBoolean(lateStaticCall);

                expr.writeSysStaticCall(ObjectInvokeHelper.class, "getConstant",
                        Memory.class, String.class, String.class, String.class, Environment.class, TraceInfo.class,
                        ConstantCallCache.class, Integer.TYPE, boolean.class
                );
                expr.setStackPeekAsImmutable();
            } else {
                if (token.getFieldExpr() != null) {
                    expr.writeExpression(token.getFieldExpr(), true, false);
                    expr.writePopString();
                } else {
                    if (!(token.getField() instanceof VariableExprToken))
                        expr.unexpectedToken(token.getField());

                    expr.writePushConstString(((VariableExprToken) token.getField()).getName());
                }
                expr.writePushEnv();
                expr.writePushTraceInfo(token);

                String name = "get";
                if (token instanceof StaticAccessUnsetExprToken)
                    name = "unset";
                else if (token instanceof StaticAccessIssetExprToken)
                    name = "isset";

                if (token.getFieldExpr() == null && token.getField() instanceof NameToken) {
                    expr.writeGetStatic("$CALL_PROP_CACHE", PropertyCallCache.class);
                    expr.writePushConstInt(method.clazz.getAndIncCallPropCount());
                } else {
                    expr.writePushConstNull();
                    expr.writePushConstInt(0);
                }

                expr.writePushConstBoolean(lateStaticCall);
                expr.writeSysStaticCall(ObjectInvokeHelper.class,
                        name + "StaticProperty",
                        Memory.class, String.class, String.class, String.class, Environment.class, TraceInfo.class,
                        PropertyCallCache.class, int.class, boolean.class
                );
            }
        }

        if (!returnValue)
            expr.writePopAll(1);
    }
}
