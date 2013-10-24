package ru.regenix.jphp.compiler.jvm;

import org.objectweb.asm.*;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.common.ASMExpression;
import ru.regenix.jphp.compiler.jvm.entity.ClassEntity;
import ru.regenix.jphp.compiler.jvm.runtime.Memory;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ConstStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.FunctionStmtToken;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class JvmCompiler extends AbstractCompiler {

    private List<ClassWriter> classes;

    public JvmCompiler(Environment environment, Context context, List<Token> tokens) {
        super(environment, context, tokens);
        this.classes = new ArrayList<ClassWriter>();
    }

    protected void writeExpression(ExprStmtToken token, MethodVisitor mv){
        ASMExpression asmExpression = new ASMExpression(context, token, true);

        for(Token el : asmExpression.getResult().getTokens()){

        }
    }


    protected void writeConstant(ConstStmtToken token, ClassWriter cw){
        FieldVisitor fv = cw.visitField(
                ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                token.getName().getName(),
                MEMORY_CLASS,
                null,
                null
        );
        fv.visitEnd();
    }

    protected void writeInitStatic(ClassStmtToken token, ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_STATIC, STATIC_INIT_METHOD, "()V", null, null);
        mv.visitCode();

        for(ConstStmtToken constant : token.getConstants()){
            writeExpression(constant.getValue(), mv);
            mv.visitFieldInsn(
                    PUTSTATIC,
                    token.getFulledName(NAME_DELIMITER),
                    constant.getName().getName(),
                    Constants.MEMORY_CLASS
            );
        }

        mv.visitEnd();
    }

    protected void writeClass(ClassStmtToken token){
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_6, ACC_SUPER, token.getFulledName(NAME_DELIMITER), null, OBJECT_CLASS, null);
        cw.visitSource(getSourceFile(), null);
        {
            writeConstructor(token, cw);
            for(ConstStmtToken constant : token.getConstants()){
                writeConstant(constant, cw);
            }

            writeInitStatic(token, cw);
        }
        cw.visitEnd();
        classes.add(cw);
    }

    protected void writeExternalCode(List<ExprStmtToken> code){

    }

    protected void process(){
        this.classes = new ArrayList<ClassWriter>();
        List<ExprStmtToken> externalCode = new ArrayList<ExprStmtToken>();

        for(Token token : tokens){
            if (token instanceof ClassStmtToken){
                ClassEntity entity = new ClassEntity((ClassStmtToken)token);

                writeClass((ClassStmtToken)token);
            } else if (token instanceof FunctionStmtToken){

            } else if (token instanceof ExprStmtToken){
                externalCode.add((ExprStmtToken)token);
            }
        }

        if (!externalCode.isEmpty()){
            writeExternalCode(externalCode);
        }
    }

    public List<ClassWriter> getClasses() {
        return classes;
    }

    public String getSourceFile(){
        return context.getFile() == null ? null : context.getFile().getPath();
    }

}
