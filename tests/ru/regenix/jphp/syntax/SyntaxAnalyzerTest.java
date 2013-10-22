package ru.regenix.jphp.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.io.File;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SyntaxAnalyzerTest {

    private Context context = new Context(new Environment(), new File("test.php"));

    @Test
    public void testSimple(){
        Tokenizer tokenizer = new Tokenizer(context, "foobar;");
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().listIterator().next() instanceof ExprStmtToken);
    }

    @Test(expected = ParseException.class)
    public void testUnexpectedEnd(){
        Tokenizer tokenizer = new Tokenizer(context, "foobar");
        new SyntaxAnalyzer(tokenizer);
    }
}
