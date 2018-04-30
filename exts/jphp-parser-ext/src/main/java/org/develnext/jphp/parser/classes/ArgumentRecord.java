package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.stmt.ArgumentStmtToken;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(ParserExtension.NS)
public class ArgumentRecord extends AbstractSourceRecord<ArgumentStmtToken> {
    private String exprValue;
    private HintType hintType;
    private String hintTypeClass;
    private boolean variadic;
    private boolean optional;
    private boolean reference;

    public ArgumentRecord(Environment env, ArgumentStmtToken token) {
        super(env);

        setName(token.getName().getName());
        setHintType(token.getHintType());
        setVariadic(token.isVariadic());
        setOptional(token.isOptional());
        setReference(token.isReference());
        setHintTypeClass(token.getHintTypeClass() == null ? null : token.getHintTypeClass().getName());

        exprValue = token.getValue() == null ? null : token.getValue().getWord();

        setToken(token);
    }

    public ArgumentRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public String getExprValue() {
        return exprValue;
    }

    @Setter
    public void setExprValue(String exprValue) {
        this.exprValue = exprValue;
    }

    @Getter
    public HintType getHintType() {
        return hintType;
    }

    @Setter
    public void setHintType(HintType hintType) {
        this.hintType = hintType;
    }

    @Getter
    public boolean isVariadic() {
        return variadic;
    }

    @Setter
    public void setVariadic(boolean variadic) {
        this.variadic = variadic;
    }

    @Getter
    public boolean isOptional() {
        return optional;
    }

    @Setter
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    @Getter
    public boolean isReference() {
        return reference;
    }

    @Setter
    public void setReference(boolean reference) {
        this.reference = reference;
    }

    @Getter
    public String getHintTypeClass() {
        return hintTypeClass;
    }

    @Setter
    public void setHintTypeClass(String hintTypeClass) {
        this.hintTypeClass = hintTypeClass;
    }
}
