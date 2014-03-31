package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.ClassExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.ObjectInvokeHelper;

public class StaticAccessValueCompiler extends BaseExprCompiler<StaticAccessExprToken> {
    public StaticAccessValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(StaticAccessExprToken token, boolean returnValue) {
        boolean isConstant = token.getField() instanceof NameToken;
        if (token.getField() == null)
            expr.unexpectedToken(token.getFieldExpr().getSingle());

        if (token.getField() instanceof ClassExprToken) {
            if (token.getClazz() instanceof ParentExprToken) {
                expr.writePushParent(token.getClazz());
            } else if (token.getClazz() instanceof StaticExprToken) {
                expr.writePushStatic();
            } else if (token.getClazz() instanceof SelfExprToken) {
                expr.writePushSelf(false);
            } else
                expr.unexpectedToken(token);
        } else {
            ValueExprToken clazz = token.getClazz();
            if (clazz instanceof NameToken){
                expr.writePushConstString(((NameToken) clazz).getName());
                expr.writePushConstString(((NameToken) clazz).getName().toLowerCase());
            } else {
                if (clazz instanceof ParentExprToken){
                    expr.writePushParent(clazz);
                } else if (clazz instanceof StaticExprToken){
                    expr.writePushStatic();
                } else
                    expr.writePush(clazz, true, false);
                expr.writePopString();
                expr.writePushDupLowerCase();
            }

            if (isConstant){
                expr.writePushConstString(((NameToken) token.getField()).getName());
                expr.writePushEnv();
                expr.writePushTraceInfo(token);

                expr.writeSysStaticCall(ObjectInvokeHelper.class, "getConstant",
                        Memory.class, String.class, String.class, String.class, Environment.class, TraceInfo.class
                );
                expr.setStackPeekAsImmutable();
            } else {
                if (!(token.getField() instanceof VariableExprToken))
                    expr.unexpectedToken(token.getField());

                expr.writePushConstString(((VariableExprToken) token.getField()).getName());
                expr.writePushEnv();
                expr.writePushTraceInfo(token);

                String name = "get";
                if (token instanceof StaticAccessUnsetExprToken)
                    name = "unset";
                else if (token instanceof StaticAccessIssetExprToken)
                    name = "isset";

                expr.writeSysStaticCall(ObjectInvokeHelper.class,
                        name + "StaticProperty",
                        Memory.class, String.class, String.class, String.class, Environment.class, TraceInfo.class
                );
            }

        }

        if (!returnValue)
            expr.writePopAll(1);
    }
}
