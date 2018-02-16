package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Runtime.FastMethod;
import php.runtime.common.Callback;
import php.runtime.common.DigestUtils;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.util.WrapRegex;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.RunnableInvoker;
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
    public static final int BUFFER_SIZE = 8192;
    private final static char CHAR_UNDEFINED = 0xFFFF;

    private final static Set<String> winSystemNames = new HashSet<>(Arrays.asList("CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"));

    private final static char[] deniedNameChars = {'*', '?', '"', '>', '<', '|', ':', '/', '\\'};

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
        if (name.trim().isEmpty()) {
            return false;
        }

        for (char c : deniedNameChars) {
            if (name.indexOf(c) > -1) {
                return false;
            }
        }

        if (name.equals(".") || name.startsWith("./") || name.startsWith(".\\")) {
            return false;
        }

        int length = name.length();
        boolean onlySpecs = true;

        for (int i = 0; i < length; i++) {
            char ch = name.charAt(i);

            if (ch != '.' && ch != '/' && ch != '\\') {
                onlySpecs = false;
            }

            if (!isPrintableChar(ch)) {
                return false;
            }
        }

        if (onlySpecs) {
            return false;
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
    public static Memory parse(Environment env, String input) throws Throwable {
        return parseAs(env, input, ext(input), -1);
    }

    @Signature
    public static Memory parse(Environment env, String input, int flags) throws Throwable {
        return parseAs(env, input, ext(input), flags);
    }

    @Signature
    public static Memory parseAs(Environment env, String input, String format) throws Throwable {
        return parseAs(env, input, format, -1);
    }

    @Signature
    public static Memory parseAs(Environment env, String input, String format, int flags) throws Throwable {
        Stream stream = Stream.create(env, input, "r");
        try {
            return stream.parseAs(env, StringMemory.valueOf(format), LongMemory.valueOf(flags));
        } finally {
            env.invokeMethod(stream, "close");
        }
    }

    @Signature
    public static Memory format(Environment env, String input, Memory value) throws Throwable {
        return format(env, input, value, -1);
    }

    @Signature
    public static Memory format(Environment env, String input, Memory value, int flags) throws Throwable {
        return formatAs(env, input, value, ext(input), flags);
    }

    @Signature
    public static Memory formatAs(Environment env, String input, Memory value, String format) throws Throwable {
        return formatAs(env, input, value, format, -1);
    }

    @Signature
    public static Memory formatAs(Environment env, String input, Memory value, String format, int flags) throws Throwable {
        Stream stream = Stream.create(env, input, "w");
        try {
            return stream.writeFormatted(env, value, StringMemory.valueOf(format), LongMemory.valueOf(flags));
        } finally {
            env.invokeMethod(stream, "close");
        }
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
        return copy(input, output, null, BUFFER_SIZE);
    }

    @Signature
    public static long copy(InputStream input, OutputStream output, @Nullable Invoker callback) throws IOException {
        return copy(input, output, callback, BUFFER_SIZE);
    }

    @Signature
    public static long copy(InputStream input, OutputStream output, @Nullable Invoker callback, int bufferSize) throws IOException {
        long nread = 0L;
        byte[] buf = new byte[bufferSize];
        int n;

        BufferedInputStream inputStream = new BufferedInputStream(input, bufferSize);
        BufferedOutputStream outputStream = new BufferedOutputStream(output, bufferSize);

        while ((n = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, n);
            nread += n;

            if (callback != null) {
                if (callback.callAny(nread, n).toValue() == Memory.FALSE) {
                    break;
                }
            }
        }

        outputStream.flush();

        return nread;
    }

    @Signature
    public static Memory hash(InputStream source) throws NoSuchAlgorithmException {
        return hash(source, "MD5", null);
    }

    @Signature
    public static Memory hash(InputStream source, String algo) throws NoSuchAlgorithmException {
        return hash(source, algo, null);
    }

    @Signature
    public static Memory hash(InputStream source, String algo, @Nullable Invoker invoker) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algo);

        byte[] buffer = new byte[BUFFER_SIZE];
        int len;

        try {
            int sum = 0;

            while ((len = source.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, len);

                sum += len;

                if (invoker != null) {
                    if (invoker.callAny(sum, len).toValue() == Memory.FALSE) {
                        break;
                    }
                }
            }

            return StringMemory.valueOf(DigestUtils.bytesToHex(messageDigest.digest()));
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

    public static class FilterProperties {
        private Memory namePattern;
        private Set<String> extensions;
        private Set<String> excludeExtensions;

        private boolean excludeDirs;
        private boolean excludeFiles;
        private boolean excludeHidden;

        private Invoker callback;

        private long minSize = 0L;
        private long maxSize = Long.MAX_VALUE;
        private long minTime = 0L;
        private long maxTime = Long.MAX_VALUE;

        public Set<String> getExtensions() {
            return extensions;
        }

        public void setExtensions(Set<String> extensions) {
            this.extensions = extensions;
        }

        public Set<String> getExcludeExtensions() {
            return excludeExtensions;
        }

        public void setExcludeExtensions(Set<String> excludeExtensions) {
            this.excludeExtensions = excludeExtensions;
        }

        public boolean isExcludeDirs() {
            return excludeDirs;
        }

        public void setExcludeDirs(boolean excludeDirs) {
            this.excludeDirs = excludeDirs;
        }

        public boolean isExcludeFiles() {
            return excludeFiles;
        }

        public void setExcludeFiles(boolean excludeFiles) {
            this.excludeFiles = excludeFiles;
        }

        public boolean isExcludeHidden() {
            return excludeHidden;
        }

        public void setExcludeHidden(boolean excludeHidden) {
            this.excludeHidden = excludeHidden;
        }

        public Invoker getCallback() {
            return callback;
        }

        public void setCallback(Invoker callback) {
            this.callback = callback;
        }

        public long getMinSize() {
            return minSize;
        }

        public void setMinSize(long minSize) {
            this.minSize = minSize;
        }

        public long getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(long maxSize) {
            this.maxSize = maxSize;
        }

        public long getMinTime() {
            return minTime;
        }

        public void setMinTime(long minTime) {
            this.minTime = minTime;
        }

        public long getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(long maxTime) {
            this.maxTime = maxTime;
        }

        public Memory getNamePattern() {
            return namePattern;
        }

        public void setNamePattern(Memory namePattern) {
            this.namePattern = namePattern;
        }
    }

    //@Signature
    private static Memory scanFilter(final Environment env, final ArrayMemory props) {
        final FilterProperties filterProperties = props.toBean(env, FilterProperties.class);

        return RunnableInvoker.make(env, new Callback<Memory, Memory[]>() {
            @Override
            public Memory call(Memory[] args) {
                if (args == null || args.length == 0) {
                    return Memory.NULL;
                }

                Memory file = args[0];
                File path;

                if (file.instanceOf(FileObject.class)) {
                    path = file.toObject(FileObject.class).getFile();
                } else {
                    path = new File(file.toString());
                }

                Set<String> extensions = filterProperties.extensions;
                Set<String> excludeExtensions = filterProperties.excludeExtensions;

                if (filterProperties.excludeDirs && path.isDirectory()) {
                    return Memory.NULL;
                }

                if (filterProperties.excludeFiles && path.isFile()) {
                    return Memory.NULL;
                }

                if (filterProperties.excludeHidden && path.isHidden()) {
                    return Memory.NULL;
                }

                if (filterProperties.minSize > 0 && path.length() < filterProperties.minSize) {
                    return Memory.NULL;
                }

                if (filterProperties.maxSize != Long.MAX_VALUE && path.length() > filterProperties.maxSize) {
                    return Memory.NULL;
                }

                if (filterProperties.minTime > 0 && path.lastModified() < filterProperties.minTime) {
                    return Memory.NULL;
                }

                if (filterProperties.maxTime != Long.MAX_VALUE && path.lastModified() > filterProperties.maxTime) {
                    return Memory.NULL;
                }

                String ext = ext(path.getPath());

                if (extensions != null && extensions.size() > 0 && !extensions.contains(ext)) {
                    return Memory.NULL;
                }

                if (excludeExtensions != null && excludeExtensions.size() > 0 && excludeExtensions.contains(ext)) {
                    return Memory.NULL;
                }

                if (filterProperties.namePattern != null && filterProperties.namePattern.isNotNull()) {
                    boolean matches;

                    if (filterProperties.namePattern.instanceOf(WrapRegex.class)) {
                        WrapRegex regex = filterProperties.namePattern.toObject(WrapRegex.class);
                        matches = regex.test(env, StringMemory.valueOf(path.getName())).toBoolean();
                    } else {
                        matches = path.getName().matches(filterProperties.namePattern.toString());
                    }

                    if (!matches) return Memory.NULL;
                }

                if (filterProperties.getCallback() != null) {
                    Memory ret = filterProperties.getCallback().callNoThrow(args);

                    if (ret.isNotNull() && ret.toValue() != Memory.FALSE) {
                        return ret.toImmutable();
                    } else {
                        return Memory.NULL;
                    }
                }

                return file;
            }
        });
    }

    @Signature
    public static Memory scan(Environment env, String path)  {
        return scan(env, path, null, 0);
    }

    @Signature
    public static Memory scan(Environment env, String path, @Nullable final Memory progress)  {
        return scan(env, path, progress, 0);
    }

    @Signature
    public static Memory scan(Environment env, String path, @Nullable final Memory progress, int maxDepth)  {
        return scan(env, path, progress, maxDepth, false);
    }

    @Signature
    public static Memory scan(final Environment env, String path,
                              @Nullable Memory filter, int maxDepth, boolean filesIsFirst)  {
        final ArrayMemory result = new ArrayMemory();

        if (filter.isArray()) {
            filter = scanFilter(env, filter.toValue(ArrayMemory.class));
        }

        final Invoker progress = Invoker.create(env, filter);

        scan(path, new ScanProgressHandler(filesIsFirst) {
            @Override
            public boolean call(File pathname, int depth) {
                if (progress != null) {
                    Memory ret = progress.callAny(pathname, depth);

                    if (ret.isNotNull() && ret.toValue() != Memory.FALSE) {
                        result.add(ret.toImmutable());
                    }
                } else {
                    result.add(new FileObject(env, pathname));
                }

                return true;
            }
        }, maxDepth);

        return result;
    }

    @Signature
    public static ArrayMemory clean(final Environment env, String path) {
        return clean(env, path, Memory.NULL);
    }

    @Signature
    public static ArrayMemory clean(final Environment env, String path, @Nullable Memory filter) {
        ArrayMemory result = new ArrayMemory();
        final ArrayMemory success = new ArrayMemory();
        final ArrayMemory error = new ArrayMemory();
        final ArrayMemory skip = new ArrayMemory();

        result.put("success", success);
        result.put("error", error);
        result.put("skip", skip);

        boolean isFilter = filter.isArray();

        if (isFilter) {
            filter = scanFilter(env, filter.toValue(ArrayMemory.class));
        }

        final Invoker checker = Invoker.create(env, filter);

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

    @Signature
    public static boolean rename(String path, String newName) {
        String parent = parent(path);
        return new File(path).renameTo(new File((parent != null ? parent + File.separator : "") + newName));
    }

    @Signature
    public static boolean move(String path, String newPath) {
        return new File(path).renameTo(new File(newPath));
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
