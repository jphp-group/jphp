package org.develnext.jphp.ext.compress;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.develnext.jphp.ext.compress.classes.PArchiveEntry;
import org.develnext.jphp.ext.compress.classes.PArchiveInput;
import org.develnext.jphp.ext.compress.classes.PArchiveOutput;
import org.develnext.jphp.ext.compress.classes.PGzipInputStream;
import org.develnext.jphp.ext.compress.classes.PGzipOutputStream;
import org.develnext.jphp.ext.compress.classes.PTarArchiveEntry;
import org.develnext.jphp.ext.compress.classes.PTarArchiveInput;
import org.develnext.jphp.ext.compress.classes.PTarArchiveOutput;
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

        registerWrapperClass(scope, ArchiveInputStream.class, PArchiveInput.class);
        registerWrapperClass(scope, TarArchiveInputStream.class, PTarArchiveInput.class);

        registerWrapperClass(scope, ArchiveOutputStream.class, PArchiveOutput.class);
        registerWrapperClass(scope, TarArchiveOutputStream.class, PTarArchiveOutput.class);

        registerClass(scope, PGzipOutputStream.class);
        registerClass(scope, PGzipInputStream.class);
    }
}
