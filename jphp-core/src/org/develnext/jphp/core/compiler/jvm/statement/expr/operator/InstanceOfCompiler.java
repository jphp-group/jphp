package org.develnext.jphp.core.compiler.jvm.statement.expr.operator;

import org.develnext.jphp.core.compiler.common.misc.StackItem;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.operator.InstanceofExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import php.runtime.Memory;
import php.runtime.OperatorUtils;

public class InstanceOfCompiler extends BaseExprCompiler<InstanceofExprToken> {
    public InstanceOfCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    protected void pushName(StackItem R) {
        NameToken name = null;
        if (R.getToken() instanceof NameToken)
            name = this.compiler.getAnalyzer().getRealName((NameToken)R.getToken());

        if (name == null) {
            expr.writePush(R);
            expr.writePopString();
            expr.writePushDupLowerCase();
        } else {
            expr.writePushConstString(name.getName());
            expr.writePushConstString(name.getName().toLowerCase());
        }
    }

    @Override
    public void write(InstanceofExprToken instanceOf, boolean returnValue) {
        if (expr.stackEmpty(true))
            expr.unexpectedToken(instanceOf);

        StackItem R = expr.stackPop();
        StackItem L = expr.stackPop();

        boolean isInvert = !R.isKnown();
        if (!R.isKnown() && !L.isKnown() && R.getLevel() > L.getLevel())
            isInvert = false;

        /*if (isInvert) {
            StackItem tmp = R;
            R = L;
            L = tmp;
        }*/

        if (isInvert) {
            pushName(R);

            expr.writePopString();
            expr.writePushDupLowerCase();

            expr.writePush(L);
            if (expr.stackPeek().isConstant())
                expr.unexpectedToken(instanceOf);

            expr.writePopBoxing();

            expr.writeSysStaticCall(
                    OperatorUtils.class, "instanceOfRight", Boolean.TYPE, String.class, String.class, Memory.class
            );

            expr.writePopAll(1);
        } else {
            expr.writePush(L);

            if (expr.stackPeek().isConstant())
                expr.unexpectedToken(instanceOf);

            expr.writePopBoxing();

            pushName(R);
            expr.writeSysDynamicCall(Memory.class, "instanceOf", Boolean.TYPE, String.class, String.class);
        }

        if (!returnValue)
            expr.writePopAll(1);
    }
}
