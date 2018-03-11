package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.CommentToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.StmtToken;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Abstract
@Namespace(ParserExtension.NS)
public class SourceToken extends BaseObject {
    protected Token token;

    public SourceToken(Environment env, Token token) {
        super(env);
        this.token = token;
    }

    public SourceToken(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public boolean isNamedToken() {
        return token.isNamedToken();
    }

    @Signature
    public boolean isStatementToken() {
        return token instanceof StmtToken;
    }

    @Signature
    public boolean isExpressionToken() {
        return token instanceof ExprToken;
    }

    @Signature
    public boolean isOperatorToken() {
        return token instanceof OperatorExprToken;
    }

    @Signature
    public ArrayMemory getMeta() {
        ArrayMemory r = new ArrayMemory();

        if (token instanceof CommentToken) {
            r.refOfIndex("comment").assign(((CommentToken) token).getComment());
            r.refOfIndex("kind").assign(((CommentToken) token).getKind().name());
        }

        return r.toConstant();
    }

    @Getter
    public String getType() {
        String name = token.getClass().getSimpleName();

        if (name.endsWith("Token")) {
            name = name.substring(0, name.length() - 5);
        }

        return name;
    }

    @Getter
    public String getWord() {
        return token.getWord();
    }

    @Getter
    public int getLine() {
        return token.getMeta().getStartLine();
    }

    @Getter
    public int getPosition() {
        return token.getMeta().getStartPosition();
    }

    @Getter
    public int getEndLine() {
        return token.getMeta().getEndLine();
    }

    @Getter
    public int getEndPosition() {
        return token.getMeta().getEndPosition();
    }
}
