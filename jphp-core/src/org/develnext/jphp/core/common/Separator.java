package org.develnext.jphp.core.common;


import org.develnext.jphp.core.tokenizer.token.ColonToken;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.CommaToken;
import org.develnext.jphp.core.tokenizer.token.stmt.AsStmtToken;

public enum Separator {
    COMMA,
    SEMICOLON,
    COLON,
    COMMA_OR_SEMICOLON,
    AS,
    ARRAY,
    ARRAY_BLOCK;

    public Class<Token>[] getTokenClass(){
        switch (this) {
            case COLON: return new Class[] {ColonToken.class};
            case SEMICOLON: return new Class[] {SemicolonToken.class};
            case COMMA: return new Class[] {CommaToken.class};
            case COMMA_OR_SEMICOLON: return new Class[] {CommaToken.class, SemicolonToken.class};
            case AS: return new Class[] {AsStmtToken.class};
            default:
                throw new IllegalStateException("Cannot get token class");
        }
    }

    public boolean is(Token token) {
        switch (this) {
            case COLON: return token instanceof ColonToken;
            case SEMICOLON: return token instanceof SemicolonToken;
            case COMMA: return token instanceof CommaToken;
            case COMMA_OR_SEMICOLON: return token instanceof CommaToken || token instanceof SemicolonToken;
            case AS: return token instanceof AsStmtToken;
            case ARRAY_BLOCK:
                if (token instanceof BraceExprToken && ((BraceExprToken) token).isBlock()) {
                    return true;
                } // to next check.
             case ARRAY: return token instanceof BraceExprToken && ((BraceExprToken) token).isArray();
        }
        return false;
    }
}
