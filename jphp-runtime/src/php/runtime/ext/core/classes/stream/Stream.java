package php.runtime.ext.core.classes.stream;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.common.Modifier;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.Resource;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.support.ReflectionUtils;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.annotation.ElementType.TYPE;
import static php.runtime.annotation.Reflection.*;

@Name(Stream.CLASS_NAME)
@Signature({
        @Arg(value = "path", modifier = Modifier.PRIVATE, readOnly = true, type = HintType.STRING),
        @Arg(value = "mode", modifier = Modifier.PRIVATE, readOnly = true)
})
abstract public class Stream extends BaseObject implements Resource {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface UsePathWithProtocols {}

    @Ignore
    public final static String CLASS_NAME = "php\\io\\Stream";

    private String path;
    private String mode;
    private Memory context = Memory.NULL;

    public Stream(Environment env) {
        super(env);
    }

    public Stream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Optional("NULL"))})
    public Memory __construct(Environment env, Memory... args) throws IOException {
        setPath(args[0].toString());
        setMode(args[1].isNull() ? null : args[1].toString());

        return Memory.NULL;
    }

    @Signature({@Arg("value")})
    public Memory setContext(Environment env, Memory... args){
        context = args[0];
        return Memory.NULL;
    }

    @Signature
    public Memory getContext(Environment env, Memory... args){
        return context;
    }

    @Signature
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        __class__.setProperty(this, "path", new StringMemory(path));
        this.path = path;
    }

    @Signature
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        __class__.setProperty(this, "mode", mode == null ? null : new StringMemory(mode));
        this.mode = mode;
    }

    @Signature({@Arg("value"), @Arg(value = "length", optional = @Optional("NULL"))})
    abstract public Memory write(Environment env, Memory... args) throws IOException;

    @Signature({@Arg(value = "length")})
    abstract public Memory read(Environment env, Memory... args) throws IOException;

    @Signature
    abstract public Memory readFully(Environment env, Memory... args) throws IOException;

    @Signature
    abstract public Memory eof(Environment env, Memory... args);

    @Signature(@Arg("position"))
    abstract public Memory seek(Environment env, Memory... args) throws IOException;

    @Signature
    abstract public Memory getPosition(Environment env, Memory... args);

    @Signature
    abstract public Memory close(Environment env, Memory... args) throws IOException;

    @Signature
    public Memory __toString(Environment env, Memory... args) throws Throwable {
        Memory memory = env.invokeMethod(this, "readFully");

        if (memory.isString()) {
            return memory;
        } else {
            return StringMemory.valueOf(memory.toString());
        }
    }

    public static Stream create(Environment env, String path, String mode) throws Throwable {
        return of(env, StringMemory.valueOf(path), StringMemory.valueOf(mode)).toObject(Stream.class);
    }

    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Optional("r"))})
    public static Memory getContents(Environment env, Memory... args) throws Throwable {
        Stream stream = create(env, args[0].toString(), args[1].toString());
        try {
            return env.invokeMethod(stream, "readFully");
        } finally {
            env.invokeMethod(stream, "close");
        }
    }

    @Signature({@Arg("path"), @Arg("data"), @Arg(value = "mode", optional = @Optional("w+"))})
    public static Memory putContents(Environment env, Memory... args) throws Throwable {
        Stream stream = create(env, args[0].toString(), args[2].toString());

        FileLock lock = null;
        try {
            if (stream instanceof FileStream) {
                lock = ((FileStream) stream).getAccessFile().getChannel().lock();
            }

            try {
                return env.invokeMethod(stream, "write", args[1]);
            } finally {
                if (lock != null) {
                    lock.release();
                }
            }
        } finally {
            env.invokeMethod(stream, "close");
        }
    }

    @Signature({
            @Arg("path")
    })
    public static Memory exists(Environment env, Memory... args) throws Throwable {
        Stream stream = null;
        try {
            stream = create(env, args[0].toString(), "r");

            if (stream._isExternalResourceStream()) {
                env.exception("Unable to check external stream");
            }

            return Memory.TRUE;
        } catch (WrapIOException e) {
            return Memory.FALSE;
        } finally {
            if (stream != null) {
                env.invokeMethod(stream, "close");
            }
        }
    }

    @Signature({
            @Arg("path"),
            @Arg(value = "callback", type = HintType.CALLABLE),
            @Arg(value = "mode", optional = @Optional("r"))}
    )
    public static Memory tryAccess(Environment env, Memory... args) throws Throwable {
        Stream stream = create(env, args[0].toString(), args[2].toString());

        try {
            Invoker invoker = Invoker.create(env, args[1]);
            return invoker.call(ObjectMemory.valueOf(stream));
        } finally {
            env.invokeMethod(stream, "close");
        }
    }

    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Optional("r"))})
    public static Memory of(Environment env, Memory... args) throws Throwable {
        String path = args[0].toString();

        String protocol = "file";
        int pos = path.indexOf("://");
        if (pos > -1) {
            protocol = path.substring(0, pos);
            path = path.substring(pos + 3);
        }

        String className = env.getUserValue(Stream.class.getName() + "#" + protocol, String.class);
        if (className == null){
            throw new IOException("Unregistered protocol - " + protocol + "://");
        }

        ClassEntity classEntity = env.fetchClass(className);

        if (classEntity.getNativeClass().getAnnotation(UsePathWithProtocols.class) != null) {
            path = protocol + "://" + path;
        }

        return new ObjectMemory(
                classEntity.newObject(env, env.trace(), true, new StringMemory(path), args[1])
        );
    }

    @Signature({@Arg("protocol"), @Arg("className")})
    public static Memory register(Environment env, Memory... args) {
        String protocol = args[0].toString();
        String className = args[1].toString();

        if (protocol.isEmpty()) {
            throw new IllegalArgumentException("Argument 1 (protocol) must be not empty");
        }

        if (!protocol.matches("^[A-Za-z0-9]+$")) {
            throw new IllegalArgumentException("Invalid Argument 1 (protocol)");
        }

        ClassEntity classEntity = env.fetchClass(className, true);
        if (classEntity == null){
            throw new IllegalArgumentException(Messages.ERR_CLASS_NOT_FOUND.fetch(className));
        }

        env.setUserValue(Stream.class.getName() + "#" + protocol, classEntity.getName());
        return Memory.TRUE;
    }

    @Signature({@Arg("protocol")})
    public static Memory unregister(Environment env, Memory... args) {
        String protocol = args[0].toString();

        if (protocol.isEmpty())
            return Memory.FALSE;

        return env.removeUserValue(Stream.class.getName() + "#" + protocol) ? Memory.TRUE : Memory.FALSE;
    }

    public static void initEnvironment(Environment env) {
        registerProtocol(env, "file", FileStream.class);
        registerProtocol(env, "php", MiscStream.class);
        registerProtocol(env, "res", ResourceStream.class);
    }

    @Override
    public String getResourceType() {
        return "stream";
    }

    @Signature
    public Memory __destruct(Environment env, Memory... args) throws IOException {
        close(env, args);
        return Memory.NULL;
    }

    @Signature({
            @Arg("value"),
            @Arg(value = "format", type = HintType.STRING),
            @Arg(value = "flags", optional = @Optional("-1"))
    })
    public Memory writeFormatted(Environment env, Memory... args) throws Throwable {
        WrapProcessor processor = WrapProcessor.createByCode(env, args[1].toString(), args[2].toInteger());
        return env.invokeMethod(processor, "formatTo", args[0], ObjectMemory.valueOf(this));
    }

    @Signature({
            @Arg(value = "format", type = HintType.STRING),
            @Arg(value = "flags", optional = @Optional("-1"))
    })
    final public Memory parseAs(Environment env, Memory... args) throws Throwable {
        return readFormatted(env, args);
    }

    @Signature({
            @Arg(value = "format", type = HintType.STRING),
            @Arg(value = "flags", optional = @Optional("-1"))
    })
    public Memory readFormatted(Environment env, Memory... args) throws Throwable {
        WrapProcessor processor = WrapProcessor.createByCode(env, args[0].toString(), args[1].toInteger());
        return env.invokeMethod(processor, "parse", ObjectMemory.valueOf(this));
    }

    @Signature({
            @Arg(value = "callback", type = HintType.CALLABLE),
            @Arg(value = "encoding", optional = @Optional("null"))
    })
    public Memory eachLine(Environment env, Memory... args) throws Throwable {
        Invoker invoker = Invoker.create(env, args[0]);
        InputStream is = getInputStream(env, this);

        Scanner scanner = new Scanner(is, args[1].isNull() ? env.getDefaultCharset().name() : args[1].toString());

        int count = 0;

        while (scanner.hasNextLine()) {
            Memory call = invoker.call(StringMemory.valueOf(scanner.nextLine()));

            if (call.toBoolean()) {
                break;
            }

            count++;
        }

        return LongMemory.valueOf(count);
    }

    /**
     * Internal method.
     * @return bool
     */
    public boolean _isExternalResourceStream() {
        return false;
    }

    public static void registerProtocol(Environment env, String protocol, Class<? extends Stream> clazz) {
        env.setUserValue(Stream.class.getName() + "#" + protocol, ReflectionUtils.getClassName(clazz));
    }

    public static OutputStream getOutputStream(Environment env, Memory arg){
        try {
            if (arg.instanceOf(FileObject.class)){
                return new FileOutputStream(arg.toObject(FileObject.class).file);
            } else if (arg.instanceOf(Stream.class)){
                return new StreamOutputStream(env, arg.toObject(Stream.class));
            } else {
                StreamOutputStream outputStream = new StreamOutputStream(env, Stream.create(env, arg.toString(), "w+"));
                outputStream.autoClose = true;
                return outputStream;
            }
        } catch (IOException e){
            env.exception(WrapIOException.class, e.getMessage());
        } catch (Throwable throwable) {
            env.forwardThrow(throwable);
        }
        return null;
    }

    /**
     * @param arg
     * @return
     */
    public static String getPath(Memory arg) {
        if (arg.instanceOf(FileObject.class)){
            return arg.toObject(FileObject.class).file.getPath();
        } else if (arg.instanceOf(Stream.class)){
            return arg.toObject(Stream.class).getPath();
        } else
            return arg.toString();
    }

    public static InputStream getInputStream(Environment env, Stream stream) {
        if (stream instanceof ResourceStream) {
            return ((ResourceStream) stream).getInputStream();
        }

        return new StreamInputStream(env, stream);
    }

    public static OutputStream getOutputStream(Environment env, Stream stream) {
        return new StreamOutputStream(env, stream);
    }

    /**
     * @param arg - File path or Stream object
     * @return
     */
    public static InputStream getInputStream(Environment env, Memory arg){
        try {
            if (arg.instanceOf(FileObject.class)){
                return new FileInputStream(arg.toObject(FileObject.class).file);
            } else if (arg.instanceOf(Stream.class)){
                if (arg.instanceOf(ResourceStream.class)) {
                    return arg.toObject(ResourceStream.class).getInputStream();
                }

                return new StreamInputStream(env, arg.toObject(Stream.class));
            } else {
                StreamInputStream inputStream = new StreamInputStream(env, Stream.create(env, arg.toString(), "r"));
                inputStream.autoClose = true;
                return inputStream;
            }
        } catch (IOException e){
            env.exception(WrapIOException.class, e.getMessage());
        } catch (Throwable throwable) {
            env.forwardThrow(throwable);
        }

        return null;
    }

    public static void closeStream(Environment env, InputStream inputStream){
        if (inputStream instanceof FileInputStream
                || (inputStream instanceof StreamInputStream && ((StreamInputStream) inputStream).autoClose))
            try {
                inputStream.close();
            } catch (IOException e) {
                env.exception(WrapIOException.class, e.getMessage());
            }
    }

    public static void closeStream(Environment env, OutputStream outputStream){
        if (outputStream instanceof FileOutputStream
                || (outputStream instanceof StreamOutputStream && ((StreamOutputStream) outputStream).autoClose))
            try {
                outputStream.close();
            } catch (IOException e) {
                env.exception(WrapIOException.class, e.getMessage());
            }
    }

    public static class StreamOutputStream extends OutputStream {
        protected final Stream stream;
        protected final Environment env;
        protected boolean autoClose = false;

        public StreamOutputStream(Environment env, Stream stream) {
            this.stream = stream;
            this.env = env;
        }

        @Override
        public void write(int b) throws IOException {
            stream.write(env, new BinaryMemory((byte)b), Memory.NULL);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException();
            } else if ((off < 0) || (off > b.length) || (len < 0) ||
                    ((off + len) > b.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return;
            }

            if (off == 0 && len == b.length) {
                stream.write(env, new BinaryMemory(b), Memory.NULL);
            } else if (off == 0 && len != b.length) {
                stream.write(env, new BinaryMemory(Arrays.copyOf(b, len)), Memory.NULL);
            } else {
                stream.write(env, new BinaryMemory(Arrays.copyOfRange(b, off, off + len)), Memory.NULL);
            }
        }

        @Override
        public void close() throws IOException {
            super.close();
            stream.close(env);
        }
    }

    public static class StreamInputStream extends InputStream {
        protected final Stream stream;
        protected final Environment env;
        protected boolean autoClose = false;

        public StreamInputStream(Environment env, Stream stream) {
            this.stream = stream;
            this.env = env;
        }

        @Override
        public int read() throws IOException {
            Memory result = stream.read(env, Memory.CONST_INT_1);
            return result.isString() ? result.getBinaryBytes(env.getDefaultCharset())[0] & 0xFF : -1;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            Memory result = stream.read(env, LongMemory.valueOf(len));
            if (!result.isString())
                return -1;

            byte[] copy = result.getBinaryBytes(env.getDefaultCharset());

            System.arraycopy(copy, 0, b, off, copy.length);

            return copy.length;
        }

        @Override
        public void close() throws IOException {
            super.close();
            stream.close(env);
        }
    }
}
