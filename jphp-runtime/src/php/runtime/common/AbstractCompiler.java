package php.runtime.common;

import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.reflection.ModuleEntity;

abstract public class AbstractCompiler {

    protected final Environment environment;
    protected final CompileScope scope;
    protected final Context context;

    protected LangMode langMode = LangMode.PHP;

    public AbstractCompiler(Environment environment, Context context){
        this.context = context;
        this.scope = environment.getScope();
        this.environment = environment;
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

    abstract public ModuleEntity compile(boolean autoRegister);

    public ModuleEntity compile(){
        return compile(true);
    }

    public Environment getEnvironment() {
        return environment;
    }
}
