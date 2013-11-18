package ru.regenix.jphp.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SyntaxAnalyzerTest extends AbstractSyntaxTestCase {

    @Test
    public void testSimple(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "foobar;"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().listIterator().next() instanceof ExprStmtToken);
    }

    @Test(expected = ParseException.class)
    public void testUnexpectedEnd(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "foobar"));
        new SyntaxAnalyzer(tokenizer);
    }
}
