package php.runtime.ext.core.classes.stream;

import php.runtime.Memory;
import php.runtime.common.Constants;
import php.runtime.common.DigestUtils;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.lib.FsUtils;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import static php.runtime.annotation.Reflection.*;

@Name("php\\io\\File")
public class FileObject extends BaseObject {
    public final static String PATH_SEPARATOR = File.pathSeparator;
    public final static String DIRECTORY_SEPARATOR = File.separator;
    public final static boolean PATH_NAME_CASE_INSENSITIVE = Constants.PATH_NAME_CASE_INSENSITIVE;

    protected File file;

    public FileObject(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public FileObject(Environment env, File file) {
        super(env);
        this.file = file;
        if (file == null)
            throw new IllegalArgumentException();
    }

    public FileObject(Environment env, ClassEntity clazz, File file) {
        super(env, clazz);
        this.file = file;
        if (file == null)
            throw new IllegalArgumentException();
    }

    protected void exception(Environment env, String message, Object... args){
        WrapIOException exception = new WrapIOException(env, env.fetchClass("php\\io\\IOException"));
        exception.__construct(env, new StringMemory(String.format(message, args)));
        env.__throwException(exception);
    }

    public File getFile() {
        return file;
    }

    @Signature({@Arg("path"), @Arg(value = "child", optional = @Optional("NULL"))})
    public Memory __construct(Environment env, Memory... args){
        String path = args[0].toString();

        if (args[1].isNull())
            file = new File(path);
        else {
            String child = args[1].toString();
            file = new File(path, child);
        }

        return Memory.NULL;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*path").assign(file.getPath());
        return r.toConstant();
    }

    @Signature
    public Memory exists(Environment env, Memory... args){
        return file.exists() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory canExecute(Environment env, Memory... args){
        return file.canExecute() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory canRead(Environment env, Memory... args){
        return file.canRead() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory canWrite(Environment env, Memory... args){
        return file.canWrite() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return new StringMemory(file.getName());
    }

    @Signature
    public Memory getAbsolutePath(Environment env, Memory... args){
        return new StringMemory(file.getAbsolutePath());
    }

    @Signature
    public Memory getCanonicalPath(Environment env, Memory... args){
        try {
            return new StringMemory(file.getCanonicalPath());
        } catch (IOException e) {
            exception(env, e.getMessage());
            return Memory.FALSE;
        }
    }

    @Signature
    public Memory getParent(Environment env, Memory... args){
        return new StringMemory(file.getParent());
    }

    @Signature
    public Memory getPath(Environment env, Memory... args){
        return new StringMemory(file.getPath());
    }

    @Signature
    public Memory getAbsoluteFile(Environment env, Memory... args){
        FileObject fo = new FileObject(env, __class__);
        fo.file = file.getAbsoluteFile();
        return new ObjectMemory(fo);
    }

    @Signature
    public Memory getCanonicalFile(Environment env, Memory... args){
        FileObject fo = new FileObject(env, __class__);
        try {
            fo.file = file.getCanonicalFile();
        } catch (IOException e) {
            exception(env, e.getMessage());
            return Memory.NULL;
        }
        return new ObjectMemory(fo);
    }

    @Signature
    public Memory getParentFile(Environment env, Memory... args){
        if (file.getParentFile() == null)
            return Memory.NULL;

        FileObject fo = new FileObject(env, __class__);
        fo.file = file.getParentFile();
        return new ObjectMemory(fo);
    }

    @Signature
    public Memory mkdir(Environment env, Memory... args){
        return file.mkdir() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory mkdirs(Environment env, Memory... args){
        return file.mkdirs() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isFile(Environment env, Memory... args){
        return file.isFile() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDirectory(Environment env, Memory... args){
        return file.isDirectory() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAbsolute(Environment env, Memory... args){
        return file.isAbsolute() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isHidden(Environment env, Memory... args){
        return file.isHidden() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory delete(Environment env, Memory... args){
        return file.delete() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory deleteOnExit(Environment env, Memory... args){
        file.deleteOnExit();
        return Memory.NULL;
    }

    @Signature(@Arg(value = "withDirs", optional = @Optional("false")))
    public Memory createNewFile(Environment env, Memory... args){
        try {
            if (args[0].toBoolean()) {
                if (!file.getParentFile().mkdirs())
                    return Memory.FALSE;
            }

            return file.createNewFile() ? Memory.TRUE : Memory.FALSE;
        } catch (IOException e) {
            exception(env, e.getMessage());
            return Memory.FALSE;
        }
    }

    @Signature
    public Memory lastModified(Environment env, Memory... args){
        return LongMemory.valueOf(file.lastModified());
    }

    @Signature
    public Memory length(Environment env, Memory... args) {
        try {
            return LongMemory.valueOf(file.length());
        } catch (Exception e){
            return Memory.FALSE;
        }
    }

    @Signature
    public Memory crc32(Environment env, Memory... args) {
        CRC32 crcMaker = new CRC32();

        byte[] buffer = new byte[1024];
        int len;

        FileInputStream is;
        try {
            is = new FileInputStream(file);

            while ((len = is.read(buffer)) > 0) {
                crcMaker.update(buffer, 0, len);
            }

            is.close();

            return LongMemory.valueOf(crcMaker.getValue());
        } catch (FileNotFoundException e) {
            return Memory.NULL;
        } catch (IOException e) {
            return Memory.NULL;
        }
    }

    @Signature({
            @Arg(value = "algorithm", optional = @Optional("MD5")),
            @Arg(value = "progress", type = HintType.CALLABLE, optional = @Optional("null"))
    })
    public Memory hash(Environment env, Memory... args) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(args[0].toString());
        Invoker invoker = Invoker.valueOf(env, null, args[1]);

        byte[] buffer = new byte[FsUtils.BUFFER_SIZE];
        int len;

        FileInputStream is;
        try {
            is = new FileInputStream(file);

            int sum = 0;

            try {
                while ((len = is.read(buffer)) > 0) {
                    messageDigest.update(buffer, 0, len);

                    sum += len;

                    if (invoker != null) {
                        if (invoker.callAny(sum, len).toValue() == Memory.FALSE) {
                            break;
                        }
                    }
                }
            } finally {
                is.close();
            }

            return StringMemory.valueOf(DigestUtils.bytesToHex(messageDigest.digest()));
        } catch (FileNotFoundException e) {
            return Memory.NULL;
        } catch (IOException e) {
            return Memory.NULL;
        }
    }

    @Signature(@Arg("newName"))
    public Memory renameTo(Environment env, Memory... args){
        return file.renameTo(new File(args[0].toString())) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({@Arg("value"), @Arg(value = "ownerOnly", optional = @Optional(value = "1", type = HintType.BOOLEAN))})
    public Memory setExecutable(Environment env, Memory... args){
        return file.setExecutable(args[0].toBoolean(), args[1].toBoolean()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({@Arg("value"), @Arg(value = "ownerOnly", optional = @Optional(value = "1", type = HintType.BOOLEAN))})
    public Memory setReadable(Environment env, Memory... args){
        return file.setReadable(args[0].toBoolean(), args[1].toBoolean()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({@Arg("value"), @Arg(value = "ownerOnly", optional = @Optional(value = "1", type = HintType.BOOLEAN))})
    public Memory setWritable(Environment env, Memory... args){
        return file.setWritable(args[0].toBoolean(), args[1].toBoolean()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory setReadOnly(Environment env, Memory... args){
        return file.setReadOnly() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("time"))
    public Memory setLastModified(Environment env, Memory... args){
        return file.setLastModified(args[0].toLong()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("file"))
    public Memory compareTo(Environment env, Memory... args){
        File what;
        if (args[0].isObject()){
            if (args[0].instanceOf("php\\io\\File")){
                FileObject fileObject = (FileObject)args[0].toValue(ObjectMemory.class).value;
                what = fileObject.file;
            } else {
                exception(env, "Argument 1 must be an instance of %s", "php\\io\\File");
                return Memory.FALSE;
            }
        } else {
            what = new File(args[0].toString());
        }

        return LongMemory.valueOf(file.compareTo(what));
    }

    @Signature(@Arg(value = "filter", optional = @Optional("NULL")))
    public Memory find(final Environment env, Memory... args){
        if (args[0].isNull()){
            return ArrayMemory.ofStrings(file.list()).toConstant();
        } else {
            final Invoker invoker = Invoker.valueOf(env, null, args[0]);
            if (invoker == null) {
                exception(env, "Invalid filter value, must be callable");
                return Memory.NULL;
            }

            final TraceInfo trace = env.trace();
            invoker.setTrace(trace);
            String[] result = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    FileObject o = new FileObject(env, __class__, dir);
                    Memory[] args = new Memory[]{new ObjectMemory(o), new StringMemory(name)};

                    return invoker.callNoThrow(args).toBoolean();
                }
            });

            return ArrayMemory.ofStrings(result);
        }
    }

    @Signature(@Arg(value = "filter", optional = @Optional("NULL")))
    public Memory findFiles(final Environment env, Memory... args){
        File[] result;
        if (args[0].isNull()){
            result = file.listFiles();
        } else {
            final Invoker invoker = Invoker.valueOf(env, null, args[0]);
            if (invoker == null) {
                exception(env, "Invalid filter value, must be callable");
                return Memory.NULL;
            }

            final TraceInfo trace = env.trace();
            invoker.setTrace(trace);
            result = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    FileObject o = new FileObject(env, __class__, dir);
                    Memory[] args = new Memory[]{new ObjectMemory(o), new StringMemory(name)};

                    return invoker.callNoThrow(args).toBoolean();
                }
            });
        }

        ArrayMemory arr = new ArrayMemory();
        if (result != null){
            for (File e : result){
                arr.add(new ObjectMemory(new FileObject(env, __class__, e)));
            }
        }

        return arr.toConstant();
    }

    @Signature
    public Memory toUrl(Environment env, Memory... args) throws MalformedURLException {
        return StringMemory.valueOf(file.toURI().toURL().toString());
    }

    @Signature
    public Memory __toString(Environment env, Memory... args) {
        return new StringMemory(file.getPath());
    }

    @Signature({
            @Arg("prefix"),
            @Arg("suffix"),
            @Arg(value = "directory", optional = @Optional("NULL"))
    })
    public static Memory createTemp(Environment env, Memory... args) throws IOException {
        File file;
        if (args[2].isNull())
            file = File.createTempFile(args[0].toString(), args[1].toString());
        else
            file = File.createTempFile(args[0].toString(), args[1].toString(), valueOf(args[2]));

        return new ObjectMemory(new FileObject(env, file));
    }

    @Signature(@Arg("pattern"))
    public Memory matches(Environment env, Memory... args) {
        FileSystem aDefault = FileSystems.getDefault();
        PathMatcher pathMatcher = aDefault.getPathMatcher(args[0].toString());

        return pathMatcher.matches(aDefault.getPath(file.getPath())) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "mode", optional = @Optional("r")))
    public Memory open(Environment env, Memory... args) throws Throwable {
        ClassEntity classEntity = env.fetchClass(FileStream.class);
        return classEntity.newObject(env, env.trace(), true, StringMemory.valueOf(file.getPath()), args[0]);
    }

    @Signature({
            @Arg("format"),
            @Arg(value = "flags", optional = @Optional("0"))
    })
    public Memory parseAs(Environment env, Memory... args) throws Throwable {
        FileStream fileStream = open(env, StringMemory.valueOf("r")).toObject(FileStream.class);
        try {
            return fileStream.parseAs(env, args);
        } finally {
            fileStream.close(env);
        }
    }

    @Signature({
            @Arg("value"),
            @Arg("format"),
            @Arg(value = "flags", optional = @Optional("0"))
    })
    public Memory formatAs(Environment env, Memory args) throws Throwable {
        FileStream fileStream = open(env, StringMemory.valueOf("r+")).toObject(FileStream.class);
        try {
            return fileStream.writeFormatted(env, args);
        } finally {
            fileStream.close(env);
        }
    }

    @Signature
    public static Memory listRoots(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        File[] roots = File.listRoots();
        if (roots == null)
            return r.toConstant();

        for(File e : roots) {
            r.add(new FileObject(env, e));
        }

        return r.toConstant();
    }

    @Signature
    public static File of(String path) {
        return new File(path);
    }

    public static File valueOf(Memory arg) {
        if (arg.instanceOf(FileObject.class))
            return arg.toObject(FileObject.class).getFile();
        else
            return new File(arg.toString());
    }
}
