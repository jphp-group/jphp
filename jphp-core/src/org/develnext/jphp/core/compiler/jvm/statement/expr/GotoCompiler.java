package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.GotoStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.LabelStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

import static org.objectweb.asm.Opcodes.GOTO;

public class GotoCompiler extends BaseStatementCompiler<GotoStmtToken> {
    public GotoCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(GotoStmtToken token) {
        LabelNode labelNode = method.getOrCreateGotoLabel(token.getLabel().getName());
        LabelStmtToken labelStmtToken = method.statement.findLabel(token.getLabel().getName());
        if (labelStmtToken == null) {
            compiler.getEnvironment().error(
                    token.getLabel().toTraceInfo(compiler.getContext()),
                    "'goto' to undefined label '%s'", token.getLabel().getName()
            );
            return;
        }

        if (labelStmtToken.getLevel() > token.getLevel()) {
            compiler.getEnvironment().error(
                    token.toTraceInfo(compiler.getContext()),
                    "'goto' into loop, switch or finally statement is disallowed"
            );
        }

        add(new JumpInsnNode(GOTO, labelNode));
    }
}
