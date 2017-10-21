package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.YieldExprToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.InvokeHelper;
import php.runtime.lang.Generator;

public class YieldValueCompiler extends BaseExprCompiler<YieldExprToken> {
    public YieldValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(YieldExprToken token, boolean returnValue) {
        if (token.isOnlyNext() && token.getValue() != null) {
            return;
        }

        expr.writeVarLoad("~this");
        expr.writePushEnv();
        expr.writePushTraceInfo(token);

        String methodName = token.isDelegating() ? "yieldFrom" : "yield";

        if (token.getValue() == null) {
            expr.writeSysDynamicCall(Generator.class, methodName, Memory.class, Environment.class, TraceInfo.class);
        } else {
            expr.writeExpression(token.getValue(), true, false);
            expr.writePopBoxing();

            if (expr.getMethod().getGeneratorEntity().isReturnReference()) {
                expr.setStackPeekAsImmutable();

                    expr.writePushDup();
                    expr.writePushEnv();
                    expr.writePushTraceInfo(token);
                    expr.writeSysStaticCall(
                            InvokeHelper.class,
                            "checkYieldReference",
                            void.class,
                            Memory.class, Environment.class, TraceInfo.class
                    );

                expr.writeSysDynamicCall(Generator.class, methodName, Memory.class, Environment.class, TraceInfo.class, Memory.class);
            } else {
                expr.writeSysDynamicCall(Generator.class, methodName, Memory.class, Environment.class, TraceInfo.class, Memory.class);
            }
        }

        if (!returnValue) {
            expr.writePopAll(1);
        }
    }
}
