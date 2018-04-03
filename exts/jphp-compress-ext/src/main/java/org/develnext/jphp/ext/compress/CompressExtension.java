package org.develnext.jphp.ext.compress;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.develnext.jphp.ext.compress.classes.*;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class CompressExtension extends Extension {
    public static final String NS = "compress";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }


    @Override
    public String[] getPackageNames() {
        return new String[] { NS };
    }

    @Override
    public void onRegister(CompileScope scope) {
        // register classes ...
        registerWrapperClass(scope, ArchiveEntry.class, PArchiveEntry.class);
        registerWrapperClass(scope, TarArchiveEntry.class, PTarArchiveEntry.class);
        registerWrapperClass(scope, ZipArchiveEntry.class, PZipArchiveEntry.class);

        registerWrapperClass(scope, ArchiveInputStream.class, PArchiveInput.class);
        registerWrapperClass(scope, TarArchiveInputStream.class, PTarArchiveInput.class);
        registerWrapperClass(scope, ZipArchiveInputStream.class, PZipArchiveInput.class);

        registerWrapperClass(scope, ArchiveOutputStream.class, PArchiveOutput.class);
        registerWrapperClass(scope, TarArchiveOutputStream.class, PTarArchiveOutput.class);
        registerWrapperClass(scope, ZipArchiveOutputStream.class, PZipArchiveOutput.class);

        registerClass(scope, PGzipOutputStream.class);
        registerClass(scope, PGzipInputStream.class);
        registerClass(scope, PBzip2OutputStream.class);
        registerClass(scope, PBZip2InputStream.class);
        registerClass(scope, PLz4OutputStream.class);
        registerClass(scope, PLz4InputStream.class);

        registerClass(scope, PArchive.class);
        registerClass(scope, PTarArchive.class);
        registerClass(scope, PZipArchive.class);
    }
}
