package ru.regenix.jphp.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.exceptions.support.ErrorException;
import php.runtime.env.Context;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SyntaxAnalyzerTest extends AbstractSyntaxTestCase {

    @Test
    public void testSimple() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("foobar;"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().listIterator().next() instanceof ExprStmtToken);
    }

    @Test(expected = ErrorException.class)
    public void testUnexpectedEnd() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("foobar"));
        new SyntaxAnalyzer(environment, tokenizer);
    }
}
