package org.develnext.jphp.core.tokenizer;

import org.develnext.jphp.core.common.TokenizeGrammarUtils;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class GrammarUtilsTest {
    @Test
    public void testIsSpace() throws Exception {
        Assert.assertTrue(TokenizeGrammarUtils.isSpace(' '));
        Assert.assertTrue(TokenizeGrammarUtils.isSpace('\t'));
        Assert.assertTrue(TokenizeGrammarUtils.isSpace('\r'));
        Assert.assertTrue(TokenizeGrammarUtils.isSpace('\n'));
    }

    @Test
    public void testIsQuote() throws Exception {
        Assert.assertEquals(StringExprToken.Quote.DOUBLE, TokenizeGrammarUtils.isQuote('"'));
        Assert.assertEquals(StringExprToken.Quote.SINGLE, TokenizeGrammarUtils.isQuote('\''));
        Assert.assertEquals(StringExprToken.Quote.SHELL, TokenizeGrammarUtils.isQuote('`'));
    }

    @Test
    public void testIsBackslash() throws Exception {
        Assert.assertTrue(TokenizeGrammarUtils.isBackslash('\\'));
    }

    @Test
    public void testIsNewline() throws Exception {
        Assert.assertTrue(TokenizeGrammarUtils.isNewline('\n'));
        Assert.assertTrue(!TokenizeGrammarUtils.isNewline('\r'));
    }

    @Test
    public void testVariable(){
        Assert.assertTrue(TokenizeGrammarUtils.isVariableChar('$'));
    }

    @Test
    public void testMisc(){
        Assert.assertTrue(TokenizeGrammarUtils.isFloatDot('.'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('0'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('1'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('2'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('3'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('4'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('5'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('6'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('7'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('8'));
        Assert.assertTrue(TokenizeGrammarUtils.isNumeric('9'));
    }
}
