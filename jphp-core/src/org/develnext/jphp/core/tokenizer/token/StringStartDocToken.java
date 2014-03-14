package org.develnext.jphp.core.tokenizer.token;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class StringStartDocToken extends Token {
    public StringStartDocToken(TokenMeta meta) {
        super(meta, TokenType.T_START_HEREDOC);
    }
}
