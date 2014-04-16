package org.develnext.jphp.core.syntax;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.exceptions.support.ErrorException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnexpectedSyntaxTest extends AbstractSyntaxTestCase {

    @Test(expected = ErrorException.class)
    public void test1(){
        getSyntaxTree("$d =");
    }

    @Test
    public void test2(){
        getSyntaxTree("$d =;"); // it's checks on operators in compiler
    }

    @Test(expected = ErrorException.class)
    public void testBraces1(){
        getSyntaxTree("(3 + 4");
    }

    @Test(expected = ErrorException.class)
    public void testBraces11(){
        getSyntaxTree("(cos($i) + 4");
    }


    @Test(expected = ErrorException.class)
    public void testBraces2(){
        getSyntaxTree("(3 + 4))");
    }

    @Test(expected = ErrorException.class)
    public void testBraces3(){
        getSyntaxTree("[3 + 4");
    }

    @Test(expected = ErrorException.class)
    public void testBraces4(){
        getSyntaxTree("[3 + 4]];");
    }

    @Test
    public void testBraces5(){
        getSyntaxTree("$x[30][[30]];");
    }

    @Test
    public void testAmpersandRef1(){
        getSyntaxTree("$x = &$var;");
    }

    @Test
    public void testAmpersandRef2(){
        getSyntaxTree("$x = &call();");
    }

    @Test(expected = ErrorException.class)
    public void testAmpersandRefEmpty(){
        getSyntaxTree("$x = &;");
    }

    @Test(expected = ErrorException.class)
    public void testBlock(){
        getSyntaxTree("{ $x = 22; ");
    }

    @Test
    public void testBlock2(){
        getSyntaxTree("{ $x = 20; }");
    }

    @Test(expected = ErrorException.class)
    public void testIf(){
        getSyntaxTree("if(){ }");
    }

    @Test(expected = ErrorException.class)
    public void testIf2(){
        getSyntaxTree("if(true) ");
    }

    @Test
    public void testIf3(){
        getSyntaxTree("if(true);");
    }

    @Test
    public void testGlobals(){
        getSyntaxTree("global $x, $y, $z;");
    }

    @Test(expected = ErrorException.class)
    public void testStaticsInvalid(){
        getSyntaxTree("static $x $y;");
    }

    @Test
    public void testStatics(){
        getSyntaxTree("static $x = 100500, $y, $z = 'foobar', $a, $b;");
    }
}
