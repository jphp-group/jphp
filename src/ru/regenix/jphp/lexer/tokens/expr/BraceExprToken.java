package ru.regenix.jphp.lexer.tokens.expr;

import com.sun.javaws.exceptions.InvalidArgumentException;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class BraceExprToken extends ExprToken {

    public enum Type { SIMPLE, ARRAY, BLOCK }

    protected Type type;
    protected boolean closed;

    public BraceExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BRACE);
        switch (meta.getWord().charAt(0)){
            case '{': type = Type.BLOCK; break;
            case '[': type = Type.ARRAY; break;
            case '(': type = Type.SIMPLE; break;
            case '}': type = Type.BLOCK; closed = true; break;
            case ']': type = Type.ARRAY; closed = true; break;
            case ')': type = Type.SIMPLE; closed = true; break;
            default:
                throw new IllegalArgumentException("Invalid " + meta.getWord() + " word for BraceExprToken");
        }
    }
}
