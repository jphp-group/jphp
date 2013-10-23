package ru.regenix.jphp.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.stmt.NamespaceStmtToken;

import java.util.List;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NamespaceSyntaxTest extends AbstractSyntaxTestCase {

    @Test
    public void testSimple(){
        List<Token> tree = getSyntaxTree("namespace; 123;");
        Assert.assertTrue(tree.size() == 1);
        Assert.assertTrue(tree.get(0) instanceof NamespaceStmtToken);

        NamespaceStmtToken token = (NamespaceStmtToken)tree.get(0);
        Assert.assertNotNull(token.getBody());
        Assert.assertNull(token.getName());
    }

    @Test
    public void testLongName(){
        List<Token> tree = getSyntaxTree("namespace A\\B\\C;");
        Assert.assertTrue(tree.size() == 1);
        Assert.assertTrue(tree.get(0) instanceof NamespaceStmtToken);

        NamespaceStmtToken token = (NamespaceStmtToken)tree.get(0);
        Assert.assertNull(token.getBody());
        Assert.assertEquals("A\\B\\C", token.getName().toName());
    }

    @Test
    public void testMultiple(){
        List<Token> tree = getSyntaxTree("namespace A { 123; } namespace B { 321; }");
        Assert.assertTrue(tree.size() == 2);

        NamespaceStmtToken token = (NamespaceStmtToken)tree.get(0);
        Assert.assertNotNull(token.getName());
        Assert.assertEquals("A", token.getName().toName());
        Assert.assertNotNull(token.getBody());

        token = (NamespaceStmtToken)tree.get(1);
        Assert.assertNotNull(token.getName());
        Assert.assertEquals("B", token.getName().toName());
        Assert.assertNotNull(token.getBody());
    }
}
