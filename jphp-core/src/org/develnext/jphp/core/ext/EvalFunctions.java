package org.develnext.jphp.core.ext;

import php.runtime.common.AbstractCompiler;
import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import php.runtime.Memory;
import php.runtime.annotation.Runtime;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.ParseException;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ModuleEntity;

public class EvalFunctions extends FunctionsContainer {

    private static String evalErrorMessage(ErrorException e){
        return e.getMessage() + ", eval()'s code on line " + (e.getTraceInfo().getStartLine() + 1)
                + ", position " + (e.getTraceInfo().getStartPosition() + 1);
    }

    protected final static ThreadLocal<SyntaxAnalyzer> syntaxAnalyzer = new ThreadLocal<SyntaxAnalyzer>(){
        @Override
        protected SyntaxAnalyzer initialValue() {
            return new SyntaxAnalyzer(null, null);
        }
    };

    public static Memory eval(Environment env, TraceInfo trace, @Runtime.GetLocals ArrayMemory locals, String code)
            throws Throwable {
        Context context = new Context(code);
        try {
            Tokenizer tokenizer = new Tokenizer(context);
            SyntaxAnalyzer analyzer = syntaxAnalyzer.get();
            analyzer.reset(env, tokenizer);
            AbstractCompiler compiler = new JvmCompiler(env, context, analyzer);

            ModuleEntity module = compiler.compile();
            env.scope.loadModule(module);
            env.registerModule(module);

            return module.include(env, locals);
        } catch (ErrorException e){
            if (e.getType() == ErrorType.E_PARSE){
                if (env.isHandleErrors(ErrorType.E_PARSE))
                    throw new ParseException(evalErrorMessage(e), trace);
            } else
                env.error(trace, e.getType(), evalErrorMessage(e));
        }
        return Memory.FALSE;
    }
}
