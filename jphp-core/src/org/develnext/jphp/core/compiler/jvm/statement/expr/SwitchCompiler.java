package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.CaseStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.SwitchStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import php.runtime.Memory;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class SwitchCompiler extends BaseStatementCompiler<SwitchStmtToken> {
    public SwitchCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(SwitchStmtToken token) {
        expr.writeDefineVariables(token.getLocal());

        LabelNode l = new LabelNode();
        LabelNode end = new LabelNode();

        add(l);
        LocalVariable switchValue = method.addLocalVariable(
                "~switch~" + method.nextStatementIndex(Memory.class), l, Memory.class
        );
        switchValue.setEndLabel(end);


        LabelNode[][] jumps = new LabelNode[token.getCases().size() + 1][2];
        int i = 0;
        for(CaseStmtToken one : token.getCases()) {
            jumps[i] = new LabelNode[]{ new LabelNode(), new LabelNode() }; // checkLabel, bodyLabel
            if (i == jumps.length - 1)
                jumps[i] = new LabelNode[]{ end, end };

            i++;
        }
        jumps[jumps.length - 1] = new LabelNode[]{end, end};


        method.pushJump(end, end);
        expr.writeExpression(token.getValue(), true, false);
        expr.writePopBoxing();

        expr.writeVarStore(switchValue, false, false);

        i = 0;
        for(CaseStmtToken one : token.getCases()){
            add(jumps[i][0]); // conditional

            if (one.getConditional() != null){
                expr.writeVarLoad(switchValue);
                expr.writeExpression(one.getConditional(), true, false);
                expr.writeSysDynamicCall(Memory.class, "equal", Boolean.TYPE, expr.stackPeek().type.toClass());
                add(new JumpInsnNode(IFEQ, jumps[i + 1][0]));
                expr.stackPop();
            }

            add(new JumpInsnNode(GOTO, jumps[i][1])); // if is done...
            i++;
        }

        i = 0;
        for(CaseStmtToken one : token.getCases()){
            add(jumps[i][1]);
            expr.writeDefineVariables(one.getLocals());

            expr.write(BodyStmtToken.class, one.getBody());
            i++;

            expr.writeUndefineVariables(one.getLocals());
        }

        method.popJump();
        add(end);

        add(new LineNumberNode(token.getMeta().getEndLine(), end));
        method.prevStatementIndex(Memory.class);
        expr.writeUndefineVariables(token.getLocal());
    }
}
