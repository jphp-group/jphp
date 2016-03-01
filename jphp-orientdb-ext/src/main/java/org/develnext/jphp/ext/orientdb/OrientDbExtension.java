package org.develnext.jphp.ext.orientdb;

import com.orientechnologies.orient.core.record.impl.ODocument;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class OrientDbExtension extends Extension {
    public final static String NS = "php\\orientdb";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, ODocument.class, org.develnext.jphp.ext.orientdb.classes.ODocument.class);
    }
}
