package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;
import php.runtime.Memory;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.StringMemory;

public class StringValueCompiler extends BaseExprCompiler<StringExprToken> {
    public StringValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(StringExprToken token, boolean returnValue) {
        if (token.isBinary()) {
            expr.writePushConstString(token.getValue());
            expr.writeSysStaticCall(BinaryMemory.class, "valueOf", Memory.class, String.class);
        } else
            expr.writePushMemory(new StringMemory(token.getValue()));
    }
}
