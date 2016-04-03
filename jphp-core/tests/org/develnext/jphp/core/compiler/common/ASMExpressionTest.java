package org.develnext.jphp.core.compiler.common;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.expr.operator.LogicOperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.MulExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.CallExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.IntegerExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.common.LangMode;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorException;

import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ASMExpressionTest {

    protected Environment environment = new Environment();
    protected Context context;

    private ASMExpression getASMExpression(String expr){
        environment.scope.setLangMode(LangMode.DEFAULT);

        Tokenizer tokenizer = null;
        try {
            tokenizer = new Tokenizer(context = new Context(expr + ";"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().get(0) instanceof ExprStmtToken);

        return new ASMExpression(
                null,
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

        ExprStmtToken expr = getASMExpression("1 && 2 + 3").getResult();
        Assert.assertEquals("1&&", expr.getWord());
        Assert.assertTrue(expr.getTokens().get(1) instanceof LogicOperatorExprToken);
        Assert.assertEquals("2+3", ((LogicOperatorExprToken)expr.getTokens().get(1)).getRightValue().getWord());

        Assert.assertEquals("1||", getASMExpression("1 || 2 + 3").getResult().getWord());
        Assert.assertEquals("1||", getASMExpression("1 || 2 && 3").getResult().getWord());
        Assert.assertEquals("1!||", getASMExpression("!1 || 2 && 3").getResult().getWord());
    }

    @Test
    public void testWithBraces(){
        ExprStmtToken expression = getASMExpression("(1 + 2) * 3").getResult();
        Assert.assertEquals(5, expression.getTokens().size());
        Assert.assertEquals("12+3*", expression.getWord());
    }

    @Test
    public void testCallExpr(){
        ExprStmtToken expression = getASMExpression("func(1 + 2, 3) * 3").getResult();
        Assert.assertEquals(3, expression.getTokens().size());

        Assert.assertTrue(expression.getTokens().get(0) instanceof CallExprToken);
        CallExprToken call = (CallExprToken)expression.getTokens().get(0);
        Assert.assertEquals(2, call.getParameters().size());
        Assert.assertEquals("1+2", call.getParameters().get(0).getWord());
        Assert.assertEquals("3", call.getParameters().get(1).getWord());

        Assert.assertTrue(expression.getTokens().get(1) instanceof IntegerExprToken);
        Assert.assertTrue(expression.getTokens().get(2) instanceof MulExprToken);
    }

    @Test(expected = ErrorException.class)
    public void testWithBracesInvalid(){
        getASMExpression("1 + 2)");
    }

    @Test
    public void testInvalidBraces(){
        getASMExpression("[10]");
    }

    @Test(expected = ErrorException.class)
    public void testInvalidBraces2(){
        getASMExpression("{x}");
    }
}
