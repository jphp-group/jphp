package ru.regenix.jphp.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.CallExprToken;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.io.File;
import java.util.List;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleExprTest {

    private Context context = new Context(new Environment(), new File("test.php"));

    @Test
    public void testSimpleCall(){
        Tokenizer tokenizer = new Tokenizer(context, "myCall(1 * 2, func(3, 2), 4);");
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        List<Token> tokens = analyzer.getTree();
        Assert.assertTrue(tokens.size() == 1);
        Assert.assertTrue(tokens.get(0) instanceof ExprStmtToken);

        ExprStmtToken expr = (ExprStmtToken)tokens.get(0);
        tokens = expr.getTokens();

        Assert.assertTrue(tokens.size() == 1);
        Assert.assertTrue(tokens.get(0) instanceof CallExprToken);

        CallExprToken call = (CallExprToken) expr.getTokens().get(0);
        Assert.assertTrue(call.getName() instanceof NameToken);
        Assert.assertEquals("myCall", ((NameToken) call.getName()).getName());
        Assert.assertTrue(call.getParameters().size() == 3);

        Assert.assertTrue(call.getParameters().get(0).getTokens().size() == 3);
        Assert.assertTrue(call.getParameters().get(2).getTokens().size() == 1);

        ExprStmtToken param = call.getParameters().get(1);
        Assert.assertTrue(param.getTokens().size() == 1);
        Assert.assertTrue(param.getTokens().get(0) instanceof CallExprToken);

        CallExprToken subCall = (CallExprToken)param.getTokens().get(0);
        Assert.assertTrue(subCall.getParameters().size() == 2);
    }
}
