package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\Process")
public class WrapProcess extends BaseObject {
    protected Process process;
    protected ProcessBuilder processBuilder;

    public WrapProcess(Environment env, Process process) {
        super(env);
        this.process = process;
    }

    public WrapProcess(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "commands", type = HintType.ARRAY),
            @Arg(value = "directory", optional = @Optional("NULL")),
            @Arg(value = "environment", type = HintType.ARRAY, optional = @Optional("NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        if (args[0].isNull())
            processBuilder = new ProcessBuilder();
        else
            processBuilder = new ProcessBuilder(args[0].toValue(ArrayMemory.class).toStringArray());

        if (!args[1].isNull()) {
            processBuilder.directory(FileObject.valueOf(args[1]));
        }

        if (!args[2].isNull()) {
            ForeachIterator iterator = args[2].getNewIterator(env);

            while (iterator.next()) {
                processBuilder.environment().put(iterator.getKey().toString(), iterator.getValue().toString());
            }
        }

        return Memory.NULL;
    }

    public Process getProcess() {
        if (process == null)
            throw new IllegalStateException("Process is not started, use the start() method to initialize it");

        return process;
    }

    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    @Signature
    public WrapProcess inheritIO() {
        if (processBuilder == null)
            throw new IllegalStateException("Process already started and it cannot set inheritIO().");

        processBuilder.inheritIO();
        return this;
    }

    @Signature
    public WrapProcess redirectOutputToInherit() {
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        return this;
    }

    @Signature
    public WrapProcess redirectOutputToPipe() {
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        return this;
    }

    @Signature
    public WrapProcess redirectOutputToFile(File file) {
        processBuilder.redirectOutput(file);
        return this;
    }

    @Signature
    public WrapProcess redirectErrorToInherit() {
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        return this;
    }

    @Signature
    public WrapProcess redirectErrorToPipe() {
        processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
        return this;
    }

    @Signature
    public WrapProcess redirectErrorToFile(File file) {
        processBuilder.redirectError(file);
        return this;
    }

    @Signature
    public WrapProcess redirectInputFromInherit() {
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        return this;
    }

    @Signature
    public WrapProcess redirectInputFromPipe() {
        processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
        return this;
    }

    @Signature
    public WrapProcess redirectInputFromFile(File file) {
        processBuilder.redirectInput(file);
        return this;
    }

    @Signature
    public Memory start(Environment env, Memory... args) throws IOException {
        if (processBuilder == null)
            throw new IllegalStateException("Process is final and it cannot start new ones");

        Process process1 = processBuilder.start();
        WrapProcess wrapProcess = new WrapProcess(env, process1);
        wrapProcess.processBuilder = processBuilder;

        return new ObjectMemory(wrapProcess);
    }

    @Signature
    public Memory startAndWait(Environment env, Memory... args) throws IOException, InterruptedException {
        Memory r = start(env, args);
        r.toObject(WrapProcess.class).process.waitFor();

        return r;
    }

    @Signature
    public Memory getExitValue(Environment env, Memory... args) {
        try {
            return LongMemory.valueOf(getProcess().exitValue());
        } catch (IllegalThreadStateException e) {
            return Memory.NULL;
        }
    }

    @Signature
    public Memory getInput(Environment env, Memory... args) {
        return new ObjectMemory(new MiscStream(env, getProcess().getInputStream()));
    }

    @Signature
    public Memory getOutput(Environment env, Memory... args) {
        return new ObjectMemory(new MiscStream(env, getProcess().getOutputStream()));
    }

    @Signature
    public Memory getError(Environment env, Memory... args) {
        return new ObjectMemory(new MiscStream(env, getProcess().getErrorStream()));
    }

    @Signature(@Arg(value = "force", optional = @Optional("false")))
    public void destroy(Environment env, Memory... args) throws Throwable {
        if (args[0].toBoolean()) {
            Process process = getProcess();

            Method destroyForcibly = process.getClass().getMethod("destroyForcibly");

            try {
                destroyForcibly.invoke(process);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        } else {
            getProcess().destroy();
        }
    }

    @Signature
    public boolean isAlive() {
        return getProcess().isAlive();
    }

    @Signature
    public int waitFor() throws InterruptedException {
        return getProcess().waitFor();
    }
}
