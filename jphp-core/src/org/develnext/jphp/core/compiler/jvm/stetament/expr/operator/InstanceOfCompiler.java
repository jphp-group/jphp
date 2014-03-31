package org.develnext.jphp.core.compiler.jvm.stetament.expr.operator;

import org.develnext.jphp.core.compiler.common.misc.StackItem;
import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.operator.InstanceofExprToken;
import php.runtime.Memory;

public class InstanceOfCompiler extends BaseExprCompiler<InstanceofExprToken> {
    public InstanceOfCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(InstanceofExprToken instanceOf, boolean returnValue) {
        if (expr.stackEmpty(true))
            expr.unexpectedToken(instanceOf);

        StackItem o = expr.stackPop();
        expr.writePush(o);

        if (expr.stackPeek().isConstant())
            expr.unexpectedToken(instanceOf);

        expr.writePopBoxing();

        if (instanceOf.isVariable()){
            expr.writePushVariable(instanceOf.getWhatVariable());
            expr.writePopString();
            expr.writePushDupLowerCase();
        } else {
            expr.writePushConstString(instanceOf.getWhat().getName());
            expr.writePushConstString(instanceOf.getWhat().getName().toLowerCase());
        }

        expr.writeSysDynamicCall(Memory.class, "instanceOf", Boolean.TYPE, String.class, String.class);

        if (!returnValue)
            expr.writePopAll(1);
    }
}
