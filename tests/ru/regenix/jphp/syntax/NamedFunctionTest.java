package ru.regenix.jphp.syntax;


import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.stmt.ArgumentStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.FunctionStmtToken;

import java.util.List;
import java.util.ListIterator;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NamedFunctionTest extends AbstractSyntaxTestCase {

    private Environment environment = new Environment();

    @Test
    public void testSimple(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "function myFunc($x, &$y, $z = 33){  } $x = 10;"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

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
    public void testNoArguments(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "function myFunc(){}"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().get(0) instanceof FunctionStmtToken);
        FunctionStmtToken func = (FunctionStmtToken)analyzer.getTree().listIterator().next();
        Assert.assertTrue(func.getArguments().size() == 0);
    }

    @Test
    public void testInterfacable(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "function myFunc();"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().get(0) instanceof FunctionStmtToken);
        FunctionStmtToken func = (FunctionStmtToken)analyzer.getTree().listIterator().next();
        Assert.assertTrue(func.isInterfacable());
    }
}
