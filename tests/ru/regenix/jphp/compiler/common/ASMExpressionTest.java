package ru.regenix.jphp.compiler.common;

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
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.File;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ASMExpressionTest {

    protected Environment environment = new Environment();
    protected Context context = new Context(environment, new File("test.php"));

    private ASMExpression getASMExpression(String expr){
        Tokenizer tokenizer = new Tokenizer(context, expr + ";");
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().get(0) instanceof ExprStmtToken);

        return new ASMExpression(
                context,
                (ExprStmtToken)analyzer.getTree().get(0)
        );
    }

    @Test
    public void testSimple(){
        ExprStmtToken expression = getASMExpression("1 + 2").getResult();
        Assert.assertEquals(3, expression.getTokens().size());
        Assert.assertEquals("12+", expression.getWord());
    }

    @Test
    public void testPriorities(){
        Assert.assertEquals("123/+", getASMExpression("1 + 2 / 3").getResult().getWord());
        Assert.assertEquals("123*+", getASMExpression("1 + 2 * 3").getResult().getWord());
        Assert.assertEquals("123%+", getASMExpression("1 + 2 % 3").getResult().getWord());
        Assert.assertEquals("12*3/", getASMExpression("1 * 2 / 3").getResult().getWord());
        Assert.assertEquals("12/3%", getASMExpression("1 / 2 % 3").getResult().getWord());
        Assert.assertEquals("12/3/", getASMExpression("1 / 2 / 3").getResult().getWord());
        Assert.assertEquals("12-3+", getASMExpression("1 - 2 + 3").getResult().getWord());
        Assert.assertEquals("123+&&", getASMExpression("1 && 2 + 3").getResult().getWord());
        Assert.assertEquals("123+||", getASMExpression("1 || 2 + 3").getResult().getWord());
        Assert.assertEquals("123&&||", getASMExpression("1 || 2 && 3").getResult().getWord());
        Assert.assertEquals("1!23&&||", getASMExpression("!1 || 2 && 3").getResult().getWord());
    }

    @Test
    public void testWithBraces(){
        ExprStmtToken expression = getASMExpression("(1 + 2) * 3").getResult();
        Assert.assertEquals(5, expression.getTokens().size());
        Assert.assertEquals("12+3*", expression.getWord());
    }

    @Test(expected = ParseException.class)
    public void testWithBracesInvalid(){
        getASMExpression("1 + 2)");
    }

    @Test(expected = ParseException.class)
    public void testInvalidBraces(){
        getASMExpression("[10]");
    }

    @Test(expected = ParseException.class)
    public void testInvalidBraces2(){
        getASMExpression("{x}");
    }
}
