package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.DoStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import static org.objectweb.asm.Opcodes.GOTO;

public class DoCompiler extends BaseStatementCompiler<DoStmtToken> {
    public DoCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(DoStmtToken token) {
        expr.writeDefineVariables(token.getLocal());

        LabelNode start = expr.writeLabel(node, token.getMeta().getStartLine());
        LabelNode end = new LabelNode();

        method.pushJump(end, start);
        expr.write(token.getBody());
        method.popJump();

        expr.writeConditional(token.getCondition(), end);

        add(new JumpInsnNode(GOTO, start));
        add(end);
        add(new LineNumberNode(token.getMeta().getEndLine(), end));

        expr.writeUndefineVariables(token.getLocal());
    }
}
