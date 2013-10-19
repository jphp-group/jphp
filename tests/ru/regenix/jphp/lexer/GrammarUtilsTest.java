package ru.regenix.jphp.lexer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.regenix.jphp.lexer.tokens.expr.StringExprToken;


@RunWith(JUnit4.class)
public class GrammarUtilsTest {
    @Test
    public void testIsSpace() throws Exception {
        Assert.assertTrue(GrammarUtils.isSpace(' '));
        Assert.assertTrue(GrammarUtils.isSpace('\t'));
        Assert.assertTrue(GrammarUtils.isSpace('\r'));
        Assert.assertTrue(GrammarUtils.isSpace('\n'));
    }

    @Test
    public void testIsQuote() throws Exception {
        Assert.assertEquals(StringExprToken.Quote.DOUBLE, GrammarUtils.isQuote('"'));
        Assert.assertEquals(StringExprToken.Quote.SINGLE, GrammarUtils.isQuote('\''));
        Assert.assertEquals(StringExprToken.Quote.SHELL, GrammarUtils.isQuote('`'));
    }

    @Test
    public void testIsBackslash() throws Exception {
        Assert.assertTrue(GrammarUtils.isBackslash('\\'));
    }

    @Test
    public void testIsNewline() throws Exception {
        Assert.assertTrue(GrammarUtils.isNewline('\n'));
        Assert.assertTrue(!GrammarUtils.isNewline('\r'));
    }
}
