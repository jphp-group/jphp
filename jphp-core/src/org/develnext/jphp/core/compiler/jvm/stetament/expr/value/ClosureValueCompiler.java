package org.develnext.jphp.core.compiler.jvm.stetament.expr.value;

import org.develnext.jphp.core.compiler.jvm.Constants;
import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.stetament.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.ClosureStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ArgumentStmtToken;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.helper.ClosureEntity;

import java.util.Collection;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.AASTORE;

public class ClosureValueCompiler extends BaseExprCompiler<ClosureStmtToken> {
    public ClosureValueCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    protected void writePushUses(Collection<ArgumentStmtToken> parameters){
        if (parameters.isEmpty()){
            add(new InsnNode(ACONST_NULL));
            expr.stackPush(Memory.Type.REFERENCE);
            return;
        }

        expr.writePushSmallInt(parameters.size());
        add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory.class)));
        expr.stackPop();
        expr.stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for(ArgumentStmtToken param : parameters){
            expr.writePushDup();
            expr.writePushSmallInt(i);

            LocalVariable local = method.getLocalVariable(param.getName().getName());
            expr.writeVarLoad(local);
            if (!param.isReference())
                expr.writePopBoxing(true);

            add(new InsnNode(AASTORE));
            expr.stackPop();
            expr.stackPop();
            expr.stackPop();
            i++;
        }
    }

    @Override
    public void write(ClosureStmtToken closure, boolean returnValue) {
        if (returnValue){
            ClosureEntity entity = compiler.getModule().findClosure( closure.getId() );
            boolean thisExists = closure.getFunction().isThisExists();
            if (closure.getFunction().getUses().isEmpty() && !thisExists
                    && closure.getFunction().getStaticLocal().isEmpty()){
                expr.writePushEnv();
                expr.writePushConstString(compiler.getModule().getInternalName());
                expr.writePushConstInt((int) entity.getId());
                expr.writeSysDynamicCall(
                        Environment.class, "__getSingletonClosure", Memory.class, String.class, Integer.TYPE
                );
            } else {
                add(new TypeInsnNode(NEW, entity.getInternalName()));
                expr.stackPush(Memory.Type.REFERENCE);
                expr.writePushDup();

                expr.writePushEnv();
                expr.writePushConstString(compiler.getModule().getInternalName());
                expr.writePushConstInt((int) entity.getId());
                expr.writeSysDynamicCall(Environment.class, "__getClosure", ClassEntity.class, String.class, Integer.TYPE);

                if (thisExists)
                    expr.writePushThis();
                else
                    expr.writePushNull();

                writePushUses(closure.getFunction().getUses());
                add(new MethodInsnNode(
                        INVOKESPECIAL, entity.getInternalName(), Constants.INIT_METHOD,
                        Type.getMethodDescriptor(
                                Type.getType(void.class),
                                Type.getType(ClassEntity.class), Type.getType(Memory.class), Type.getType(Memory[].class)
                        ),
                        false
                ));
                expr.stackPop();
                expr.stackPop();
                expr.stackPop();
                expr.stackPop();

                expr.writeSysStaticCall(ObjectMemory.class, "valueOf", Memory.class, IObject.class);
            }
            expr.setStackPeekAsImmutable();
        }
    }
}
