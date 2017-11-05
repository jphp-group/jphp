package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.ImportExprToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;

public class ImportCompiler extends BaseExprCompiler<ImportExprToken> {
    public ImportCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ImportExprToken token, boolean returnValue) {
        method.getEntity().setImmutable(false); // fix

        expr.writePushEnv();

        expr.writeExpression(token.getValue(), true, false);
        expr.writePopString();

        expr.writePushLocal();
        expr.writePushTraceInfo(token);

        String name = "__" + token.getCode();

        expr.writeSysDynamicCall(Environment.class, name, Memory.class,
                String.class, ArrayMemory.class, TraceInfo.class
        );

        if (!returnValue)
            expr.writePopAll(1);
    }
}
