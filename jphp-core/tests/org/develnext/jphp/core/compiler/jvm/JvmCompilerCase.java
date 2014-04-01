package org.develnext.jphp.core.compiler.jvm;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tester.Test;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.zend.ext.standard.StringFunctions;
import org.junit.Assert;
import php.runtime.Memory;
import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CustomErrorException;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.CoreExtension;
import php.runtime.ext.SPLExtension;
import php.runtime.lang.UncaughtException;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.util.PrintF;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

abstract public class JvmCompilerCase {
    protected Environment environment = new Environment();
    protected int runIndex = 0;
    protected String lastOutput;

    protected CompileScope newScope(){
        CompileScope compileScope = new CompileScope();
        compileScope.setDebugMode(true);

        compileScope.registerExtension(new CoreExtension());
        compileScope.registerExtension(new SPLExtension());

        return compileScope;
    }

    protected List<Token> getSyntaxTree(Context context){
        Tokenizer tokenizer = null;
        try {
            tokenizer = new Tokenizer(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);
        return analyzer.getTree();
    }

    protected SyntaxAnalyzer getSyntax(Context context){
        Tokenizer tokenizer = null;
        try {
            tokenizer = new Tokenizer(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new SyntaxAnalyzer(environment, tokenizer);
    }

    protected List<Token> getSyntaxTree(String code){
        return getSyntaxTree(new Context(code));
    }

    protected Memory run(String code, boolean returned){
        runIndex += 1;
        Environment environment = new Environment(newScope());
        code = "class TestClass { static function test(){ " + (returned ? "return " : "") + code + "; } }";
        Context context = new Context(code);

        JvmCompiler compiler = new JvmCompiler(environment, context, getSyntax(context));
        ModuleEntity module = compiler.compile();
        environment.getScope().loadModule(module);

        ClassEntity entity = module.findClass("TestClass");
        try {
            return entity.findMethod("test").invokeStatic(environment);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    protected Memory runDynamic(String code, boolean returned){
        runIndex += 1;
        Environment environment = new Environment(newScope());
        code = (returned ? "return " : "") + code + ";";
        Context context = new Context(code);

        JvmCompiler compiler = new JvmCompiler(environment, context, getSyntax(context));
        ModuleEntity module = compiler.compile();
        environment.getScope().loadModule(module);
        try {
            environment.registerModule(module);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        return module.includeNoThrow(environment);
    }



    @SuppressWarnings("unchecked")
    protected Memory includeResource(String name, ArrayMemory globals){
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Environment environment = new Environment(newScope(), output);
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("resources/" + name).getFile());
        Context context = new Context(file);

        JvmCompiler compiler = new JvmCompiler(environment, context, getSyntax(context));
        ModuleEntity module = compiler.compile();
        environment.getScope().loadModule(module);
        try {
            environment.registerModule(module);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        if (globals != null)
            environment.getGlobals().putAll(globals);

        Memory memory = module.includeNoThrow(environment, environment.getGlobals());
        try {
            environment.doFinal();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        lastOutput = output.toString();
        return memory;
    }

    protected String getOutput(){
        return lastOutput;
    }

    protected Memory includeResource(String name){
        return includeResource(name, null);
    }


    public static String rtrim(String s) {
        int i = s.length() - 1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    public void check(String name){
        check(name, false, -1);
    }

    public void check(String name, boolean withErrors) {
        check(name, withErrors, -1);
    }

    public void check(String name, int errorFlags) {
        check(name, false, errorFlags);
    }

    public void check(String name, boolean withErrors, int errorFlags){
        File file;
        ByteArrayOutputStream outputR = new ByteArrayOutputStream();
        Environment environment = new Environment(newScope(), outputR);
        //environment.setErrorFlags(ErrorType.E_ALL.value);

        Test test = new Test(file = new File(
                Thread.currentThread().getContextClassLoader().getResource("resources/" + name).getFile()
        ));
        Context context = new Context(test.getFile(), file);

        try {
            JvmCompiler compiler = new JvmCompiler(environment, context, getSyntax(context));
            environment.setErrorFlags(0);
            if (errorFlags != -1)
                environment.setErrorFlags(errorFlags);

            ModuleEntity module = compiler.compile(false);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ModuleDumper dumper = new ModuleDumper(context, environment, true);
            dumper.save(module, output);

            environment.setErrorFlags(ErrorType.E_ALL.value);
            module = dumper.load(new ByteArrayInputStream(output.toByteArray()));

            environment.getScope().loadModule(module);
            environment.getScope().addUserModule(module);
            environment.registerModule(module);

            Memory memory = module.includeNoThrow(environment, environment.getGlobals());
        } catch (ErrorException e) {
            if (withErrors){
                environment.getErrorReportHandler().onFatal(e);
            } else {
                throw new CustomErrorException(e.getType(), e.getMessage()
                        + " line: "
                        + (e.getTraceInfo().getStartLine() + test.getSectionLine("FILE") + 2)
                        + ", pos: " + (e.getTraceInfo().getStartPosition() + 1),
                        e.getTraceInfo());
            }
        } catch (UncaughtException e){
            environment.catchUncaught(e);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        try {
            environment.doFinal();
        } catch (ErrorException e){
            if (withErrors) {
                environment.getErrorReportHandler().onFatal(e);
                try {
                    environment.getDefaultBuffer().flush();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            } else
                throw e;
        } catch (RuntimeException e){
            throw e;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        lastOutput = outputR.toString();

        if (test.getExpect() != null)
            Assert.assertEquals(test.getTest() + " (" + name + ")", test.getExpect(), rtrim(lastOutput));

        if (test.getExpectF() != null){

            Memory result = StringFunctions.sscanf(
                    environment, TraceInfo.valueOf(file.getName(), 0, 0), rtrim(lastOutput), test.getExpectF()
            );
            if (result.isNull())
                result = new ArrayMemory();

            PrintF printF = new PrintF(environment.getLocale(), test.getExpectF(), ((ArrayMemory)result).values());
            String out = printF.toString();

            Assert.assertEquals(out, rtrim(lastOutput));
        }
    }

    protected Memory run(String code){
        return run(code, true);
    }

    protected Memory runDynamic(String code){
        return runDynamic(code, true);
    }
}
