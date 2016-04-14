package org.develnext.jphp.ext.orientdb.classes;

import com.orientechnologies.orient.core.command.OCommandExecutor;
import com.orientechnologies.orient.core.command.OCommandRequestText;
import com.orientechnologies.orient.core.config.OStorageClusterConfiguration;
import com.orientechnologies.orient.core.db.ODatabaseListener;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.metadata.security.OUser;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.develnext.jphp.ext.orientdb.OrientDbExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.util.WrapFlow;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Namespace(OrientDbExtension.NS)
public class ODatabase extends BaseWrapper<ODatabaseDocument> {
    public ODatabase(Environment env, ODatabaseDocumentTx wrappedObject) {
        super(env, wrappedObject);
    }

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

    @Signature
    public boolean exists() {
        return getWrappedObject().exists();
    }

    @Signature
    public void open(String username, String password) {
        getWrappedObject().open(username, password);
    }

    @Signature
    public void close() {
        getWrappedObject().close();
    }

    @Signature
    public boolean isClosed() {
        return getWrappedObject().isClosed();
    }

    @Signature
    public void drop() {
        getWrappedObject().drop();
    }

    @Signature
    public void create() {
        getWrappedObject().create();
    }

    @Signature
    public void reload() {
        getWrappedObject().reload();
    }

    @Signature
    public void freeze() {
        getWrappedObject().freeze();
    }

    @Signature
    public void release() {
        getWrappedObject().release();
    }

    @Signature
    public void commit() {
        getWrappedObject().commit();
    }

    @Signature
    public void commit(boolean force) {
        getWrappedObject().commit(force);
    }

    @Signature
    public void begin() {
        getWrappedObject().begin();
    }

    @Signature
    public void rollback() {
        getWrappedObject().rollback();
    }

    @Signature
    public void rollback(boolean force) {
        getWrappedObject().rollback(force);
    }

    @Signature
    public WrapFlow getDocuments(Environment env, String className) {
        return new WrapFlow(env, getWrappedObject().browseClass(className));
    }

    @Signature
    public WrapFlow query(Environment env, String query) {
        return query(env, query, -1);
    }

    @Signature
    public int command(Environment env, String command, ArrayMemory args) {
        return getWrappedObject().command(new OCommandSQL(command)).execute(args.values());
    }

    @Signature
    public WrapFlow query(Environment env, String query, int limit) {
        return new WrapFlow(env, getWrappedObject().<List<?>>query(new OSQLSynchQuery<>(query, limit)));
    }

    @Signature
    public void setUser(String userName) {
        getWrappedObject().setUser(new OUser(userName));
    }

    @Signature
    public void setUserAndPassword(String userName, String password) {
        getWrappedObject().setUser(new OUser(userName, password));
    }

    @Getter
    public String getName() {
        return getWrappedObject().getName();
    }

    @Getter
    public String getType() {
        return getWrappedObject().getType();
    }

    @Getter
    public String getUrl() {
        return getWrappedObject().getURL();
    }

    @Getter
    public com.orientechnologies.orient.core.db.ODatabase.STATUS getStatus() {
        return getWrappedObject().getStatus();
    }

    @Getter
    public long getSize() {
        return getWrappedObject().getSize();
    }
}
