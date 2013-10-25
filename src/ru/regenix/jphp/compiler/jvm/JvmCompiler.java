package ru.regenix.jphp.compiler.jvm;

import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.entity.ClassEntity;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.FunctionStmtToken;

import java.util.ArrayList;
import java.util.List;

public class JvmCompiler extends AbstractCompiler {

    private List<ClassEntity> classes = new ArrayList<ClassEntity>();

    public JvmCompiler(CompileScope scope, Context context, List<Token> tokens) {
        super(scope, context, tokens);
        this.classes = new ArrayList<ClassEntity>();
    }

    protected void process(){
        this.classes = new ArrayList<ClassEntity>();
        List<ExprStmtToken> externalCode = new ArrayList<ExprStmtToken>();

        for(Token token : tokens){
            if (token instanceof ClassStmtToken){
                ClassEntity entity = new ClassEntity(this, (ClassStmtToken)token);
                entity.getResult();
                classes.add(entity);
            } else if (token instanceof FunctionStmtToken){

            } else if (token instanceof ExprStmtToken){
                externalCode.add((ExprStmtToken)token);
            }
        }

        if (!externalCode.isEmpty()){
            // ..
        }
    }

    public List<ClassEntity> getClasses() {
        return classes;
    }

    public String getSourceFile(){
        return context.getFile() == null ? null : context.getFile().getPath();
    }
}
