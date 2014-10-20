package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.YieldExprToken;
import php.runtime.Memory;
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
        if (token.isOnlyGet() && !token.isOnlyNext() && token.getValue() == null) {
            expr.writeSysDynamicCall(Generator.class, "__current", Memory.class);
        } else if (token.getValue() == null) {
            expr.writeSysDynamicCall(Generator.class, "yield", Memory.class);
        } else {

            expr.writeExpression(token.getValue(), true, false);
            expr.writePopBoxing();

            if (expr.getMethod().getGeneratorEntity().isReturnReference()) {
                expr.setStackPeekAsImmutable();
                expr.writeSysDynamicCall(Generator.class, "yield", Memory.class, Memory.class);
            } else {
                expr.writeSysDynamicCall(Generator.class, "yield", Memory.class, Memory.class);
            }
        }

        if (!returnValue) {
            expr.writePopAll(1);
        }
    }
}
