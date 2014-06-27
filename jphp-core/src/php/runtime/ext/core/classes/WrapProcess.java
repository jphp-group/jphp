package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;

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
            @Arg(value = "directory", optional = @Optional("NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        if (args[0].isNull())
            processBuilder = new ProcessBuilder();
        else
            processBuilder = new ProcessBuilder(args[0].toValue(ArrayMemory.class).toStringArray());

        if (!args[1].isNull())
            processBuilder.directory(FileObject.valueOf(args[1]));

        return Memory.NULL;
    }

    protected Process getProcess() {
        if (process == null)
            throw new IllegalStateException("Process is not started, use the start() method to initialize it");

        return process;
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
        return LongMemory.valueOf(getProcess().exitValue());
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

    @Signature
    public Memory destroy(Environment env, Memory... args) {
        getProcess().destroy();
        return Memory.NULL;
    }
}
