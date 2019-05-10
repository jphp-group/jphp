package org.develnext.jphp.assertj;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractObjectAssert;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;

public class TokenizerAssert extends AbstractObjectAssert<TokenizerAssert, Tokenizer> {
    TokenizerAssert(Tokenizer tokenizer) {
        super(tokenizer, TokenizerAssert.class);
    }

    public TokenizerAssert nextIsInstanceOf(Class<? extends Token> cls) {
        assertThat(actual.nextToken()).isInstanceOf(cls);
        return this;
    }

    public TokenizerAssert nextIsStringToken(String expectedValue) {
        Token actual = this.actual.nextToken();
        isStringTokenWithValue(expectedValue, actual);

        return this;
    }

    private void isStringTokenWithValue(String expectedValue, Token actual) {
        assertThat(actual)
                .isInstanceOf(StringExprToken.class)
                .extracting(Token::getWord)
                .isEqualTo(expectedValue);
    }

    public TokenizerAssert nextIsStringToken(String expectedValue, StringExprToken.Quote quote) {
        Token actual = this.actual.nextToken();
        isStringTokenWithValue(expectedValue, actual);
        assertThat(((StringExprToken) actual).getQuote()).isEqualTo(quote);
        return this;
    }
}
