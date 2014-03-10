package ru.regenix.jphp.tokenizer.token;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.regenix.jphp.tokenizer.TokenMeta;

@RunWith(JUnit4.class)
public class TokenMetaTest {
    @Test
    public void testSimple() throws Exception {
        TokenMeta meta = new TokenMeta("foobar", 1, 2, 3, 4);
        Assert.assertEquals("foobar", meta.getWord());
        Assert.assertEquals(1, meta.getStartLine());
        Assert.assertEquals(2, meta.getEndLine());
        Assert.assertEquals(3, meta.getStartPosition());
        Assert.assertEquals(4, meta.getEndPosition());
    }
}
