package org.develnext.jphp.core.tokenizer.token;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class CloseTagToken extends Token {
    public CloseTagToken(TokenMeta meta) {
        super(meta, TokenType.T_CLOSE_TAG);
    }
}
