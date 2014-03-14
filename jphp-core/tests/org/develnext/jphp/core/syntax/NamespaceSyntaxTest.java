package org.develnext.jphp.core.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceStmtToken;

import java.util.List;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NamespaceSyntaxTest extends AbstractSyntaxTestCase {

    @Test
    public void testSimple(){
        List<Token> tree = getSyntaxTree("namespace; 123;");
        Assert.assertTrue(tree.size() == 2);
        Assert.assertTrue(tree.get(0) instanceof NamespaceStmtToken);
        Assert.assertTrue(tree.get(1) instanceof ExprStmtToken);

        NamespaceStmtToken token = (NamespaceStmtToken)tree.get(0);
        Assert.assertNull(token.getTree());
        Assert.assertNull(token.getName());
    }

    @Test
    public void testLongName(){
        List<Token> tree = getSyntaxTree("namespace A\\B\\C;");
        Assert.assertTrue(tree.size() == 1);
        Assert.assertTrue(tree.get(0) instanceof NamespaceStmtToken);

        NamespaceStmtToken token = (NamespaceStmtToken)tree.get(0);
        Assert.assertNull(token.getTree());
        Assert.assertEquals("A\\B\\C", token.getName().toName());
    }

    @Test
    public void testMultiple(){
        List<Token> tree = getSyntaxTree("namespace A { 123; } namespace B { 321; }");
        Assert.assertTrue(tree.size() == 4);

        NamespaceStmtToken token = (NamespaceStmtToken)tree.get(0);
        Assert.assertNotNull(token.getName());
        Assert.assertEquals("A", token.getName().toName());
        Assert.assertNull(token.getTree());

        token = (NamespaceStmtToken)tree.get(2);
        Assert.assertNotNull(token.getName());
        Assert.assertEquals("B", token.getName().toName());
        Assert.assertNull(token.getTree());
    }
}
