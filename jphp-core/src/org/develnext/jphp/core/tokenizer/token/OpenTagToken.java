package org.develnext.jphp.core.tokenizer.token;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class OpenTagToken extends Token {
    public OpenTagToken(TokenMeta meta) {
        super(meta, TokenType.T_OPEN_TAG);
    }
}
