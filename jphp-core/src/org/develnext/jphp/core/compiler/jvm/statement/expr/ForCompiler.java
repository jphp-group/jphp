package org.develnext.jphp.core.compiler.jvm.statement.expr;

import java.util.Iterator;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ForStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class ForCompiler extends BaseStatementCompiler<ForStmtToken> {
    public ForCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ForStmtToken token) {
        expr.writeDefineVariables(token.getInitLocal());
        for(ExprStmtToken expr : token.getInitExpr()){
            this.expr.writeExpression(expr, false, false);
        }
        expr.writeUndefineVariables(token.getInitLocal());

        expr.writeDefineVariables(token.getLocal());
        for(VariableExprToken variable : token.getIterationLocal()){
            // TODO optimize this for Dynamic Values of variables
            LocalVariable local = method.getLocalVariable(variable.getName());
            local.setValue(null);
        }

        LabelNode start = expr.writeLabel(node, token.getMeta().getStartLine());
        LabelNode iter = new LabelNode();
        LabelNode end = new LabelNode();

	for (Iterator<ExprStmtToken> i = token.getConditionExpr().iterator(); i.hasNext();) {
	    ExprStmtToken expr = i.next();
	    if (i.hasNext()) {
		this.expr.writeExpression(expr, false, false);
	    } else {
		this.expr.writeExpression(expr, true, false);
		this.expr.writePopBoolean();

		add(new JumpInsnNode(IFEQ, end));
		this.expr.stackPop();
	    }
        }

        method.pushJump(end, iter);
        expr.write(BodyStmtToken.class, token.getBody());
        method.popJump();

        add(iter);
        for(ExprStmtToken expr : token.getIterationExpr()){
            this.expr.writeExpression(expr, false, false);
        }
        add(new JumpInsnNode(GOTO, start));
        add(end);
        add(new LineNumberNode(token.getMeta().getEndLine(), end));
        expr.writeUndefineVariables(token.getLocal());
    }
}
