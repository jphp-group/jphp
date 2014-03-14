package org.develnext.jphp.core.syntax;


import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.stmt.ArgumentStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NamedFunctionTest extends AbstractSyntaxTestCase {

    private Environment environment = new Environment();

    @Test
    public void testSimple() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("function myFunc($x, &$y, $z = 33){  } $x = 10;"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

        ListIterator<Token> iterator = analyzer.getTree().listIterator();
        Token token;

        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue((token = iterator.next()) instanceof FunctionStmtToken);

        FunctionStmtToken func = (FunctionStmtToken)token;

        Assert.assertFalse(func.isReturnReference());
        List<ArgumentStmtToken> arguments = func.getArguments();

        Assert.assertTrue(arguments != null && arguments.size() == 3);
        Assert.assertFalse(arguments.get(0).isReference());
        Assert.assertTrue(arguments.get(1).isReference());
        Assert.assertNotNull(arguments.get(2).getValue());

        Assert.assertEquals("myFunc", func.getName().getName());
        Assert.assertNotNull(func.getBody());
        Assert.assertFalse(func.isInterfacable());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() instanceof ExprStmtToken);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNoArguments() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("function myFunc(){}"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().get(0) instanceof FunctionStmtToken);
        FunctionStmtToken func = (FunctionStmtToken)analyzer.getTree().listIterator().next();
        Assert.assertTrue(func.getArguments().size() == 0);
    }

    @Test
    public void testInterfacable() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("function myFunc();"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().get(0) instanceof FunctionStmtToken);
        FunctionStmtToken func = (FunctionStmtToken)analyzer.getTree().listIterator().next();
        Assert.assertTrue(func.isInterfacable());
    }
}
