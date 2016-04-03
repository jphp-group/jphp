package php.runtime.ext.core.classes.lib;

import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Runtime.FastMethod;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Name("php\\lib\\fs")
public class FsUtils extends BaseObject {
    private static final int BUFFER_SIZE = 8192;
    private final static char CHAR_UNDEFINED = 0xFFFF;

    private final static Set<String> winSystemNames = new HashSet<>(Arrays.asList("CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"));

    public FsUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    private static boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                c != CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    @Signature
    public static String abs(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            return new File(path).getAbsolutePath();
        }
    }

    @Signature
    public static boolean valid(String name) {
        if (name.isEmpty()) {
            return false;
        }

        if (name.indexOf('*') > -1 || name.indexOf('?') > -1 || name.indexOf('"') > -1) {
            return false;
        }

        if (name.contains("..")) {
            return false;
        }

        if (name.equals(".")) {
            return false;
        }

        int length = name.length();

        for (int i = 0; i < length; i++) {
            char ch = name.charAt(i);

            if (!isPrintableChar(ch)) {
                return false;
            }
        }

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (winSystemNames.contains(name.toUpperCase())) {
                return false;
            }
        }

        return true;
    }

    @Signature
    public static String name(String path) {
        return new File(path).getName();
    }

    @Signature
    public static String ext(String path) {
        String name = new File(path).getName();

        int indexOf = name.lastIndexOf('.');

        if (indexOf > -1) {
            return name.substring(indexOf + 1);
        } else {
            return null;
        }
    }

    @Signature
    public static boolean hasExt(Environment env, String path) {
        return ext(path) != null;
    }

    @Signature
    public static boolean hasExt(Environment env, String path, Memory extensions) {
        return hasExt(env, path, extensions, true);
    }

    @Signature
    public static boolean hasExt(Environment env, String path, Memory extensions, boolean ignoreCase) {
        Set<String> exts = new HashSet<>();

        if (extensions.isTraversable()) {
            ForeachIterator iterator = extensions.getNewIterator(env);

            while (iterator.next()) {
                String value = iterator.getValue().toString();

                if (ignoreCase) {
                    value = value.toLowerCase();
                }

                exts.add(value);
            }
        } else {
            exts.add(ignoreCase ? extensions.toString().toLowerCase() : extensions.toString());
        }

        String ext = ext(path);

        if (ignoreCase && ext != null) {
            ext = ext.toLowerCase();
        }

        return exts.contains(ext);
    }

    @Signature
    public static String nameNoExt(String path) {
        String name = new File(path).getName();

        int indexOf = name.lastIndexOf('.');

        if (indexOf > -1) {
            name = name.substring(0, indexOf);
        }

        return name;
    }

    @Signature
    public static String pathNoExt(String path) {
        String name = new File(path).getPath();

        int indexOf = name.lastIndexOf('.');

        if (indexOf > -1) {
            name = name.substring(0, indexOf);
        }

        return name;
    }

    @Signature
    public static long size(String path) {
        return new File(path).length();
    }

    @Signature
    public static long time(String path) {
        return new File(path).lastModified();
    }

    @Signature
    public static boolean isFile(String path) {
        return new File(path).isFile();
    }

    @Signature
    public static boolean isDir(String path) {
        return new File(path).isDirectory();
    }

    @Signature
    public static boolean isHidden(String path) {
        return new File(path).isHidden();
    }

    @Signature
    public static String normalize(String path) {
        return new File(path).getPath();
    }

    @Signature
    public static String parent(String path) {
        return new File(path).getParent();
    }

    @Signature
    public static boolean ensureParent(String path) {
        File parent = new File(path).getParentFile();

        if (parent == null) {
            return true;
        }

        if (!parent.isDirectory()) {
            return parent.mkdirs();
        }

        return true;
    }

    @Signature
    public static boolean exists(String path) {
        return new File(path).exists();
    }

    @Signature
    @FastMethod
    public static Memory separator(Environment env, Memory... args) {
        return StringMemory.valueOf(File.separator);
    }

    @Signature
    @FastMethod
    public static Memory pathSeparator(Environment env, Memory... args) {
        return StringMemory.valueOf(File.pathSeparator);
    }

    @Signature
    public static boolean makeDir(String path) {
        return new File(path).mkdirs();
    }

    @Signature
    public static boolean makeFile(String path) {
        String parent = parent(path);

        if (parent != null && !isDir(path)) {
            if (!makeDir(parent)) {
                return false;
            }
        }

        try {
            return new File(path).createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    @Signature
    public static boolean delete(String path) {
        return new File(path).delete();
    }

    @Signature
    public static Memory get(Environment env, String input) throws Throwable {
        return get(env, input, null, "r");
    }

    @Signature
    public static Memory get(Environment env, String input, @Nullable String charset) throws Throwable {
        return get(env, input, charset, "r");
    }

    @Signature
    public static Memory get(Environment env, String input, @Nullable String charset, String mode) throws Throwable {
        Stream stream = Stream.create(env, input, mode);

        try {
            Memory memory = env.invokeMethod(stream, "readFully");
            if (charset == null || charset.trim().isEmpty()) {
                return memory;
            } else {
                return StrUtils.decode(env, memory, StringMemory.valueOf(charset));
            }
        } finally {
            env.invokeMethod(stream, "close");
        }
    }

    @Signature
    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, null);
    }

    @Signature
    public static long copy(InputStream input, OutputStream output, @Nullable Invoker callback) throws IOException {
        long nread = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        int n;

        while ((n = input.read(buf)) > 0) {
            output.write(buf, 0, n);
            nread += n;

            if (callback != null) {
                if (callback.callAny(nread).toValue() == Memory.FALSE) {
                    break;
                }
            }
        }

        return nread;
    }

    @Signature
    public static Memory hash(InputStream source) throws NoSuchAlgorithmException {
        return hash(source, "MD5");
    }

    @Signature
    public static Memory hash(InputStream source, String algo) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algo);

        byte[] buffer = new byte[1024];
        int len;

        try {
            while ((len = source.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, len);
            }

            return StringMemory.valueOf(String.format("%064x", new java.math.BigInteger(1, messageDigest.digest())));
        } catch (FileNotFoundException e) {
            return Memory.NULL;
        } catch (IOException e) {
            return Memory.NULL;
        }
    }

    public static void scan(String path, ScanProgressHandler scanProgressHandler)  {
        scan(path, scanProgressHandler, 0);
    }

    public static void scan(String path, final ScanProgressHandler scanProgressHandler, final int maxDepth)  {
        scan(path, scanProgressHandler, maxDepth, 1);
    }

    public static void scan(String path, final ScanProgressHandler scanProgressHandler, final int maxDepth, final int _depth)  {
        if (maxDepth > 0 && _depth > maxDepth) {
            return;
        }

        File file = new File(path);

        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!scanProgressHandler.isSubIsFirst()) {
                    scanProgressHandler.call(pathname, _depth);
                }

                if (pathname.isDirectory()) {
                    scan(pathname.getPath(), scanProgressHandler, maxDepth, _depth + 1);
                }

                if (scanProgressHandler.isSubIsFirst()) {
                    scanProgressHandler.call(pathname, _depth);
                }

                return false;
            }
        });
    }

    @Signature
    public static void scan(String path, final Invoker progress)  {
        scan(path, progress, 0);
    }

    @Signature
    public static void scan(String path, final Invoker progress, int maxDepth)  {
        scan(path, progress, maxDepth, false);
    }

    @Signature
    public static void scan(String path, final Invoker progress, int maxDepth, boolean filesIsFirst)  {
        scan(path, new ScanProgressHandler(filesIsFirst) {
            @Override
            public boolean call(File pathname, int depth) {
                progress.callAny(pathname, depth);
                return true;
            }
        }, maxDepth);
    }

    @Signature
    public ArrayMemory clean(final Environment env, String path) {
        return clean(env, path, null);
    }

    @Signature
    public ArrayMemory clean(final Environment env, String path, @Nullable final Invoker checker) {
        ArrayMemory result = new ArrayMemory();
        final ArrayMemory success = new ArrayMemory();
        final ArrayMemory error = new ArrayMemory();
        final ArrayMemory skip = new ArrayMemory();

        result.put("success", success);
        result.put("error", error);
        result.put("skip", error);

        scan(path, new ScanProgressHandler(true) {
            @Override
            public boolean call(File file, int depth) {
                Memory value = ObjectMemory.valueOf(new FileObject(env, file));

                if (checker == null || checker.callAny(file, depth).toBoolean()) {
                    if (delete(file.getPath())) {
                        success.add(value);
                    } else {
                        error.add(value);
                    }
                } else {
                    skip.add(value);
                }

                return true;
            }
        });

        return result.toConstant();
    }

    public abstract static class ScanProgressHandler {
        protected final boolean subIsFirst;

        protected ScanProgressHandler(boolean subIsFirst) {
            this.subIsFirst = subIsFirst;
        }

        public boolean isSubIsFirst() {
            return subIsFirst;
        }

        abstract public boolean call(File file, int depth);
    }
}
