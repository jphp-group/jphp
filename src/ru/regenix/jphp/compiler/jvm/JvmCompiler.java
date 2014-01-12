package ru.regenix.jphp.compiler.jvm;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.jvm.stetament.*;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.reflection.helper.ClosureEntity;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.*;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.ClosureStmtToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.stmt.*;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.IOException;
import java.util.*;

public class JvmCompiler extends AbstractCompiler {
    protected final ModuleEntity module;
    protected NamespaceStmtToken namespace;
    private List<ClassStmtCompiler> classes = new ArrayList<ClassStmtCompiler>();
    private Map<String, ConstantEntity> constants = new LinkedHashMap<String, ConstantEntity>();
    private Map<String, FunctionEntity> functions = new LinkedHashMap<String, FunctionEntity>();

    public JvmCompiler(Environment environment, Context context) {
        this(environment, context, new SyntaxAnalyzer(environment, new Tokenizer(context)));
    }

    public JvmCompiler(Environment environment, Context context, SyntaxAnalyzer analyzer) {
        super(environment, context, analyzer);
        this.classes = new ArrayList<ClassStmtCompiler>();
        this.module = new ModuleEntity(context, getLangMode());
        this.module.setId( scope.nextModuleIndex() );
    }

    public ConstantEntity findConstant(String shortName){
        return constants.get(shortName);
    }

    public FunctionEntity findFunction(String name){
        return functions.get(name.toLowerCase());
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

    public List<ConstantEntity> compileConstant(ConstStmtToken constant){
        List<ConstantEntity> result = new ArrayList<ConstantEntity>();
        for(ConstStmtToken.Item el : constant.items){
            ConstantEntity constantEntity = new ConstantEntity(getContext());
            constantEntity.setName(el.getFulledName());
            constantEntity.setModule(module);
            constantEntity.setTrace(el.name.toTraceInfo(context));

            ExpressionStmtCompiler expressionStmtCompiler = new ExpressionStmtCompiler(this);
            Memory memory = expressionStmtCompiler.writeExpression(el.value, true, true, false);
            if (memory == null){
                throw new CompileException(
                        Messages.ERR_EXPECTED_CONST_VALUE.fetch(el.getFulledName()),
                        constant.toTraceInfo(context)
                );
            }

            constantEntity.setValue(memory);
            result.add(constantEntity);
        }
        return result;
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

        // write constants
        for(ConstStmtToken constant : analyzer.getConstants()){
            List<ConstantEntity> items = compileConstant(constant);

            for(ConstantEntity el : items){
                if (!constants.containsKey(el.getName())){
                    module.addConstant(el);

                    if (scope.findUserConstant(el.getName()) != null){
                        environment.error(el.getTrace(), ErrorType.E_ERROR,
                            Messages.ERR_CANNOT_REDECLARE_CONSTANT,
                            el.getName()
                        );
                    }

                    constants.put(el.getShortName(), el);
                } else {
                    environment.error(el.getTrace(), ErrorType.E_ERROR,
                        Messages.ERR_CANNOT_REDECLARE_CONSTANT,
                        el.getName()
                    );
                }
            }
        }

        // write closures
        for(ClosureStmtToken closure : analyzer.getClosures()){
            ClosureEntity closureEntity = new ClosureStmtCompiler(this, closure).compile();
            module.addClosure(closureEntity);
        }

        for (FunctionStmtToken function : analyzer.getFunctions()){
            if (!function.isStatic()) {
                FunctionEntity entity = compileFunction(function);
                entity.setStatic(function.isStatic());
                module.addFunction(entity);
            }
        }

        for(Token token : tokens) {
            if (token instanceof NamespaceStmtToken){
              setNamespace((NamespaceStmtToken)token);
            } if (token instanceof ClassStmtToken){
                ClassStmtCompiler cmp = new ClassStmtCompiler(this, (ClassStmtToken)token);
                ClassEntity entity = cmp.compile();
                entity.setStatic(true);
                module.addClass(entity);

                if (cmp.isInitDynamicExists()){
                    externalCode.add(new ExprStmtToken(
                        new ClassInitEnvironment((ClassStmtToken)token, entity)
                    ));
                }
            } else if (token instanceof FunctionStmtToken){
                FunctionEntity entity = compileFunction((FunctionStmtToken)token);
                entity.setStatic(true);
                module.addFunction(entity);
                functions.put(entity.getLowerName(), entity);
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

    public class ClassInitEnvironment extends StmtToken {
        protected final ClassStmtToken token;
        protected final ClassEntity entity;

        public ClassInitEnvironment(ClassStmtToken token, ClassEntity clazz) {
            super(token.getMeta(), TokenType.T_J_CUSTOM);
            this.token = token;
            this.entity = clazz;
        }

        public ClassStmtToken getToken() {
            return token;
        }

        public ClassEntity getEntity() {
            return entity;
        }
    }
}
