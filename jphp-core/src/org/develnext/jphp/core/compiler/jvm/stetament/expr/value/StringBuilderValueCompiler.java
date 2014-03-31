package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.common.misc.StackItem;
import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringBuilderExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class StringBuilderValueCompiler extends BaseExprCompiler<StringBuilderExprToken> {
    public StringBuilderValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(StringBuilderExprToken value, boolean returnValue) {
        expr.writePushNewObject(StringBuilder.class);

        for(Token el : value.getExpression()){
            //writePushDup();
            if (el instanceof ValueExprToken){
                expr.writePush((ValueExprToken) el, true, false);
            } else if (el instanceof ExprStmtToken){
                //unexpectedToken(el);
                expr.writeExpression((ExprStmtToken) el, true, false, true);
            } else
                expr.unexpectedToken(el);

            StackItem.Type peek = expr.stackPeek().type;
            if (!peek.isConstant()) {
                expr.writeSysDynamicCall(StringBuilder.class, "append", StringBuilder.class, Object.class);
            } else
                expr.writeSysDynamicCall(StringBuilder.class, "append", StringBuilder.class, peek.toClass());
        }

        expr.writeSysDynamicCall(StringBuilder.class, "toString", String.class);
    }
}
