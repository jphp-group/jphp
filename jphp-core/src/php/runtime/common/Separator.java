package php.runtime.common;


import org.develnext.jphp.core.tokenizer.token.ColonToken;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
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
            case SEMICOLON: return new Class[] {CommaToken.class};
            case COMMA: return new Class[] {CommaToken.class};
            case COMMA_OR_SEMICOLON: return new Class[] {CommaToken.class, SemicolonToken.class};
            case AS: return new Class[] {AsStmtToken.class};
            default:
                throw new IllegalStateException("Cannot get token class");
        }
    }
}
