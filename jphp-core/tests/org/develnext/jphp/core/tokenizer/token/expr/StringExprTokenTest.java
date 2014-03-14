package org.develnext.jphp.core.tokenizer.token.expr;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;

@RunWith(JUnit4.class)
public class StringExprTokenTest {

    @Test
    public void testSimple(){
        TokenMeta meta = new TokenMeta("foobar", 0, 0, 0, 0);

        StringExprToken token = new StringExprToken(meta, StringExprToken.Quote.SINGLE);
        Assert.assertEquals("foobar", token.getValue());
    }
}
