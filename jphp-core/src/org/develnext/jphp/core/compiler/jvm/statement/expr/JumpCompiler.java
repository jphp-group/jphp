package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.misc.JumpItem;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.BreakStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ContinueStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.JumpStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import php.runtime.common.Messages;
import php.runtime.exceptions.support.ErrorType;

import static org.objectweb.asm.Opcodes.GOTO;

public class JumpCompiler extends BaseStatementCompiler<JumpStmtToken> {
    public JumpCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(JumpStmtToken token) {
        int level = token.getLevel();
        JumpItem jump = method.getJump(level);

        if (jump == null){
            env.error(
                    token.toTraceInfo(compiler.getContext()),
                    ErrorType.E_COMPILE_ERROR,
                    level == 1
                            ? Messages.ERR_CANNOT_JUMP.fetch()
                            : Messages.ERR_CANNOT_JUMP_TO_LEVEL.fetch(level)
            );
            return;
        }

        if (token instanceof ContinueStmtToken){
            add(new JumpInsnNode(GOTO, jump.continueLabel));
        } else if (token instanceof BreakStmtToken){
            add(new JumpInsnNode(GOTO, jump.breakLabel));
        }
    }
}
