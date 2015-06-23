package org.develnext.jphp.core.compiler.jvm.statement.expr.operator;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import php.runtime.Memory;
import php.runtime.common.Association;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.PropertyCallCache;

public class DynamicAccessCompiler extends BaseExprCompiler<DynamicAccessExprToken> {
    public DynamicAccessCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(DynamicAccessExprToken dynamic, boolean returnValue) {
        expr.writeDynamicAccessPrepare(dynamic, false);

        if (dynamic.getFieldExpr() == null && dynamic.getField() instanceof NameToken && !method.clazz.statement.isTrait()) {
            expr.writeGetStatic("$CALL_PROP_CACHE", PropertyCallCache.class);
            expr.writePushConstInt(method.clazz.getAndIncCallPropCount());
        } else {
            expr.writePushConstNull();
            expr.writePushConstInt(0);
        }

        if (dynamic instanceof DynamicAccessAssignExprToken){
            OperatorExprToken operator = (OperatorExprToken) ((DynamicAccessAssignExprToken) dynamic).getAssignOperator();
            String code = operator.getCode();
            if (operator instanceof IncExprToken || operator instanceof DecExprToken) {
                if (operator.getAssociation() == Association.RIGHT)
                    code = code + "AndGet";
                else
                    code = "GetAnd" + code.substring(0, 1).toUpperCase() + code.substring(1);

                expr.writeSysStaticCall(ObjectInvokeHelper.class,
                        code + "Property", Memory.class,
                        Memory.class, String.class, Environment.class, TraceInfo.class,
                        PropertyCallCache.class, int.class
                );
            } else {
                expr.writeSysStaticCall(ObjectInvokeHelper.class,
                        code + "Property", Memory.class,
                        Memory.class, Memory.class, String.class, Environment.class, TraceInfo.class,
                        PropertyCallCache.class, int.class
                );
            }
        } else if (dynamic instanceof DynamicAccessUnsetExprToken){
            expr.writeSysStaticCall(ObjectInvokeHelper.class,
                    "unsetProperty", void.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class,
                    PropertyCallCache.class, int.class
            );

            if (returnValue)
                expr.writePushNull();
        } else if (dynamic instanceof DynamicAccessEmptyExprToken){
            expr.writeSysStaticCall(ObjectInvokeHelper.class,
                    "emptyProperty", Memory.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class,
                    PropertyCallCache.class, int.class
            );
        } else if (dynamic instanceof DynamicAccessIssetExprToken){
            expr.writeSysStaticCall(ObjectInvokeHelper.class,
                    "issetProperty", Memory.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class,
                    PropertyCallCache.class, int.class
            );
        } else {

            expr.writeSysStaticCall(ObjectInvokeHelper.class,
                    dynamic instanceof  DynamicAccessGetRefExprToken ? "getRefProperty" : "getProperty",
                    Memory.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class,
                    PropertyCallCache.class, int.class
            );
        }
    }
}
