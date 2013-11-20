package ru.regenix.jphp.compiler.jvm;

import org.objectweb.asm.MethodVisitor;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.ClassStmtCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.ExpressionStmtCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.FunctionStmtCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.MethodStmtCompiler;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.stmt.*;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JvmCompiler extends AbstractCompiler {

    protected final ModuleEntity module;
    protected NamespaceStmtToken namespace;
    private List<ClassStmtCompiler> classes = new ArrayList<ClassStmtCompiler>();

    public JvmCompiler(Environment environment, Context context) {
        this(environment, context, new SyntaxAnalyzer(new Tokenizer(context)));
    }

    public JvmCompiler(Environment environment, Context context, SyntaxAnalyzer analyzer) {
        super(environment, context, analyzer);
        this.classes = new ArrayList<ClassStmtCompiler>();
        this.module = new ModuleEntity(context);
        this.module.setId( scope.nextModuleIndex() );
    }

    public NamespaceStmtToken getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceStmtToken namespace) {
        this.namespace = namespace;
    }

    public ClassEntity compileClass(ClassStmtToken clazz, boolean external) {
        ClassStmtCompiler cmp = new ClassStmtCompiler(this, clazz);
        cmp.setExternal(external);
        return cmp.compile();
    }

    public ClassEntity compileClass(ClassStmtToken clazz){
        return compileClass(clazz, false);
    }

    public FunctionEntity compileFunction(FunctionStmtToken function){
        FunctionStmtCompiler cmp = new FunctionStmtCompiler(this, function);
        return cmp.compile();
    }

    @SuppressWarnings("unchecked")
    public MethodEntity compileMethod(ClassStmtCompiler clazzCompiler, MethodStmtToken method, boolean external) {
        MethodStmtCompiler compiler = new MethodStmtCompiler(clazzCompiler, method);
        compiler.setExternal(external);
        clazzCompiler.node.methods.add(compiler.node);
        return compiler.compile();
    }

    public MethodEntity compileMethod(ClassStmtCompiler clazzCompiler, MethodStmtToken method){
        return compileMethod(clazzCompiler, method, false);
    }

    public void compileExpression(MethodStmtCompiler method, ExprStmtToken expression) {
        new ExpressionStmtCompiler(method, expression).compile();
    }

    public List<ExprStmtToken> process(List<Token> tokens, NamespaceStmtToken namespace){
        List<ExprStmtToken> externalCode = new ArrayList<ExprStmtToken>();

        for(Token token : tokens){
            if (token instanceof NamespaceStmtToken){
              setNamespace((NamespaceStmtToken)token);
            } if (token instanceof ClassStmtToken){
                ClassEntity entity = compileClass((ClassStmtToken)token);
                module.addClass(entity);
            } else if (token instanceof FunctionStmtToken){
                FunctionEntity entity = compileFunction((FunctionStmtToken)token);
                module.addFunction(entity);
            } else if (token instanceof ExprStmtToken){
                externalCode.add((ExprStmtToken)token);
            } else
                externalCode.add(new ExprStmtToken(token));
        }

        return externalCode;
    }

    @Override
    public ModuleEntity compile(boolean autoRegister) {
        this.classes = new ArrayList<ClassStmtCompiler>();

        List<ExprStmtToken> externalCode = process(tokens, NamespaceStmtToken.getDefault());

        NamespaceStmtToken namespace = new NamespaceStmtToken( TokenMeta.of(module.getModuleNamespace()) );
        namespace.setName(new FulledNameToken( TokenMeta.of( module.getModuleNamespace() ), '\\' ));

        ClassStmtToken token = new ClassStmtToken(TokenMeta.of( module.getName() ));
        token.setFinal(true);
        token.setName(new NameToken(TokenMeta.of( module.getModuleClassName() )));
        token.setNamespace(namespace);

        MethodStmtToken methodToken = new MethodStmtToken( token.getMeta() );
        methodToken.setFinal(true);
        methodToken.setClazz(token);
        methodToken.setModifier(Modifier.PUBLIC);
        methodToken.setStatic(true);
        methodToken.setDynamicLocal(true);
        methodToken.setName((NameToken) Token.of("__include"));
        methodToken.setArguments(new ArrayList<ArgumentStmtToken>());
        methodToken.setLocal(analyzer.getLocalScope());
        methodToken.setBody(BodyStmtToken.of(externalCode));

        token.setMethods(Arrays.asList(methodToken));

        ClassStmtCompiler classStmtCompiler = new ClassStmtCompiler(this, token);
        classStmtCompiler.setExternal(true);
        classStmtCompiler.setSystem(true);

        module.setData(classStmtCompiler.compile().getData());
        // ..

        if (autoRegister)
            scope.addUserModule(module);

        return module;
    }

    public ModuleEntity getModule() {
        return module;
    }

    public List<ClassStmtCompiler> getClasses() {
        return classes;
    }

    public String getSourceFile() {
        try {
            return context.getFile() == null ? null : context.getFile().getCanonicalPath();
        } catch (IOException e) {
            return context.getFile().getAbsolutePath();
        }
    }
}
