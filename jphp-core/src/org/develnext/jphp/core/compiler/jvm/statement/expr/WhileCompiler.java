package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.WhileStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import static org.objectweb.asm.Opcodes.GOTO;

public class WhileCompiler extends BaseStatementCompiler<WhileStmtToken> {
    public WhileCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(WhileStmtToken token) {
        expr.writeDefineVariables(token.getLocal());

        LabelNode start = expr.writeLabel(node, token.getMeta().getStartLine());
        LabelNode end = new LabelNode();

        expr.writeConditional(token.getCondition(), end);

        method.pushJump(end, start);
        expr.write(BodyStmtToken.class, token.getBody());
        method.popJump();

        add(new JumpInsnNode(GOTO, start));
        add(end);
        add(new LineNumberNode(token.getMeta().getEndLine(), end));

        expr.writeUndefineVariables(token.getLocal());
    }
}
