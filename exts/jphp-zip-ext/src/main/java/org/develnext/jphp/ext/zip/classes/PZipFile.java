package org.develnext.jphp.ext.zip.classes;

import org.develnext.jphp.ext.zip.ZipExtension;
import org.zeroturnaround.zip.*;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.ItemsUtils;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

@Name("ZipFile")
@Namespace(ZipExtension.NS)
public class PZipFile extends BaseObject {
    private File zipFile;

    public PZipFile(Environment env, File zipFile) {
        super(env);
        this.zipFile = zipFile;
    }

    public PZipFile(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(File file) throws FileNotFoundException {
        __construct(file, false);
    }

    @Signature
    public void __construct(File file, boolean create) throws FileNotFoundException {
        if (!file.isFile()) {
            if (create) {
                ZipUtil.packEntries(new File[0], file);
            } else {
                throw new FileNotFoundException(file.getPath() + " not found, use ZipFile::create() to make zip archive.");
            }
        }

        this.zipFile = file;
    }

    @Signature
    public static PZipFile create(Environment env, File file) throws Throwable {
        return create(env, file, true);
    }

    @Signature
    public static PZipFile create(Environment env, File file, boolean rewrite) throws Throwable {
        if (rewrite) {
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IOException("Failed to rewrite zip archive: " + file.getPath());
                }
            }
        } else {
            if (file.exists()) {
                throw new IOException("Zip archive already exists: " + file.getPath());
            }
        }

        PZipFile zipFile = new PZipFile(env, file);
        env.invokeMethod(zipFile, "__construct", StringMemory.valueOf(file.getPath()), Memory.TRUE);
        return zipFile;
    }

    @Signature
    public Memory __debugInfo() {
        return ArrayMemory.ofPair("*path", getPath()).toConstant();
    }

    @Signature
    public String getPath() {
        return zipFile.getPath();
    }


    @Signature
    public void repack(int compressLevel) {
        ZipUtil.repack(zipFile, compressLevel);
    }

    @Signature
    public void add(String path, InputStream source) throws IOException {
        add(path, source, ZipUtil.DEFAULT_COMPRESSION_LEVEL);
    }

    private static ZipEntrySource streamToEntrySource(InputStream stream, String path, int compressLevel) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return new ByteSource(path, buffer.toByteArray(), compressLevel);
    }

    @Signature
    public void add(String path, InputStream source, int compressLevel) throws IOException {
        ZipUtil.addOrReplaceEntries(zipFile, new ZipEntrySource[] { streamToEntrySource(source, path, compressLevel) });
    }

    @Signature
    public void addFromString(Environment env, String path, Memory string) {
        addFromString(env, path, string, ZipUtil.DEFAULT_COMPRESSION_LEVEL);
    }

    @Signature
    public void addFromString(Environment env, String path, Memory string, int compressLevel) {
        ZipUtil.addOrReplaceEntries(zipFile, new ZipEntrySource[] {
            new ByteSource(path, string.getBinaryBytes(env.getDefaultCharset()), compressLevel)
        });
    }

    @Signature
    public void addDirectory(Environment env, File path) {
        addDirectory(env, path, ZipUtil.DEFAULT_COMPRESSION_LEVEL, null);
    }

    @Signature
    public void addDirectory(Environment env, File path, int compressLevel) {
        addDirectory(env, path, compressLevel, null);
    }

    @Signature
    public void addDirectory(Environment env, File path, int compressLevel, @Nullable Invoker invoker) {
        ZipUtil.pack(path, zipFile, invokerToNameMapper(invoker), compressLevel);
    }

    @Signature
    public void remove(Environment env, Memory path) {
        if (path.isTraversable()) {
            ForeachIterator iterator = path.getNewIterator(env);

            List<String> paths = new ArrayList<>();
            while (iterator.next()) {
                String value = iterator.getValue().toString();
                paths.add(value);
            }

            ZipUtil.removeEntries(zipFile, paths.toArray(new String[paths.size()]));
        } else {
            ZipUtil.removeEntry(zipFile, path.toString());
        }
    }

    private static ArrayMemory zipEntryToArray(ZipEntry zipEntry) {
        final ArrayMemory result = new ArrayMemory();
        result.refOfIndex("name").assign(zipEntry.getName());
        result.refOfIndex("crc").assign(zipEntry.getCrc());
        result.refOfIndex("size").assign(zipEntry.getSize());
        result.refOfIndex("compressedSize").assign(zipEntry.getCompressedSize());
        result.refOfIndex("time").assign(zipEntry.getTime());
        result.refOfIndex("method").assign(zipEntry.getMethod());
        result.refOfIndex("comment").assign(zipEntry.getComment());
        result.refOfIndex("directory").assign(zipEntry.isDirectory());

        return result;
    }

    @Signature
    public Memory stat(String path) {
        final ArrayMemory[] result = new ArrayMemory[1];

        ZipUtil.iterate(zipFile, new String[] { path }, new ZipInfoCallback() {
            @Override
            public void process(ZipEntry zipEntry) throws IOException {
                result[0] = zipEntryToArray(zipEntry);
            }
        });

        return result[0].toConstant();
    }

    @Signature
    public Memory statAll() {
        final ArrayMemory result = new ArrayMemory();

        ZipUtil.iterate(zipFile, new ZipInfoCallback() {
            @Override
            public void process(ZipEntry zipEntry) throws IOException {
                result.put(zipEntry.getName(), zipEntryToArray(zipEntry));
            }
        });

        return result.toConstant();
    }

    @Signature
    public void read(final Environment env, String path, final Invoker callback) {
        ZipUtil.iterate(zipFile, new String[]{path}, new ZipEntryCallback() {
            @Override
            public void process(InputStream in, ZipEntry zipEntry) throws IOException {
                callback.callAny(zipEntryToArray(zipEntry), new MiscStream(env, in));
            }
        });
    }

    @Signature
    public void readAll(final Environment env, final Invoker callback) {
        ZipUtil.iterate(zipFile, new ZipEntryCallback() {
            @Override
            public void process(InputStream in, ZipEntry zipEntry) throws IOException {
                callback.callAny(zipEntryToArray(zipEntry), new MiscStream(env, in));
            }
        });
    }

    @Signature
    public boolean has(String path) {
        return ZipUtil.containsEntry(zipFile, path);
    }

    @Signature
    public static void unwrap(PZipFile zip, File outputDir, @Nullable Invoker callback) throws FileNotFoundException {
        if (!zip.zipFile.isFile()) {
            throw new FileNotFoundException(zip.zipFile.getPath() + " not found");
        }

        NameMapper mapper = invokerToNameMapper(callback);

        ZipUtil.unwrap(zip.zipFile, outputDir, mapper);
    }

    @Signature
    public void unpack(File outputDir) throws FileNotFoundException {
        unpack(outputDir, null, null);
    }

    @Signature
    public void unpack(File outputDir, String charset) throws FileNotFoundException {
        unpack(outputDir, charset, null);
    }

    @Signature
    public void unpack(File outputDir, String charset, @Nullable final Invoker callback) throws FileNotFoundException {
        if (!zipFile.isFile()) {
            throw new FileNotFoundException(zipFile.getPath() + " not found");
        }

        NameMapper mapper = invokerToNameMapper(callback);

        if (charset == null || charset.isEmpty()) {
            ZipUtil.unpack(zipFile, outputDir, mapper);
        } else {
            ZipUtil.unpack(zipFile, outputDir, Charset.forName(charset));
        }
    }

    private static NameMapper invokerToNameMapper(final Invoker invoker) {
        return new NameMapper() {
            @Override
            public String map(String name) {
                if (invoker == null) {
                    return name;
                }

                Memory r = invoker.callNoThrow(StringMemory.valueOf(name));

                if (r.isNull()) {
                    return name;
                }

                return r.toString();
            }
        };
    }
}
