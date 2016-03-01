package org.develnext.jphp.ext.orientdb.classes;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Signature
public class ODatabase extends BaseWrapper<ODatabaseDocument> {
    public ODatabase(Environment env, ODatabaseDocument wrappedObject) {
        super(env, wrappedObject);
    }

    public ODatabase(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String url) {
        __wrappedObject = new ODatabaseDocumentTx(url);
    }
}
