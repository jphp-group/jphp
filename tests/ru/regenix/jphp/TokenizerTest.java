package ru.regenix.jphp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.regenix.jphp.lexer.Tokenizer;

@RunWith(JUnit4.class)
public class TokenizerTest {

    @Test
    public void testSimple(){
        Tokenizer tokenizer = new Tokenizer("");

        assertNull(tokenizer.nextToken());
        assertEquals("", tokenizer.getCode());

        tokenizer = new Tokenizer(" ");
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer("  ");
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer("\t");
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer("\n");
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer("\r");
        assertNull(tokenizer.nextToken());
    }
}
