package php.runtime.ext.core;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.output.OutputBuffer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class OutputFunctions extends FunctionsContainer {

    public static Memory print(Environment environment, Memory memory){
        environment.echo(memory);
        return Memory.CONST_INT_1;
    }

    public static void flush(Environment env) throws Throwable {
        OutputBuffer root = env.getDefaultBuffer();
        if (root != null) {
            root.flush();
        }
    }

    public static Memory ob_start(Environment env, TraceInfo trace, Memory outputCallback, Memory _chunkSize,
                                  Memory erase){
        Invoker invoker;
        if (!outputCallback.isNull()){
            invoker = expectingCallback(env, trace, 1, outputCallback);
            if (invoker == null)
                return Memory.FALSE;
            invoker.check("ob_start", trace);
        }

        switch (_chunkSize.getRealType()){
            case ARRAY:case STRING:case OBJECT:
                env.warning(trace,
                        "ob_start() expects parameter 2 to be long, "+_chunkSize.getRealType().toString()+" given");
                return Memory.NULL;
        }

        switch (erase.getRealType()){
            case ARRAY:case STRING:case OBJECT:
                env.warning(trace,
                        "ob_start() expects parameter 3 to be long, "+erase.getRealType().toString()+" given");
                return Memory.NULL;
        }

        int chunkSize = _chunkSize.toInteger();
        if (chunkSize < 0)
            chunkSize = 0;

        if (chunkSize < 0){
            env.warning(trace, "ob_start(): chunk_size must be grater or equal than zero");
            return Memory.FALSE;
        }

        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null && buffer.isLock())
            env.error(trace, "ob_start(): Cannot use output buffering in output buffering display handlers");

        env.pushOutputBuffer(outputCallback, chunkSize, erase.toBoolean());
        return Memory.TRUE;
    }

    public static Memory ob_start(Environment env, TraceInfo trace, Memory outputCallback, Memory chunkSize){
        return ob_start(env, trace, outputCallback, chunkSize, Memory.TRUE);
    }

    public static Memory ob_start(Environment env, TraceInfo trace, Memory outputCallback){
        return ob_start(env, trace, outputCallback, Memory.CONST_INT_0, Memory.TRUE);
    }

    public static Memory ob_start(Environment env, TraceInfo trace){
        return ob_start(env, trace, Memory.NULL, Memory.CONST_INT_0, Memory.TRUE);
    }

    public static boolean ob_clean(Environment env, TraceInfo trace) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null && !buffer.isRoot()){
            buffer.setTrace(trace);
            buffer.clean();
            return true;
        } else {
            env.error(trace, ErrorType.E_NOTICE, "ob_clean(): failed to delete buffer. No buffer to delete");
            return false;
        }
    }

    public static boolean ob_end_clean(Environment env, TraceInfo trace) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null && !buffer.isRoot()){
            buffer.setTrace(trace);
            buffer.setStatus(OutputBuffer.HANDLER_FINAL);
            buffer.clean();
            env.popOutputBuffer();
            return true;
        } else {
            env.error(trace, ErrorType.E_NOTICE, "ob_end_clean(): failed to delete buffer. No buffer to delete");
            return false;
        }
    }

    public static boolean ob_end_flush(Environment env, TraceInfo trace) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null && !buffer.isRoot()){
            buffer.setTrace(trace);
            buffer.flush();
            env.popOutputBuffer();
            return true;
        } else {
            env.error(trace, ErrorType.E_NOTICE, "ob_end_flush(): failed to delete and flush buffer. No buffer to delete or flush");
            return false;
        }
    }

    public static Memory ob_get_contents(Environment env, TraceInfo trace) throws UnsupportedEncodingException {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer == null || buffer.isRoot())
            return Memory.FALSE;

        return buffer.getContents();
    }

    public static boolean ob_flush(Environment env, TraceInfo trace) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null && !buffer.isRoot()) {
            buffer.setTrace(trace);
            buffer.flush();
            return true;
        } else {
            env.error(trace, ErrorType.E_NOTICE, "ob_flush(): failed to flush buffer. No buffer to flush");
            return false;
        }
    }

    public static Memory ob_get_clean(Environment env, TraceInfo trace) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null && !buffer.isRoot()){
            Memory result = buffer.getContents();
            buffer.setTrace(trace);
            buffer.clean();
            return result;
        } else {
            //env.error(trace, ErrorType.E_NOTICE, "ob_get_clean(): failed to delete and clean buffer. No buffer to delete or clean");
            return Memory.FALSE;
        }
    }

    public static Memory ob_get_flush(Environment env, TraceInfo trace) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null){
            if (buffer.isLock()){
                env.error(trace, "ob_get_flush(): Cannot use output buffering in output buffering display handlers");
                return Memory.FALSE;
            }

            Memory result = buffer.getContents();
            buffer.setTrace(trace);
            buffer.flush();
            return result;
        } else {
            env.error(trace, ErrorType.E_NOTICE, "ob_get_flush(): failed to delete and flush buffer. No buffer to delete or flush");
            return Memory.FALSE;
        }
    }

    public static Memory ob_get_length(Environment env) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null && !buffer.isRoot()){
            return LongMemory.valueOf(buffer.getBufferSize());
        } else
            return Memory.FALSE;
    }

    public static Memory ob_get_level(Environment env) throws Throwable {
        OutputBuffer buffer = env.peekOutputBuffer();
        if (buffer != null){
            return LongMemory.valueOf(buffer.getLevel());
        } else
            return Memory.FALSE;
    }

    public static void ob_implicit_flush(Environment env, TraceInfo trace, Memory value){
        OutputBuffer root = env.getDefaultBuffer();
        switch (value.getRealType()){
            case ARRAY:
            case STRING:
            case OBJECT:
                env.warning(trace,
                        "ob_implicit_flush() expects parameter 1 to be long, " + value.getRealType().toString() + " given"
                );
                return;
        }
        if (root != null)
            root.setImplicitFlush(value.toBoolean());
    }

    public static void ob_implicit_flush(Environment env, TraceInfo trace){
        ob_implicit_flush(env, trace, Memory.TRUE);
    }

    private static ArrayMemory _get_status(OutputBuffer buffer){
        ArrayMemory result = new ArrayMemory();
        result.refOfIndex("name").assign(buffer.getName());
        result.refOfIndex("type").assign(buffer.getInvoker() == null ? 0 : 1);
        result.refOfIndex("flags").assign(buffer.getStatus());
        result.refOfIndex("level").assign(buffer.getLevel() - 1);
        result.refOfIndex("chunk_size").assign(buffer.getChunkSize());
        result.refOfIndex("buffer_used").assign(buffer.getBufferSize());
        return result;
    }

    public static Memory ob_get_status(Environment env, boolean fullStatus){
        if (fullStatus){
            ArrayMemory result = new ArrayMemory();
            OutputBuffer peek = env.peekOutputBuffer();

            ArrayList<OutputBuffer> list = new ArrayList<OutputBuffer>();
            while (peek != null && !peek.isRoot()){
                list.add(0, peek);
                peek = peek.getParentOutput();
            }

            for(OutputBuffer e : list){
                result.add(_get_status(e));
            }

            return result;
        } else {
            OutputBuffer buffer = env.peekOutputBuffer();
            if (buffer == null || buffer.isRoot())
                return new ArrayMemory().toConstant();

            return _get_status(buffer).toConstant();
        }
    }

    public static Memory ob_list_handlers(Environment env){
        ArrayMemory result = new ArrayMemory();
        OutputBuffer peek = env.peekOutputBuffer();

        ArrayList<OutputBuffer> list = new ArrayList<OutputBuffer>();
        while (peek != null && !peek.isRoot()){
            list.add(0, peek);
            peek = peek.getParentOutput();
        }

        for(OutputBuffer e : list){
            result.refOfPush().assign(e.getName());
        }

        return result.toConstant();
    }

    public static Memory ob_get_status(Environment env){
        return ob_get_status(env, false);
    }
}
