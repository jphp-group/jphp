package ru.regenix.jphp.compiler;

import ru.regenix.jphp.common.LangMode;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.List;

abstract public class AbstractCompiler {

    protected final Environment environment;
    protected final CompileScope scope;
    protected final SyntaxAnalyzer analyzer;
    protected final List<Token> tokens;
    protected final Context context;

    protected final LangMode langMode;

    public AbstractCompiler(Environment environment, Context context, SyntaxAnalyzer analyzer){
        this.tokens = analyzer.getTree();
        this.analyzer = analyzer;
        this.context = context;
        this.scope = environment.getScope();
        this.environment = environment;
        this.langMode = analyzer.getLangMode();
    }

    public LangMode getLangMode(){
        return langMode == null ? scope.getLangMode() : langMode;
    }

    public boolean isMode(LangMode langMode){
        return getLangMode() == langMode;
    }

    public boolean isPhpMode(){
        return isMode(LangMode.PHP);
    }

    public Context getContext() {
        return context;
    }

    public CompileScope getScope() {
        return scope;
    }

    public SyntaxAnalyzer getAnalyzer() {
        return analyzer;
    }

    abstract public ModuleEntity compile(boolean autoRegister);

    public ModuleEntity compile(){
        return compile(true);
    }

    public Environment getEnvironment() {
        return environment;
    }
}
