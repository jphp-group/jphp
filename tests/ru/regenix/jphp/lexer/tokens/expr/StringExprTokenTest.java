package ru.regenix.jphp.lexer.tokens.expr;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

@RunWith(JUnit4.class)
public class StringExprTokenTest {

    @Test
    public void testSimple(){
        TokenMeta meta = new TokenMeta("foobar", 0, 0, 0, 0);

        StringExprToken token = new StringExprToken(meta, StringExprToken.Quote.SINGLE);
        Assert.assertEquals("foobar", token.getValue());
    }
}
