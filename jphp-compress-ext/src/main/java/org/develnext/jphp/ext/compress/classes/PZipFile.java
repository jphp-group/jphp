package org.develnext.jphp.ext.compress.classes;

import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Name("ZipFile")
@Namespace(CompressExtension.NS)
public class PZipFile extends BaseWrapper<ZipFile> {
    interface WrappedInterface {
        String getName();
        String getComment();
        void close();
    }

    public PZipFile(Environment env, ZipFile wrappedObject) {
        super(env, wrappedObject);
    }

    public PZipFile(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String filename) throws IOException {
        __construct(filename, "UTF-8");
    }

    @Signature
    public void __construct(String filename, String charset) throws IOException {
        __wrappedObject = new ZipFile(filename, Charset.forName(charset));
    }

    @Signature
    public ZipEntry getEntry(String name) {
        return getWrappedObject().getEntry(name);
    }

    public static byte[] readFully(InputStream is, int length, boolean readAll)
            throws IOException {
        byte[] output = {};
        if (length == -1) length = Integer.MAX_VALUE;
        int pos = 0;
        while (pos < length) {
            int bytesToRead;
            if (pos >= output.length) { // Only expand when there's no room
                bytesToRead = Math.min(length - pos, output.length + 1024);
                if (output.length < pos + bytesToRead) {
                    output = Arrays.copyOf(output, pos + bytesToRead);
                }
            } else {
                bytesToRead = output.length - pos;
            }
            int cc = is.read(output, pos, bytesToRead);
            if (cc < 0) {
                if (readAll && length != Integer.MAX_VALUE) {
                    throw new EOFException("Detect premature EOF");
                } else {
                    if (output.length != pos) {
                        output = Arrays.copyOf(output, pos);
                    }
                    break;
                }
            }
            pos += cc;
        }
        return output;
    }

    @Signature
    public byte[] getEntryContent(String name) throws IOException {
        ZipEntry entry = getWrappedObject().getEntry(name);

        if (entry == null) {
            return null;
        }

        InputStream stream = getWrappedObject().getInputStream(entry);
        return readFully(stream, -1, true);
    }

    @Signature
    public InputStream getEntryStream(String name) throws IOException {
        ZipEntry entry = getWrappedObject().getEntry(name);

        if (entry == null) {
            return null;
        }

        return getWrappedObject().getInputStream(entry);
    }

    @Signature
    public List<String> getEntryNames() {
        List<String> result = new ArrayList<>();

        for (Enumeration entries = getWrappedObject().entries(); entries.hasMoreElements();) {
            // Get the entry name
            String zipEntryName = ((ZipEntry)entries.nextElement()).getName();
            result.add(zipEntryName);
        }

        return result;
    }
}
