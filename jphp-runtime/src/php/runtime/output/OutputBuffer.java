package php.runtime.output;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class OutputBuffer {
    public final static int HANDLER_START = 1;
    public final static int HANDLER_WRITE = 0;
    public final static int HANDLER_FLUSH = 4;
    public final static int HANDLER_CLEAN = 2;
    public final static int HANDLER_FINAL = 8;

    public enum Type { INTERNAL, USER }

    private OutputStream output;
    private ByteArrayOutputStream buffer;
    private boolean binaryInBuffer;

    private Memory callback;
    private Invoker invoker;

    private int chunkSize;
    private boolean erase;

    private final Environment environment;
    private final OutputBuffer parentOutput;

    private int level;
    private Type type;
    private String name = "default output handler";

    private boolean implicitFlush;

    private int lock = 0;
    private boolean isFlushed = false;
    private TraceInfo trace;

    private int status;

    public OutputBuffer(Environment environment, OutputBuffer parent, Memory callback, int chunkSize, boolean erase) {
        this.environment = environment;
        this.buffer = new ByteArrayOutputStream(4098);

        this.callback = callback;
        if (callback != null)
            this.invoker = Invoker.valueOf(environment, null, callback);

        this.chunkSize = chunkSize;
        this.erase = erase;
        this.parentOutput = parent;

        this.type = Type.INTERNAL;
        this.implicitFlush = false;
        this.status = HANDLER_START;
    }

    public OutputBuffer(Environment environment, OutputBuffer parent, Memory callback, int chunkSize) {
        this(environment, parent, callback, chunkSize, true);
    }

    public OutputBuffer(Environment environment, OutputBuffer parent, Memory callback) {
        this(environment, parent, callback, 4096);
    }

    public OutputBuffer(Environment environment, OutputBuffer parent) {
        this(environment, parent, null);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return invoker != null ? invoker.getName() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setImplicitFlush(boolean implicitFlush) {
        this.implicitFlush = implicitFlush;
    }

    public boolean isImplicitFlush() {
        return implicitFlush;
    }

    public Memory getCallback() {
        return callback;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public OutputStream getOutput() {
        return output;
    }


    public void setCallback(Memory callback, Invoker invoker) {
        this.callback = callback;
        this.invoker = invoker;
    }

    public void setCallback(Memory callback) {
        this.callback = callback;

        if (callback != null)
            this.invoker = Invoker.valueOf(environment, null, callback);
        else
            this.invoker = null;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public boolean isErase() {
        return erase;
    }

    public void setErase(boolean erase) {
        this.erase = erase;
    }

    public Memory getContents() throws UnsupportedEncodingException {
        if (!binaryInBuffer){
            return new StringMemory(buffer.toString(environment.getDefaultCharset().name()));
        } else
            return new BinaryMemory(buffer.toByteArray());
    }

    protected void reset(){
        buffer.reset();
        binaryInBuffer = false;
    }

    public void clean() throws Throwable {
        if (invoker != null){
            doFlush(false);
        }
        status = HANDLER_CLEAN;
        reset();
    }

    protected void doFlush(boolean flush) throws Throwable {
        /*if (buffer.size() == 0)
            return;*/
        if (flush){
            isFlushed = true;
            if (status != HANDLER_START && status != HANDLER_FLUSH)
                status = HANDLER_WRITE;
        } else {
            if (status == HANDLER_FINAL)
                status |= HANDLER_CLEAN;
            else
                status = HANDLER_CLEAN;
        }

        if (invoker != null){
            invoker.setTrace(trace == null ? TraceInfo.UNKNOWN : trace);
            Memory[] args;
            if (!binaryInBuffer){
                args = new Memory[]{ new StringMemory(buffer.toString(environment.getDefaultCharset().name())), null };
            } else
                args = new Memory[]{ new BinaryMemory(buffer.toByteArray()), null };

            args[1] = LongMemory.valueOf(status);

            invoker.setTrace(trace == null ? TraceInfo.UNKNOWN : trace);
            Memory result;
            incLock();
            try {
               result = invoker.call(args).toValue();
            } finally {
                decLock();
            }
            if (result != Memory.FALSE){
                byte[] data = result instanceof BinaryMemory
                        ? result.getBinaryBytes(environment.getDefaultCharset()) : result.toString().getBytes(environment.getDefaultCharset());
                if (flush){
                    if (output == null) {
                        parentOutput.write(data);
                    } else {
                        output.write(data);
                    }

                    reset();
                    status = HANDLER_FLUSH;
                }

                return;
            }
        }

        if (flush){
            status |= HANDLER_FLUSH;
            if (output == null) {
                parentOutput.write(buffer.toByteArray());
            } else {
                buffer.writeTo(output);
            }

            reset();
        }
    }

    public void write(String content) throws Throwable {
        if (!isLock())
            _write(content.getBytes(environment.getDefaultCharset()));
    }

    public void write(Memory content) throws Throwable {
        if (!isLock()){
            content = content.toValue();
            if (content instanceof BinaryMemory)
                write(content.getBinaryBytes(environment.getDefaultCharset()));
            else
                write(content.toString());
        }
    }

    protected void _write(byte[] bytes) throws Throwable {
        _write(bytes, bytes.length);
    }

    protected void _write(byte[] bytes, int length) throws Throwable {
        if (isLock()) return;

        boolean needFlush = implicitFlush || (chunkSize > 0 && length + buffer.size() >= chunkSize);

        if (needFlush){
            if (chunkSize > 0){
                int cutLength = (length + buffer.size()) - chunkSize;
                if (cutLength > 0){
                    buffer.write(bytes, 0, length);
                    doFlush(true);

                    /*byte[] tmp = new byte[cutLength];
                    System.arraycopy(bytes, bytes.length - cutLength, tmp, 0, cutLength);
                    _write(tmp);*/
                } else {
                    buffer.write(bytes, 0, length);
                    doFlush(true);
                }
            } else {
                buffer.write(bytes, 0, length);
                doFlush(true);
            }
        } else {
            buffer.write(bytes, 0, length);
        }
    }

    public void write(byte[] bytes, int length) throws Throwable {
        if (!isLock()){
            binaryInBuffer = true;
            _write(bytes, length);
        }
    }

    public void write(byte[] bytes) throws Throwable {
        write(bytes, bytes.length);
    }

    public void flush() throws Throwable {
        if (!isLock()){
            status = HANDLER_FLUSH;
            doFlush(true);
            if (output != null)
                output.flush();
        }
    }

    public void close() throws Throwable {
        if (!isBufferEmpty())
            doFlush(true);
    }

    public int getBufferSize(){
        return buffer.size();
    }

    public boolean isRoot(){
        return level == 0;
    }

    public void decLock(){
        if (isRoot())
            lock--;
        else
            environment.getDefaultBuffer().decLock();
    }

    public void incLock(){
        if (isRoot())
            lock++;
        else
            environment.getDefaultBuffer().incLock();
    }

    public boolean isLock(){
        return isRoot() ? lock > 0 : environment.getDefaultBuffer().isLock();
    }

    public boolean isFlushed() {
        return isFlushed;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public OutputBuffer getParentOutput() {
        return parentOutput;
    }

    public void setTrace(TraceInfo trace) {
        this.trace = trace;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isBufferEmpty(){
        return buffer.size() == 0;
    }
}
