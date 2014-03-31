package org.develnext.jphp.core.tokenizer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;


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

    @Test
    public void testVariable(){
        Assert.assertTrue(GrammarUtils.isVariableChar('$'));
    }

    @Test
    public void testMisc(){
        Assert.assertTrue(GrammarUtils.isFloatDot('.'));
        Assert.assertTrue(GrammarUtils.isNumeric('0'));
        Assert.assertTrue(GrammarUtils.isNumeric('1'));
        Assert.assertTrue(GrammarUtils.isNumeric('2'));
        Assert.assertTrue(GrammarUtils.isNumeric('3'));
        Assert.assertTrue(GrammarUtils.isNumeric('4'));
        Assert.assertTrue(GrammarUtils.isNumeric('5'));
        Assert.assertTrue(GrammarUtils.isNumeric('6'));
        Assert.assertTrue(GrammarUtils.isNumeric('7'));
        Assert.assertTrue(GrammarUtils.isNumeric('8'));
        Assert.assertTrue(GrammarUtils.isNumeric('9'));
    }
}
