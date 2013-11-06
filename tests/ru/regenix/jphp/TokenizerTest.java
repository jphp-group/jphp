package ru.regenix.jphp;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.env.TraceInfo;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.SemicolonToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.*;
import ru.regenix.jphp.lexer.tokens.expr.operator.*;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.macro.*;
import ru.regenix.jphp.lexer.tokens.stmt.*;

import java.math.BigInteger;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenizerTest {

    private Environment environment = new Environment();

    @Test
    public void testSimple(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, ""));

        assertNull(tokenizer.nextToken());
        assertEquals("", tokenizer.getCode());

        tokenizer = new Tokenizer(new Context(environment, " "));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context(environment, "  "));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context(environment, "\t"));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context(environment, "\n"));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context(environment, "\r"));
        assertNull(tokenizer.nextToken());
    }

    @Test
    public void testScalarTokens(){
        Token token;
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "10 3.3 'foo' '' `bar` \"xyz\" 0xCC true false"));

        token = tokenizer.nextToken();
        assertTrue(token instanceof IntegerExprToken);
        assertEquals(10L, ((IntegerExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof DoubleExprToken);
        assertEquals(3.3, ((DoubleExprToken) token).getValue(), 0.01);

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.SINGLE, ((StringExprToken) token).getQuote());
        assertEquals("foo", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.SINGLE, ((StringExprToken) token).getQuote());
        assertEquals("", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.SHELL, ((StringExprToken) token).getQuote());
        assertEquals("bar", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.DOUBLE, ((StringExprToken) token).getQuote());
        assertEquals("xyz", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof HexExprValue);
        assertEquals(new BigInteger("CC", 16).longValue(), ((HexExprValue) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof BooleanExprToken);
        assertEquals(true, ((BooleanExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof BooleanExprToken);
        assertEquals(false, ((BooleanExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertNull(token);
    }

    @Test
    public void testStringSlashes(){
        Token token;
        Tokenizer tokenizer = new Tokenizer(new Context(environment, " 'foo\\'bar' \"foo\\\"bar\" `foo\\`bar`"));

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals("foo\\'bar", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals("foo\\\"bar", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals("foo\\`bar", ((StringExprToken) token).getValue());
    }

    @Test
    public void testComplexOperators(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "== >= <= === !== != && ||"));

        assertTrue(tokenizer.nextToken() instanceof EqualExprToken);
        assertTrue(tokenizer.nextToken() instanceof GreaterOrEqualExprToken);
        assertTrue(tokenizer.nextToken() instanceof SmallerOrEqualToken);
        assertTrue(tokenizer.nextToken() instanceof IdenticalExprToken);
        assertTrue(tokenizer.nextToken() instanceof NotIdenticalExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanNotEqualExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanAndExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanOrExprToken);
    }

    @Test
    public void testSimpleOperators(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "= + - / * % . and or new && || ! xor"));

        assertTrue(tokenizer.nextToken() instanceof AssignExprToken);
        assertTrue(tokenizer.nextToken() instanceof PlusExprToken);
        assertTrue(tokenizer.nextToken() instanceof MinusExprToken);
        assertTrue(tokenizer.nextToken() instanceof DivExprToken);
        assertTrue(tokenizer.nextToken() instanceof MulExprToken);
        assertTrue(tokenizer.nextToken() instanceof ModExprToken);
        assertTrue(tokenizer.nextToken() instanceof ConcatExprToken);

        assertTrue(tokenizer.nextToken() instanceof BooleanAnd2ExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanOr2ExprToken);
        assertTrue(tokenizer.nextToken() instanceof NewExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanAndExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanOrExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanNotExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanXorExprToken);
    }

    @Test
    public void testParseError(){
        Throwable ex = null;
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "  'foobar \n "));

        try {
            tokenizer.nextToken();
        } catch (Throwable e){
            ex = e;
        }

        assertTrue(ex instanceof ParseException);
        TraceInfo traceInfo = ((ParseException) ex).getTraceInfo();
        assertNotNull(traceInfo);
        assertNull(traceInfo.getContext().getFile());
        assertEquals(0, traceInfo.getStartLine());
        assertEquals(1, traceInfo.getEndLine());
        assertEquals(0, traceInfo.getStartPosition());
        assertEquals(2, traceInfo.getEndPosition());
    }

    @Test
    public void testComplex(){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, "0==10==='30';"));

        assertTrue(tokenizer.nextToken() instanceof IntegerExprToken);
        assertTrue(tokenizer.nextToken() instanceof EqualExprToken);
        assertTrue(tokenizer.nextToken() instanceof IntegerExprToken);
        assertTrue(tokenizer.nextToken() instanceof IdenticalExprToken);
        assertTrue(tokenizer.nextToken() instanceof StringExprToken);
        assertTrue(tokenizer.nextToken() instanceof SemicolonToken);

        tokenizer = new Tokenizer(environment.createContext("F =; 20;"));

        Token token;
        assertTrue((token = tokenizer.nextToken()) instanceof NameToken);
        assertEquals("F", ((NameToken)token).getName());
        assertTrue(tokenizer.nextToken() instanceof AssignExprToken);
        assertTrue(tokenizer.nextToken() instanceof SemicolonToken);
        assertTrue(tokenizer.nextToken() instanceof IntegerExprToken);
        assertTrue(tokenizer.nextToken() instanceof SemicolonToken);

        tokenizer = new Tokenizer(environment.createContext("123foobar"));
        token = tokenizer.nextToken();
        assertFalse(token instanceof IntegerExprToken);
        assertFalse(token instanceof NameToken);
        assertTrue(token.getClass() == Token.class);
        assertEquals("123foobar", token.getWord());

        assertNull(tokenizer.nextToken());
    }

    @Test
    public void testBraces(){
        Tokenizer tokenizer = new Tokenizer(environment.createContext(" :: ->foobar('a', 1, 3.0);"));

        assertTrue(tokenizer.nextToken() instanceof StaticAccessExprToken);
        assertTrue(tokenizer.nextToken() instanceof DynamicAccessExprToken);
        assertTrue(tokenizer.nextToken() instanceof NameToken);
        assertTrue(tokenizer.nextToken() instanceof BraceExprToken);
        assertTrue(tokenizer.nextToken() instanceof StringExprToken);
        assertTrue(tokenizer.nextToken() instanceof CommaToken);
        assertTrue(tokenizer.nextToken() instanceof IntegerExprToken);
        assertTrue(tokenizer.nextToken() instanceof CommaToken);
        assertTrue(tokenizer.nextToken() instanceof DoubleExprToken);
        assertTrue(tokenizer.nextToken() instanceof BraceExprToken);
        assertTrue(tokenizer.nextToken() instanceof SemicolonToken);
    }

    @Test
    public void testVarVar(){
        Tokenizer tokenizer = new Tokenizer(environment.createContext("$$foo $ $bar $$$foobar"));

        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);

        tokenizer = new Tokenizer(environment.createContext("${'foo;6bar'}"));
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof BraceExprToken);
        assertTrue(tokenizer.nextToken() instanceof StringExprToken);
        assertTrue(tokenizer.nextToken() instanceof BraceExprToken);
    }

    @Test
    public void testMacro(){
        Tokenizer tokenizer = new Tokenizer(
                environment.createContext("__LINE__ __FILE__ __DIR__ __METHOD__ __FUNCTION__ __CLASS__ __NAMESPACE__ __TRAIT__")
        );

        assertTrue(tokenizer.nextToken() instanceof LineMacroToken);
        assertTrue(tokenizer.nextToken() instanceof FileMacroToken);
        assertTrue(tokenizer.nextToken() instanceof DirMacroToken);
        assertTrue(tokenizer.nextToken() instanceof MethodMacroToken);
        assertTrue(tokenizer.nextToken() instanceof FunctionMacroToken);
        assertTrue(tokenizer.nextToken() instanceof ClassMacroToken);
        assertTrue(tokenizer.nextToken() instanceof NamespaceMacroToken);
        assertTrue(tokenizer.nextToken() instanceof TraitMacroToken);
    }

    @Test
    public void testStmt(){
        Tokenizer tokenizer = new Tokenizer(environment.createContext(
                "class function private public protected static final try catch for if foreach switch while " +
                "default return declare case do else elseif endif endfor endforeach endwhile endswitch " +
                "abstract use namespace finally extends implements global"
        ));

        assertTrue(tokenizer.nextToken() instanceof ClassStmtToken);
        assertTrue(tokenizer.nextToken() instanceof FunctionStmtToken);
        assertTrue(tokenizer.nextToken() instanceof PrivateStmtToken);
        assertTrue(tokenizer.nextToken() instanceof PublicStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ProtectedStmtToken);
        assertTrue(tokenizer.nextToken() instanceof StaticStmtToken);
        assertTrue(tokenizer.nextToken() instanceof FinalStmtToken);
        assertTrue(tokenizer.nextToken() instanceof TryStmtToken);
        assertTrue(tokenizer.nextToken() instanceof CatchStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ForStmtToken);
        assertTrue(tokenizer.nextToken() instanceof IfStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ForeachStmtToken);
        assertTrue(tokenizer.nextToken() instanceof SwitchStmtToken);
        assertTrue(tokenizer.nextToken() instanceof WhileStmtToken);
        assertTrue(tokenizer.nextToken() instanceof DefaultStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ReturnStmtToken);
        assertTrue(tokenizer.nextToken() instanceof DeclareStmtToken);
        assertTrue(tokenizer.nextToken() instanceof CaseStmtToken);
        assertTrue(tokenizer.nextToken() instanceof DoStmtToken);

        assertTrue(tokenizer.nextToken() instanceof ElseStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ElseIfStmtToken);

        assertTrue(tokenizer.nextToken() instanceof EndifStmtToken);
        assertTrue(tokenizer.nextToken() instanceof EndforStmtToken);
        assertTrue(tokenizer.nextToken() instanceof EndforeachStmtToken);
        assertTrue(tokenizer.nextToken() instanceof EndwhileStmtToken);
        assertTrue(tokenizer.nextToken() instanceof EndswitchStmtToken);

        assertTrue(tokenizer.nextToken() instanceof AbstractStmtToken);
        assertTrue(tokenizer.nextToken() instanceof NamespaceUseStmtToken);
        assertTrue(tokenizer.nextToken() instanceof NamespaceStmtToken);
        assertTrue(tokenizer.nextToken() instanceof FinallyStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ExtendsStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ImplementsStmtToken);
        assertTrue(tokenizer.nextToken() instanceof GlobalStmtToken);
    }
}
