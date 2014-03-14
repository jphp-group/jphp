package org.develnext.jphp.core.tokenizer.token;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

/**
 * :
 */
public class ColonToken extends Token {
    public ColonToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
