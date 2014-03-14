package org.develnext.jphp.core.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.exceptions.support.ErrorException;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.value.IntegerExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ConstStmtToken;

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

        Assert.assertEquals("my_CONST", constant.items.get(0).getFulledName());
        Assert.assertNull(constant.getClazz());
        Assert.assertNotNull(constant.items.get(0).value);
        Assert.assertTrue(constant.items.get(0).value.getTokens().size() == 1);
        Assert.assertTrue(constant.items.get(0).value.getTokens().get(0) instanceof IntegerExprToken);
    }

    @Test(expected = ErrorException.class)
    public void testInvalid(){
        getSyntaxTree("const my_CONST = 22}");
    }

    @Test(expected = ErrorException.class)
    public void testInvalid2(){
        getSyntaxTree("const my_CONST = ;");
    }
}
