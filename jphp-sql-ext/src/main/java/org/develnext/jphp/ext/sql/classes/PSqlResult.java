package org.develnext.jphp.ext.sql.classes;

import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.time.WrapTime;
import php.runtime.lang.BaseObject;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.sql.*;

@Abstract
@Reflection.Name("SqlResult")
@Reflection.Namespace(SqlExtension.NS)
public class PSqlResult extends BaseObject {
    protected ResultSet resultSet;
    protected ResultSetMetaData metaData;

    public PSqlResult(Environment env, ResultSet resultSet) {
        super(env);
        this.resultSet = resultSet;

        try {
            this.metaData  = resultSet.getMetaData();
        } catch (SQLException e) {
            throw new WrapSqlException(env, e);
        }
    }

    public PSqlResult(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public boolean isLast() throws SQLException {
        return resultSet.isLast();
    }

    @Signature
    public boolean isFirst() throws SQLException {
        return resultSet.isFirst();
    }

    public Memory getTyped(Environment env, int index) throws SQLException {
        index += 1;

        ResultSet set = resultSet;
        int type = metaData.getColumnType(index);

        if (set.getObject(index) == null) {
            return Memory.NULL;
        }

        switch (type) {
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.BIT:
                return LongMemory.valueOf(set.getLong(index));

            case Types.BOOLEAN:
                return TrueMemory.valueOf(set.getBoolean(index));

            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.DECIMAL:
                return DoubleMemory.valueOf(set.getDouble(index));

            case Types.NULL:
                return Memory.NULL;

            case Types.TIME:
                Time time = set.getTime(index);
                return ObjectMemory.valueOf(new WrapTime(env, time));

            case Types.TIMESTAMP:
                Timestamp timestamp = set.getTimestamp(index);
                return ObjectMemory.valueOf(new WrapTime(env, timestamp));

            case Types.DATE:
                Date date = set.getDate(index);
                return ObjectMemory.valueOf(new WrapTime(env, date));

            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.BIGINT:
                return StringMemory.valueOf(set.getString(index));

            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                return StringMemory.valueOf(set.getNString(index));

            case Types.BINARY:
                return new BinaryMemory(set.getBytes(index));

            case Types.BLOB:
            case Types.CLOB:
            case Types.NCLOB:
                try {
                    Blob blob = set.getBlob(index);
                    return ObjectMemory.valueOf(new MiscStream(env, blob.getBinaryStream()));
                } catch (SQLException e) {
                    return new BinaryMemory(set.getBytes(index));
                }

            default:
                return StringMemory.valueOf(set.getString(index));
        }
    }

    @Signature
    public Memory get(Environment env, Memory column) throws SQLException {
        return getTyped(env, resultSet.findColumn(column.toString()) - 1);
    }

    @Signature
    public ArrayMemory toArray(Environment env) throws SQLException {
        return toArray(env, true);
    }

    @Signature
    public void delete() throws SQLException {
        resultSet.deleteRow();
    }

    @Signature
    public boolean isDeleted() throws SQLException {
        return resultSet.rowDeleted();
    }

    @Signature
    public void refresh() throws SQLException {
        resultSet.refreshRow();
    }

    @Signature
    public ArrayMemory toArray(Environment env, boolean assoc) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        ArrayMemory result = new ArrayMemory();
        int count = metaData.getColumnCount();

        for (int i = 0; i < count; i++) {
            Memory value = getTyped(env, i);

            if (assoc) {
                result.putAsKeyString(metaData.getColumnLabel(i + 1), value);
            } else {
                result.add(value);
            }
        }

        return result.toConstant();
    }
}
