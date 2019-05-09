package org.develnext.jphp.assertj;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.develnext.jphp.core.tokenizer.Tokenizer;

import php.runtime.env.Context;

public final class CustomAssertions {
    private CustomAssertions() {
        throw new AssertionError("No instances!");
    }

    public static TokenizerAssert assertThat(String content) {
        try {
            return new TokenizerAssert(new Tokenizer(new Context(content)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
