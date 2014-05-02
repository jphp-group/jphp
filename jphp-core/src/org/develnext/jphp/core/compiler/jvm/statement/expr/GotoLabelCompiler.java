package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.LabelStmtToken;
import org.objectweb.asm.tree.LabelNode;

public class GotoLabelCompiler extends BaseStatementCompiler<LabelStmtToken> {
    public GotoLabelCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(LabelStmtToken token) {
        LabelNode labelNode = method.getOrCreateGotoLabel(token.getName());
        add(labelNode);
    }
}
