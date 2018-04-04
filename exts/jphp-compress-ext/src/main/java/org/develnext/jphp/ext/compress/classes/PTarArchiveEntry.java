package org.develnext.jphp.ext.compress.classes;

import java.io.File;
import java.util.Map;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name("TarArchiveEntry")
@Namespace(CompressExtension.NS)
public class PTarArchiveEntry extends PArchiveEntry<TarArchiveEntry> {
    interface WrappedInterface {
        @Property long userId();
        @Property String userName();
        @Property long groupId();
        @Property String groupName();
        @Property String linkName();
        @Property int mode();
        @Property long modTime();
        @Property long realSize();
        @Property int devMinor();
        @Property int devMajor();

        void addPaxHeader(String name,String value);
        void clearExtraPaxHeaders();
        String getExtraPaxHeader(String name);
        Map<String, String> getExtraPaxHeaders();

        boolean isCheckSumOK();
        boolean isBlockDevice();
        boolean isFIFO();
        boolean isSparse();
        boolean isCharacterDevice();
        boolean isLink();
        boolean isSymbolicLink();
        boolean isFile();
        boolean isGlobalPaxHeader();
        boolean isPaxHeader();
        boolean isGNULongNameEntry();
        boolean isGNULongLinkEntry();
        boolean isStarSparse();
        boolean isPaxGNUSparse();
        boolean isOldGNUSparse();
        boolean isGNUSparse();
        boolean isExtended();
    }

    public PTarArchiveEntry(Environment env, TarArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PTarArchiveEntry(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String name) {
        __construct(name, 0);
    }

    @Signature
    public void __construct(String name, long size) {
        __wrappedObject = new TarArchiveEntry(name);
        getWrappedObject().setSize(size);
    }

    @Signature
    public PTarArchiveEntry ofFile(Environment env, File file) {
        return new PTarArchiveEntry(env, new TarArchiveEntry(file));
    }

    @Signature
    public PTarArchiveEntry ofFile(Environment env, File file, String fileName) {
        return new PTarArchiveEntry(env, new TarArchiveEntry(file, fileName));
    }

    @Setter
    public void setName(String name) {
        getWrappedObject().setName(name);
    }

    @Setter
    public void setSize(long size) {
        getWrappedObject().setSize(size);
    }

    @Signature
    public void writeEntryHeader(Environment env, Memory outbuf) {
        getWrappedObject().writeEntryHeader(outbuf.getBinaryBytes(env.getDefaultCharset()));
    }
}
