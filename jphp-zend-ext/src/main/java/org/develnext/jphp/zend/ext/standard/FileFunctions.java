package org.develnext.jphp.zend.ext.standard;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.memory.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.*;

import static org.develnext.jphp.zend.ext.standard.FileConstants.*;
import static php.runtime.annotation.Runtime.Immutable;

public class FileFunctions extends FunctionsContainer {

    @Immutable
    public static String basename(String path, String suffix) {
        String result = new File(path).getName();
        if (suffix != null && !suffix.isEmpty() && result.endsWith(suffix))
            result = result.substring(0, result.length() - suffix.length());

        return result;
    }

    @Immutable
    public static String basename(String path) {
        return basename(path, null);
    }

    public static boolean chgrp(String fileName, Memory group) {
        return false;
    }

    public static boolean copy(Environment env, TraceInfo trace, String source, String dest) throws Throwable {
        Stream stream = Stream.create(env, source, "r");
        if (stream == null) {
            env.warning("copy(): Invalid source path");
            return false;
        }

        Memory value = stream.readFully(env);
        RandomAccessFile outputStream;
        try {
            outputStream = new RandomAccessFile(dest, "rw");
        } catch (FileNotFoundException e) {
            return false;
        }

        try {
            outputStream.setLength(0);
            outputStream.write(value.getBinaryBytes(env.getDefaultCharset()));
            return true;
        } catch (IOException e) {
            env.warning("copy(): " + e.getMessage());
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                return false;
            }
        }
    }

    @Immutable
    public static String dirname(String path) {
        String r = new File(path).getParent();
        if (r == null)
            return "";
        return r;
    }

    public static Memory disk_free_space(String path) {
        return LongMemory.valueOf(new File(path).getFreeSpace());
    }

    public static Memory disk_total_space(String path) {
        return LongMemory.valueOf(new File(path).getTotalSpace());
    }

    public static Memory diskfreespace(String path) {
        return disk_free_space(path);
    }

    public static boolean file_exists(String path) {
        return new File(path).getAbsoluteFile().exists();
    }

    public static Memory file(Environment env, TraceInfo trace, String path, int flags) throws Throwable {
        return file(env, trace, path, flags, Memory.NULL);
    }

    public static Memory file(Environment env, TraceInfo trace, String path) throws Throwable {
        return file(env, trace, path, 0, Memory.NULL);
    }

    public static Memory file(Environment env, TraceInfo trace, String path, int flags, Memory context) throws Throwable {
        Stream stream = null;
        try {
            stream = Stream.create(env, path, "r");
            if (stream == null) {
                env.warning(trace, "file(): failed to open stream");
                return Memory.FALSE;
            }
            stream.setContext(env, context);
            Memory value = env.invokeMethod(trace, stream, "readFully");

            byte[] bytes = value.getBinaryBytes(env.getDefaultCharset());
            ArrayMemory result = new ArrayMemory();

            int prev = 0;
            boolean ignoreNewLines = (flags & FileConstants.FILE_IGNORE_NEW_LINES) == FileConstants.FILE_IGNORE_NEW_LINES;
            int i;
            for (i = 0; i < bytes.length; i++) {
                byte ch = bytes[i];
                if (ch == '\n') {
                    if (prev == i && (flags & FileConstants.FILE_SKIP_EMPTY_LINES) == FileConstants.FILE_SKIP_EMPTY_LINES) {
                        prev += 1;
                        continue;
                    }

                    byte[] chunk = ignoreNewLines ? Arrays.copyOfRange(bytes, prev, i - 1) : Arrays.copyOfRange(bytes, prev, i);
                    prev = i + 1;

                    result.add(new BinaryMemory(chunk));
                }
            }
            if (prev != i) {
                byte[] chunk = Arrays.copyOfRange(bytes, prev, i);
                result.add(new BinaryMemory(chunk));
            }

            return result.toConstant();
        } catch (WrapIOException e) {
            if (stream == null && (flags & FileConstants.FILE_USE_INCLUDE_PATH) == FileConstants.FILE_USE_INCLUDE_PATH) {
                path = env.findInIncludePaths(path);
                if (path != null)
                    return file(env, trace, path, flags ^ FileConstants.FILE_USE_INCLUDE_PATH, context);
            }
            env.warning(trace, "file(): " + e.getMessage());
            return Memory.FALSE;
        } finally {
            if (stream != null)
                stream.close(env);
        }
    }

    public static Memory file_get_contents(Environment env, TraceInfo trace, String path, boolean useIncludePaths,
                                           Memory context, Memory offset, Memory maxLength) throws Throwable {
        Stream stream = null;
        try {
            stream = Stream.create(env, path, "r");
            if (stream == null) {
                env.warning(trace, "file_get_contents(): failed to open stream");
                return Memory.FALSE;
            }
            stream.setContext(env, context);

            if (offset.toLong() > 0)
                stream.seek(env, offset);

            if (maxLength.isNull())
                return stream.readFully(env, LongMemory.valueOf(4096));
            else
                return stream.read(env, maxLength);
        } catch (WrapIOException | IOException e) {
            if (stream == null && useIncludePaths) {
                path = env.findInIncludePaths(path);
                if (path != null)
                    return file_get_contents(env, trace, path, false, context, offset, maxLength);
            }
            env.warning(trace, "file_get_contents(): " + e.getMessage());
            return Memory.FALSE;
        } finally {
            if (stream != null)
                stream.close(env);
        }
    }

    public static Memory file_get_contents(Environment env, TraceInfo trace, String path, boolean useIncludePaths,
                                           Memory context, Memory offset) throws Throwable {
        return file_get_contents(env, trace, path, useIncludePaths, context, offset, Memory.NULL);
    }

    public static Memory file_get_contents(Environment env, TraceInfo trace, String path, boolean useIncludePaths,
                                           Memory context) throws Throwable {
        return file_get_contents(env, trace, path, useIncludePaths, context, Memory.CONST_INT_M1, Memory.NULL);
    }

    public static Memory file_get_contents(Environment env, TraceInfo trace, String path, boolean useIncludePaths) throws Throwable {
        return file_get_contents(env, trace, path, useIncludePaths, Memory.NULL, Memory.CONST_INT_M1, Memory.NULL);
    }

    public static Memory file_get_contents(Environment env, TraceInfo trace, String path) throws Throwable {
        return file_get_contents(env, trace, path, true, Memory.NULL, Memory.CONST_INT_M1, Memory.NULL);
    }


    public static Memory file_put_contents(Environment env, TraceInfo trace, String path, Memory data, int flags,
                                           Memory context) throws Throwable {
        Stream stream = null;
        try {
            String mode = "w";
            if ((flags & FileConstants.FILE_APPEND) == FileConstants.FILE_APPEND)
                mode = "a";

            stream = Stream.create(env, path, mode);
            if (stream == null) {
                env.warning(trace, "file_put_contents(): failed to open stream");
                return Memory.FALSE;
            }
            stream.setContext(env, context);
            if (data.instanceOf(Stream.CLASS_NAME)) {
                data = env.invokeMethod(trace, data, "readFully");
            }

            return stream.write(env, data, Memory.NULL);
        } catch (WrapIOException e) {
            env.warning(trace, "file_put_contents(): " + e.getMessage());
            return Memory.FALSE;
        } finally {
            if (stream != null)
                stream.close(env);
        }
    }

    public static Memory file_put_contents(Environment env, TraceInfo trace, String path, Memory data, int flags)
            throws Throwable {
        return file_put_contents(env, trace, path, data, flags, Memory.NULL);
    }

    public static Memory file_put_contents(Environment env, TraceInfo trace, String path, Memory data)
            throws Throwable {
        return file_put_contents(env, trace, path, data, 0, Memory.NULL);
    }

    public static boolean is_dir(String path) {
        return new File(path).isDirectory();
    }

    public static boolean is_file(String path) {
        return new File(path).isFile();
    }

    public static boolean is_link(String path) {
        try {
            File file = new File(path);

            File canon;
            if (file.getParent() == null) {
                canon = file;
            } else {
                File canonDir = file.getParentFile().getCanonicalFile();
                canon = new File(canonDir, file.getName());
            }
            return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean is_executable(String path) {
        return new File(path).canExecute();
    }

    public static boolean is_readable(String path) {
        return new File(path).canRead();
    }

    public static boolean is_writable(String path) {
        return new File(path).canWrite();
    }

    public static boolean is_writeable(String path) {
        return new File(path).canWrite();
    }

    public static boolean mkdir(String path, int mode, boolean recursive) {
        if (recursive)
            return new File(path).mkdirs();
        else
            return new File(path).mkdir();
    }

    public static boolean mkdir(String path, int mode) {
        return mkdir(path, mode, false);
    }

    public static boolean mkdir(String path) {
        return mkdir(path, 777, false);
    }

    public static Memory filemtime(Environment env, TraceInfo trace, String path) {
        Path file = Paths.get(path);

        try {
            return LongMemory.valueOf(Files.getLastModifiedTime(file).toMillis() / 1000);
        } catch (IOException e) {
            env.warning(trace, e.getMessage());
            return Memory.FALSE;
        } catch (UnsupportedOperationException e) {
            return Memory.FALSE;
        }
    }

    public static Memory fileatime(Environment env, TraceInfo trace, String path) {
        Path file = Paths.get(path);

        try {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            return LongMemory.valueOf(attributes.lastAccessTime().toMillis() / 1000);
        } catch (IOException e) {
            env.warning(trace, e.getMessage());
            return Memory.FALSE;
        } catch (UnsupportedOperationException e) {
            return Memory.FALSE;
        }
    }

    public static Memory filectime(Environment env, TraceInfo trace, String path) {
        Path file = Paths.get(path);

        try {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            return LongMemory.valueOf(attributes.creationTime().toMillis() / 1000);
        } catch (IOException e) {
            env.warning(trace, e.getMessage());
            return Memory.FALSE;
        } catch (UnsupportedOperationException e) {
            return Memory.FALSE;
        }
    }

    public static Memory filesize(Environment env, TraceInfo trace, String path) {
        try {
            return LongMemory.valueOf(new File(path).length());
        } catch (Exception e) {
            env.warning(trace, "filesize(): file not found - %s", path);
            return Memory.FALSE;
        }
    }

    public static Memory filetype(Environment env, TraceInfo trace, String path) {
        File file = new File(path);
        if (file.isFile())
            return new StringMemory("file");
        else if (file.isDirectory())
            return new StringMemory("dir");
        else {
            try {
                BasicFileAttributes attributes = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);

                if (attributes.isSymbolicLink()) {
                    return new StringMemory("link");
                }

                return new StringMemory("unknown");
            } catch (IOException e) {
                env.warning(trace, e.getMessage());
                return Memory.FALSE;
            }
        }
    }

    public static Memory filegroup(Environment env, TraceInfo trace, String path) {
        Path file = Paths.get(path);

        try {
            int attribute = (int) Files.getAttribute(file, "unix:gid");
            return LongMemory.valueOf(attribute);
        } catch (IOException|SecurityException e) {
            env.warning(trace, e.getMessage());
            return Memory.FALSE;
        } catch (UnsupportedOperationException e) {
            return Memory.FALSE;
        }
    }

    public static Memory fileowner(Environment env, TraceInfo trace, String path) {
        Path file = Paths.get(path);

        try {
            int attribute = (int) Files.getAttribute(file, "unix:uid");
            return LongMemory.valueOf(attribute);
        } catch (IOException|SecurityException e) {
            env.warning(trace, e.getMessage());
            return Memory.FALSE;
        } catch (UnsupportedOperationException e) {
            return Memory.FALSE;
        }
    }

    public static Memory fileperms(Environment env, TraceInfo trace, String path) {
        Path file = Paths.get(path);

        try {
            int attribute = (int) Files.getAttribute(file, "unix:mode");
            return LongMemory.valueOf(attribute);
        } catch (IOException|SecurityException e) {
            env.warning(trace, e.getMessage());
            return Memory.FALSE;
        } catch (UnsupportedOperationException e) {
            return Memory.FALSE;
        }
    }

    public static Memory pathinfo(String path, int options) {
        File file = new File(path);
        ArrayMemory result = new ArrayMemory();
        String basename = file.getName();

        int pos = basename.lastIndexOf('.');
        String ext = null;
        if (pos > -1)
            ext = basename.substring(pos + 1);

        if ((options & PATHINFO_DIRNAME) == PATHINFO_DIRNAME)
            result.refOfIndex("dirname").assign(file.getParent());

        if ((options & PATHINFO_DIRNAME) == PATHINFO_DIRNAME)
            result.refOfIndex("basename").assign(file.getName());

        if (ext != null && (options & PATHINFO_EXTENSION) == PATHINFO_EXTENSION)
            result.refOfIndex("extension").assign(ext);

        if ((options & PATHINFO_FILENAME) == PATHINFO_FILENAME)
            result.refOfIndex("filename").assign(pos > -1 ? basename.substring(0, pos) : basename);

        return result.toConstant();
    }

    public static Memory pathinfo(String path) {
        return pathinfo(path, PATHINFO_BASENAME | PATHINFO_DIRNAME | PATHINFO_EXTENSION | PATHINFO_FILENAME);
    }

    public static boolean rename(String oldname, String newname) {
        try {
            return new File(oldname).renameTo(new File(newname));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean rmdir(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            try {
                return file.delete();
            } catch (Exception e) {
                return false;
            }
        } else
            return false;
    }

    public static boolean unlink(String path) {
        File file = new File(path);
        if (file.isDirectory())
            return false;

        try {
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean touch(Environment env, TraceInfo trace, String path, long time, long atime) {
        File file = new File(path);
        if (!file.exists())
            try {
                if (!file.createNewFile())
                    return false;
            } catch (IOException e) {
                env.warning(trace, e.getMessage());
                return false;
            }

        return file.setLastModified(time * 1000);
    }

    public static boolean touch(Environment env, TraceInfo trace, String path, long time) {
        return touch(env, trace, path, time, 0);
    }

    public static boolean touch(Environment env, TraceInfo trace, String path) {
        return touch(env, trace, path, System.currentTimeMillis() / 1000, 0);
    }

    public static Memory tempnam(String dir, String prefix) {
        try {
            return new StringMemory(File.createTempFile(prefix, "", new File(dir)).getPath());
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }

    public static Memory realpath(String path) {
        try {
            return new StringMemory(new File(path).getCanonicalPath());
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }

    public static Memory readfile(Environment env, TraceInfo trace, String path, boolean useIncludePaths,
                                  Memory stream) throws Throwable {
        File file = new File(path);
        if (useIncludePaths && !file.exists()) {
            path = env.findInIncludePaths(path);
            if (path == null)
                return Memory.FALSE;

            file = new File(path);
        }

        try {
            RandomAccessFile accessFile = new RandomAccessFile(file, "r");
            try {
                if (!stream.isNull()) {
                    if (!stream.instanceOf(Stream.CLASS_NAME)) {
                        env.warning(trace, "readfile(): Argument 3 must be stream, %s given", stream.getRealType().toString());
                        return Memory.FALSE;
                    }

                    byte[] buff = new byte[4096];
                    int len = 0;
                    int read = 0;
                    while ((len = accessFile.read(buff)) != -1) {
                        read += len;
                        ObjectInvokeHelper.invokeMethod(
                                stream, "write", env, trace, new BinaryMemory(buff), LongMemory.valueOf(len)
                        );
                    }
                    return LongMemory.valueOf(read);
                } else {
                    byte[] buff = new byte[4096];
                    int len = 0;
                    int read = 0;
                    while ((len = accessFile.read(buff)) != -1) {
                        read += len;
                        env.echo(buff, len);
                    }
                    return LongMemory.valueOf(read);
                }
            } finally {
                accessFile.close();
            }
        } catch (FileNotFoundException e) {
            env.warning(trace, "readfile(): File not found - %s", path);
            return Memory.FALSE;
        } catch (IOException e) {
            env.warning(trace, "readfile(): %s", e.getMessage());
            return Memory.FALSE;
        }
    }

    public static Memory readfile(Environment env, TraceInfo trace, String path, boolean useIncludePaths)
            throws Throwable {
        return readfile(env, trace, path, useIncludePaths, Memory.NULL);
    }

    public static Memory readfile(Environment env, TraceInfo trace, String path)
            throws Throwable {
        return readfile(env, trace, path, false, Memory.NULL);
    }

    public static Memory fopen(Environment env, TraceInfo trace, String path, String mode) {
        try {
            return ObjectMemory.valueOf(Stream.create(env, path, mode));
        } catch (Throwable throwable) {
            env.warning(trace, "fopen(): failed to open stream, " + throwable.getMessage());
            return Memory.FALSE;
        }
    }

    public static Memory ftell(Environment env, TraceInfo trace, Memory stream) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            try {
                return env.invokeMethod(trace, stream, "getPosition");
            } catch (Throwable throwable) {
                env.warning(trace, "ftell(): " + throwable.getMessage());
                return Memory.FALSE;
            }
        }

        env.warning(trace, "ftell(): unable to get position from a non-stream");
        return Memory.FALSE;
    }

    public static Memory feof(Environment env, TraceInfo trace, Memory stream) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            try {
                return env.invokeMethod(trace, stream, "eof");
            } catch (Throwable throwable) {
                env.warning(trace, "feof(): " + throwable.getMessage());
                return Memory.FALSE;
            }
        }

        env.warning(trace, "feof(): unable get eof from a non-stream");
        return Memory.FALSE;
    }

    public static Memory fread(Environment env, TraceInfo trace, Memory stream, int length) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            try {
                return env.invokeMethod(trace, stream, "read", LongMemory.valueOf(length));
            } catch (Throwable throwable) {
                env.warning(trace, "fread(): " + throwable.getMessage());
                return Memory.FALSE;
            }
        }

        env.warning(trace, "fread(): unable to read from a non-stream");
        return Memory.FALSE;
    }

    public static Memory fgetc(Environment env, TraceInfo trace, Memory stream) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            try {
                Memory memory = env.invokeMethod(trace, stream, "read", Memory.CONST_INT_1);
                return memory.isNull() ? Memory.FALSE : memory;
            } catch (Throwable throwable) {
                env.warning(trace, "fgetc(): " + throwable.getMessage());
                return Memory.FALSE;
            }
        }

        env.warning(trace, "fgetc(): unable to read from a non-stream");
        return Memory.FALSE;
    }

    public static Memory fseek(Environment env, TraceInfo trace, Memory stream, long offset) {
        return fseek(env, trace, stream, offset, 0);
    }

    public static Memory fseek(Environment env, TraceInfo trace, Memory stream, long offset, int whence) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            try {
                switch (whence) {
                    case 1:
                        offset += env.invokeMethod(trace, stream, "getPosition").toLong();
                        break;
                    case 2:
                        env.error(trace, "fseek(): flag SEEK_END is not supported.");
                        break;
                    default:
                    case 0:
                        break;
                }

                env.invokeMethod(trace, stream, "seek", LongMemory.valueOf(offset));
                return Memory.CONST_INT_0;
            } catch (Throwable throwable) {
                env.warning(trace, "fseek(): " + throwable.getMessage());
                return Memory.CONST_INT_M1;
            }
        }

        env.warning(trace, "fseek(): unable to seek in a non-stream");
        return Memory.CONST_INT_M1;
    }

    public static Memory fputs(Environment env, TraceInfo trace, Memory stream, Memory value) {
        return fwrite(env, trace, stream, value);
    }

    public static Memory fputs(Environment env, TraceInfo trace, Memory stream, Memory value, Memory length) {
        return fwrite(env, trace, stream, value, length);
    }

    public static Memory fgets(Environment env, TraceInfo trace, Memory stream) {
        return fgets(env, trace, stream, Memory.NULL);
    }

    public static Memory fgets(Environment env, TraceInfo trace, Memory stream, Memory length) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            InputStream in = Stream.getInputStream(env, stream);

            if (in != null) {
                int read;

                StringBuilder sb = new StringBuilder();

                try {
                    while ((read = in.read()) != -1) {
                        if (length.isNotNull() && sb.length() >= length.toInteger()) {
                            break;
                        }

                        if (read == '\n' || read == '\r') {
                            break;
                        }

                        sb.append((char) read);
                    }

                    return StringMemory.valueOf(sb.toString());
                } catch (IOException e) {
                    env.warning(trace, "fgets(): " + e.getMessage());
                    return Memory.FALSE;
                }
            }
        }

        env.warning(trace, "fgets(): unable to get from a non-stream");
        return Memory.FALSE;
    }

    public static Memory fwrite(Environment env, TraceInfo trace, Memory stream, Memory value) {
        return fwrite(env, trace, stream, value, Memory.NULL);
    }

    public static Memory fwrite(Environment env, TraceInfo trace, Memory stream, Memory value, Memory length) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            try {
                return env.invokeMethod(trace, stream, "write", value, length);
            } catch (Throwable throwable) {
                env.warning(trace, "fwrite(): " + throwable.getMessage());
                return Memory.FALSE;
            }
        }

        env.warning(trace, "fwrite(): unable to write to a non-stream");
        return Memory.FALSE;
    }

    public static Memory fclose(Environment env, TraceInfo trace, Memory stream) {
        if (stream.instanceOf(Stream.CLASS_NAME)) {
            try {
                env.invokeMethod(trace, stream, "close");
                return Memory.TRUE;
            } catch (Throwable throwable) {
                return Memory.FALSE;
            }
        } else {
            env.warning("fclose(): unable to close a non-stream");
            return Memory.FALSE;
        }
    }

    public static String getcwd() {
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toString();
    }

    public static String getenv(Environment env, String name) {
        Map<String, String> zendEnv = env.getUserValue("env", Map.class);

        if (zendEnv != null) {
            String s = zendEnv.get(name);

            if (s != null) {
                return s;
            }
        }

        return System.getenv(name);
    }

    synchronized public static void putenv(Environment env, String _value) {
        if (_value.isEmpty()) {
            return;
        }

        String[] strings = StringUtils.split(_value, "=", 2);

        String name = strings[0];
        String value = strings.length > 1 ? strings[1] : null;

        Map<String, String> zendEnv = env.getUserValue("env", Map.class);

        if (zendEnv == null) {
            env.setUserValue("env", zendEnv = new HashMap<String, String>());
        }

        if (value == null) {
            zendEnv.remove(name);
        } else {
            zendEnv.put(name, value);
        }
    }

    public static Memory scandir(String path, int order) {
        ArrayMemory r = new ArrayMemory();

        String[] list = new File(path).list();

        switch (order) {
            case FileConstants.SCANDIR_SORT_DESCENDING:
                Arrays.sort(list, Collections.reverseOrder());
                for (String s : list) { r.add(s); }
                r.add("..");
                r.add(".");
                break;

            case FileConstants.SCANDIR_SORT_ASCENDING:
                r.add(".");
                r.add("..");
                Arrays.sort(list);
                for (String s : list) { r.add(s); }

                break;

            default:
                r.add(".");
                r.add("..");
                for (String s : list) { r.add(s); }

                break;
        }


        return r.toConstant();
    }

    public static Memory scandir(String path) {
        return scandir(path, FileConstants.SCANDIR_SORT_ASCENDING);
    }
}
