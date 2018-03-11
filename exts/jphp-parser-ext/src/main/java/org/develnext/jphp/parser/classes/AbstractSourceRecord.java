package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.Information;
import php.runtime.annotation.Reflection.*;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.util.Arrays;

@Namespace(ParserExtension.NS)
abstract public class AbstractSourceRecord<T extends Token> extends BaseObject {
    protected String name;
    protected String comment;
    protected T token;

    public AbstractSourceRecord(Environment env) {
        super(env);
    }

    public AbstractSourceRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public T getToken() {
        return token;
    }

    public void setToken(T token) {
        this.token = token;
    }

    @Getter
    public String getIdName() {
        return name == null ? null : name.toLowerCase();
    }

    @Getter
    public String getName() {
        return name;
    }

    @Setter
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Setter
    public void setNamespace(String namespace) {
        String[] tmp = StringUtils.split(getName(), Information.NAMESPACE_SEP_CHAR);

        switch (tmp.length) {
            case 0:
                setName(namespace + Information.NAMESPACE_SEP_CHAR);
                break;
            case 1:
                setName(namespace + Information.NAMESPACE_SEP_CHAR + getName());
                break;
            default:
                setName(namespace + Information.NAMESPACE_SEP_CHAR + getShortName());
                break;
        }
    }

    @Getter
    public String getNamespace() {
        String[] tmp = StringUtils.split(getName(), Information.NAMESPACE_SEP_CHAR);

        if (tmp.length > 1) {
            return StringUtils.join(Arrays.copyOf(tmp, tmp.length - 1), Information.NAMESPACE_SEP);
        }

        return null;
    }

    @Setter
    public void setShortName(String name) {
        String[] tmp = StringUtils.split(this.getName(), Information.NAMESPACE_SEP_CHAR);

        if (tmp.length > 0) {
            tmp[tmp.length - 1] = name;
            setName(StringUtils.join(tmp, Information.NAMESPACE_SEP));
        } else {
            setName(name);
        }
    }

    @Getter
    public String getShortName() {
        String[] tmp = StringUtils.split(getName(), Information.NAMESPACE_SEP_CHAR);

        if (tmp.length > 0) {
            return tmp[tmp.length - 1];
        }

        return null;
    }

    @Getter
    public String getComment() {
        return comment;
    }

    @Setter
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractSourceRecord)) return false;

        AbstractSourceRecord that = (AbstractSourceRecord) o;

        return !(name != null ? !name.equalsIgnoreCase(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.toLowerCase().hashCode() : 0;
    }

    @Signature
    public void clear() {
        this.name = null;
    }
}
