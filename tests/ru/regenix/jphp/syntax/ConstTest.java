package ru.regenix.jphp.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.value.IntegerExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ConstStmtToken;

import java.util.List;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstTest extends AbstractSyntaxTestCase {

    @Test
    public void testSimple(){
        List<Token> tree = getSyntaxTree("const my_CONST = 1;");
        Assert.assertTrue(tree.size() == 1);
        Assert.assertTrue(tree.get(0) instanceof ConstStmtToken);

        ConstStmtToken constant = (ConstStmtToken)tree.get(0);

        Assert.assertEquals("my_CONST", constant.getName().getName());
        Assert.assertNull(constant.getClazz());
        Assert.assertNotNull(constant.getValue());
        Assert.assertTrue(constant.getValue().getTokens().size() == 1);
        Assert.assertTrue(constant.getValue().getTokens().get(0) instanceof IntegerExprToken);
    }

    @Test(expected = ParseException.class)
    public void testInvalid(){
        getSyntaxTree("const my_CONST = 22}");
    }

    @Test(expected = ParseException.class)
    public void testInvalid2(){
        getSyntaxTree("const my_CONST = ;");
    }
}
