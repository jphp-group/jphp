package org.develnext.jphp.ext.zip;

import org.develnext.jphp.ext.zip.classes.PZipException;
import org.develnext.jphp.ext.zip.classes.PZipFile;
import org.zeroturnaround.zip.ZipException;
import org.zeroturnaround.zip.ZipUtil;
import php.runtime.annotation.Reflection;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class ZipExtension extends Extension {
    public final static String NS = "php\\compress";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "compress" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PZipFile.class);
        registerJavaException(scope, PZipException.class, ZipException.class);
    }
}
