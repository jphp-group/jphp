package org.develnext.jphp.ext.orientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.develnext.jphp.ext.orientdb.classes.ODatabase;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.memory.support.MemoryOperation;

public class OrientDbExtension extends Extension {
    public final static String NS = "php\\orientdb";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, ODocument.class, org.develnext.jphp.ext.orientdb.classes.ODocument.class);

        registerWrapperClass(scope, ODatabaseDocument.class, ODatabase.class);
        MemoryOperation.registerWrapper(ODatabaseDocumentTx.class, ODatabase.class);
    }
}
