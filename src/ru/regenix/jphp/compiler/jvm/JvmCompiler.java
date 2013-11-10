package ru.regenix.jphp.compiler.jvm;

import org.objectweb.asm.MethodVisitor;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.ClassStmtCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.ExpressionStmtCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.MethodStmtCompiler;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.util.ArrayList;
import java.util.List;

public class JvmCompiler extends AbstractCompiler {

    private List<ClassStmtCompiler> classes = new ArrayList<ClassStmtCompiler>();

    public JvmCompiler(Environment environment, Context context, List<Token> tokens) {
        super(environment, context, tokens);
        this.classes = new ArrayList<ClassStmtCompiler>();
    }

    public ClassEntity compileClass(ClassStmtToken clazz){
        ClassStmtCompiler cmp = new ClassStmtCompiler(this, clazz);
        return cmp.compile();
    }

    public MethodEntity compileMethod(ClassStmtCompiler clazzCompiler, MethodStmtToken method){
        return new MethodStmtCompiler(clazzCompiler, method).compile();
    }

    public void compileMethod(ClassStmtCompiler clazzCompiler, MethodVisitor mv, String name){
        new MethodStmtCompiler(clazzCompiler, mv, name).compile();
    }

    public void compileExpression(MethodStmtCompiler method, ExprStmtToken expression){
        new ExpressionStmtCompiler(method, expression).compile();
    }

    @Override
    public void compile(boolean autoRegister){
        this.classes = new ArrayList<ClassStmtCompiler>();
        List<ExprStmtToken> externalCode = new ArrayList<ExprStmtToken>();

        for(Token token : tokens){
            if (token instanceof ClassStmtToken){
                ClassEntity entity = compileClass((ClassStmtToken)token);
                if (autoRegister)
                    scope.addUserClass(entity);

            } else if (token instanceof ExprStmtToken){
                externalCode.add((ExprStmtToken)token);
            }
        }

        if (!externalCode.isEmpty()){
            // ..
        }
    }

    public List<ClassStmtCompiler> getClasses() {
        return classes;
    }

    public String getSourceFile(){
        return context.getFile() == null ? null : context.getFile().getAbsolutePath();
    }
}
