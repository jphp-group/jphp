package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.IfStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import php.runtime.Memory;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class IfElseCompiler extends BaseStatementCompiler<IfStmtToken> {

    public IfElseCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    private void writeBody(IfStmtToken token) {
        LabelNode end = new LabelNode();
        LabelNode elseL = new LabelNode();

        expr.writePopBoolean();
        add(new JumpInsnNode(IFEQ, token.getElseBody() != null ? elseL : end));
        expr.stackPop();

        if (token.getBody() != null) {
            expr.write(token.getBody());
        }

        if (token.getElseBody() != null){
            add(new JumpInsnNode(GOTO, end));
            add(elseL);
            expr.write(token.getElseBody());
        }

        add(end);
        add(new LineNumberNode(token.getMeta().getEndLine(), end));
    }

    @Override
    public void write(IfStmtToken token) {
        expr.writeDefineVariables(token.getLocal());
        Memory memory = expr.writeExpression(token.getCondition(), true, true);

        boolean isConstantly = token.getBody() != null && token.getBody().isConstantly()
                && (token.getElseBody() == null || token.getElseBody().isConstantly());

        if (isConstantly && memory != null){
            if (memory.toBoolean()){
                expr.write(BodyStmtToken.class, token.getBody());
            } else {
                expr.write(BodyStmtToken.class, token.getElseBody());
            }
        } else {
            if (memory != null)
                expr.writePushMemory(memory);
            writeBody(token);
        }
        expr.writeUndefineVariables(token.getLocal());
    }
}
