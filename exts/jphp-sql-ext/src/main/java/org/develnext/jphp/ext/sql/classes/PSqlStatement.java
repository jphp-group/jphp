package org.develnext.jphp.ext.sql.classes;

import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.time.WrapTime;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.InputStream;
import java.sql.*;

@Abstract
@Reflection.Name("SqlStatement")
@Reflection.Namespace(SqlExtension.NS)
public class PSqlStatement extends BaseObject implements Iterator {
    protected PreparedStatement statement;
    protected boolean valid = true;
    private ResultSet resultSet;

    public PSqlStatement(Environment env, PreparedStatement statement) {
        super(env);
        this.statement = statement;
    }

    public PSqlStatement(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public PSqlResult fetch(Environment env) throws SQLException {
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return new PSqlResult(env, resultSet);
        } else {
            return null;
        }
    }

    @Signature
    public boolean execute(Environment env) throws SQLException {
        return statement.execute();
    }

    @Signature
    public int update(Environment env) throws SQLException {
        return statement.executeUpdate();
    }

    @Signature
    public Memory getLastInsertId(Environment env) throws SQLException {
        Memory keys = getGeneratedKeys(env);

        if (keys.instanceOf(PSqlResult.class)) {
            return keys.toObject(PSqlResult.class).getTyped(env, 0);
        }

        return Memory.NULL;
    }

    @Signature
    public Memory getGeneratedKeys(Environment env) throws SQLException {
        ResultSet keys = statement.getGeneratedKeys();

        if (keys != null && keys.next()) {
            return ObjectMemory.valueOf(new PSqlResult(env, keys));
        }

        return Memory.NULL;
    }

    @Signature
    public void bindBlob(Environment env, int index, Memory arg) throws SQLException {
        InputStream is = Stream.getInputStream(env, arg);
        try {
            statement.setBlob(index + 1, is);
        } finally {
            Stream.closeStream(env, is);
        }
    }

    @Signature
    public void bindDate(Environment env, int index, WrapTime time) throws SQLException {
        statement.setDate(index + 1, new Date(time.getDate().getTime()), time.getCalendar());
    }

    @Signature
    public void bindTime(Environment env, int index, WrapTime time) throws SQLException {
        statement.setTime(index + 1, new Time(time.getDate().getTime()), time.getCalendar());
    }

    @Signature
    public void bindTimestamp(Environment env, int index, Memory value) throws SQLException {
        if (value.instanceOf(WrapTime.class)) {
            WrapTime time = value.toObject(WrapTime.class);
            statement.setTimestamp(index + 1, new Timestamp(time.getDate().getTime()), time.getCalendar());
        } else {
            statement.setTimestamp(index + 1, new Timestamp(value.toLong()));
        }
    }

    @Signature
    public void bind(Environment env, int index, Memory value) throws SQLException {
        if (value.instanceOf(WrapTime.class)) {
            WrapTime time = value.toObject(WrapTime.class);
            statement.setDate(index + 1, new Date(time.getDate().getTime()), time.getCalendar());
        } else if (value.instanceOf(Stream.class)) {
            statement.setBlob(index + 1, Stream.getInputStream(env, value));
        } else if (value.toValue() instanceof BinaryMemory) {
            statement.setBytes(index + 1, value.getBinaryBytes(env.getDefaultCharset()));
        } else {
            if (value.isNull()) {
                statement.setNull(index + 1, Types.NULL);
            } else {
                switch (value.getRealType()) {
                    case INT:
                        statement.setLong(index + 1, value.toLong());
                        break;

                    case DOUBLE:
                        statement.setDouble(index + 1, value.toDouble());
                        break;

                    case BOOL:
                        statement.setBoolean(index + 1, value.toBoolean());
                        break;

                    default:
                        statement.setString(index + 1, value.toString());
                }
            }
        }
    }

    @Override
    public Memory current(Environment env, Memory... args) {
        return resultSet == null ? Memory.NULL : ObjectMemory.valueOf(new PSqlResult(env, resultSet));
    }

    @Override
    public Memory key(Environment env, Memory... args) {
        try {
            return resultSet != null ? LongMemory.valueOf(resultSet.getRow() - 1) : Memory.NULL;
        } catch (SQLException e) {
            throw new WrapSqlException(env, e);
        }
    }

    @Override
    public Memory next(Environment env, Memory... args) {
        try {
            if (resultSet == null) {
                valid = false;
            } else {
                valid = resultSet.next();
            }
        } catch (SQLException e) {
            throw new WrapSqlException(env, e);
        }

        return Memory.NULL;
    }

    @Override
    public Memory rewind(Environment env, Memory... args) {
        try {
            resultSet = statement.executeQuery();

            if (resultSet != null) {
                valid = resultSet.next();
            } else {
                valid = false;
            }
        } catch (SQLException e) {
            throw new WrapSqlException(env, e);
        }

        return Memory.NULL;
    }

    @Override
    public Memory valid(Environment env, Memory... args) {
        return valid && resultSet != null ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }
}
