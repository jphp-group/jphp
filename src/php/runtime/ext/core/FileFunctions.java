package php.runtime.ext.core;

import php.runtime.Memory;
import php.runtime.common.Constants;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.stream.PHP_IOException;
import php.runtime.ext.core.stream.Stream;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import static php.runtime.annotation.Runtime.Immutable;

public class FileFunctions extends FunctionsContainer {

    @Immutable
    public static String basename(String path, String suffix){
        String result = new File(path).getName();
        if (suffix != null && !suffix.isEmpty() && result.endsWith(suffix))
            result = result.substring(0, result.length() - suffix.length());

        return result;
    }

    @Immutable
    public static String basename(String path){
        return basename(path, null);
    }

    public static boolean chgrp(String fileName, Memory group){
        return false;
    }

    public static boolean chmod(String fileName, int mode){
        if (!Constants.OS_WINDOWS){
            try {
                Process proc = Runtime.getRuntime().exec(String.format("chmod %s '%s'", mode, fileName));
                return proc.waitFor() == 0;
            } catch (IOException e) {
                return false;
            } catch (InterruptedException e) {
                return false;
            }
        } else
            return true;
    }

    public static boolean chown(String fileName, String owner){
        if (!Constants.OS_WINDOWS){
            try {
                Process proc = Runtime.getRuntime().exec(String.format("chown %s '%s'", owner, fileName));
                return proc.waitFor() == 0;
            } catch (IOException e) {
                return false;
            } catch (InterruptedException e) {
                return false;
            }
        } else
            return true;
    }

    public static boolean copy(Environment env, TraceInfo trace, String source, String dest) throws Throwable {
        Stream stream = Stream.create(env, trace, source, "r");
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
            outputStream.write(value.getBinaryBytes());
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
    public static String dirname(String path){
        return new File(path).getPath();
    }

    public static Memory disk_free_space(String path){
        return LongMemory.valueOf(new File(path).getFreeSpace());
    }

    public static Memory disk_total_space(String path){
        return LongMemory.valueOf(new File(path).getTotalSpace());
    }

    public static Memory diskfreespace(String path){
        return disk_free_space(path);
    }

    public static boolean file_exists(String path){
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
            stream = Stream.create(env, trace, path, "r");
            if (stream == null){
                env.warning(trace, "file(): failed to open stream");
                return Memory.FALSE;
            }
            stream.setContext(env, context);
            Memory value = env.invokeMethod(trace, stream, "readFully");

            byte[] bytes = value.getBinaryBytes();
            ArrayMemory result = new ArrayMemory();

            int prev = 0;
            boolean ignoreNewLines = (flags & FileConstants.FILE_IGNORE_NEW_LINES) == FileConstants.FILE_IGNORE_NEW_LINES;
            int i;
            for(i = 0; i < bytes.length; i++){
                byte ch = bytes[i];
                if (ch == '\n') {
                    if (prev == i && (flags & FileConstants.FILE_SKIP_EMPTY_LINES) == FileConstants.FILE_SKIP_EMPTY_LINES){
                        prev += 1;
                        continue;
                    }

                    byte[] chunk = ignoreNewLines ? Arrays.copyOfRange(bytes, prev, i - 1) : Arrays.copyOfRange(bytes, prev, i);
                    prev = i + 1;

                    result.add(new BinaryMemory(chunk));
                }
            }
            if (prev != i){
                byte[] chunk = Arrays.copyOfRange(bytes, prev, i);
                result.add(new BinaryMemory(chunk));
            }

            return result.toConstant();
        } catch (PHP_IOException e){
            if (stream == null && (flags & FileConstants.FILE_USE_INCLUDE_PATH) == FileConstants.FILE_USE_INCLUDE_PATH){
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
            stream = Stream.create(env, trace, path, "r");
            if (stream == null){
                env.warning(trace, "file_get_contents(): failed to open stream");
                return Memory.FALSE;
            }
            stream.setContext(env, context);

            if (offset.toLong() > 0)
                stream.seek(env, offset);

            if (maxLength.isNull())
                return stream.readFully(env);
            else
                return stream.read(env, maxLength);
        } catch (PHP_IOException e){
            if (stream == null && useIncludePaths){
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

            stream = Stream.create(env, trace, path, mode);
            if (stream == null){
                env.warning(trace, "file_put_contents(): failed to open stream");
                return Memory.FALSE;
            }
            stream.setContext(env, context);
            if (data.instanceOf(Stream.CLASS_NAME)){
                data = env.invokeMethod(trace, data, "readFully");
            }

            return stream.write(env, data, Memory.NULL);
        } catch (PHP_IOException e){
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

    public static boolean is_dir(String path){
        return new File(path).isDirectory();
    }

    public static boolean is_file(String path){
        return new File(path).isFile();
    }
}
