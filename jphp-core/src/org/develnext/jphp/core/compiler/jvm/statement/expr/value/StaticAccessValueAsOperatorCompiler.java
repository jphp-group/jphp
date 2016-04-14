package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseStatementCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.ClassExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.StaticAccessOperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.ConstantCallCache;
import php.runtime.invoke.cache.PropertyCallCache;

public class StaticAccessValueAsOperatorCompiler extends BaseExprCompiler<StaticAccessOperatorExprToken> {
    public StaticAccessValueAsOperatorCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(StaticAccessOperatorExprToken token, boolean returnValue) {
        StaticAccessExprToken origin = token.getOrigin();

        BaseExprCompiler<StaticAccessExprToken> compiler = (BaseExprCompiler<StaticAccessExprToken>) expr.getCompiler(StaticAccessExprToken.class);
        compiler.write(origin, returnValue);
    }
}
