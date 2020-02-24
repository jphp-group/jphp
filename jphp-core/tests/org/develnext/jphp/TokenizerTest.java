package org.develnext.jphp;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.develnext.jphp.assertj.CustomAssertions.assertThat;
import static org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken.Quote.DOC;
import static org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken.Quote.DOUBLE;
import static org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken.Quote.SINGLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.CommentToken;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.CommaToken;
import org.develnext.jphp.core.tokenizer.token.expr.DollarExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.AssignExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.BooleanAnd2ExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.BooleanAndExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.BooleanNotEqualExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.BooleanNotExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.BooleanOr2ExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.BooleanOrExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.BooleanXorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.ConcatExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DivExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DynamicAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.EqualExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.GreaterOrEqualExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.IdenticalExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.MinusExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.ModExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.MulExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.NotIdenticalExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.PlusExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.PowExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.SmallerOrEqualToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.BooleanExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.DoubleExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.IntegerExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NewExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StaticAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StaticExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.ClassMacroToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.DirMacroToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.FileMacroToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.FunctionMacroToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.LineMacroToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.MethodMacroToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.NamespaceMacroToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.TraitMacroToken;
import org.develnext.jphp.core.tokenizer.token.stmt.AbstractStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.CaseStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.CatchStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.DeclareStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.DefaultStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.DoStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ElseIfStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ElseStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.EndforStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.EndforeachStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.EndifStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.EndswitchStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.EndwhileStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExtendsStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FinalStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FinallyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ForStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ForeachStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.GlobalStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.IfStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ImplementsStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceUseStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.PrivateStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ProtectedStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.PublicStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ReturnStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.SwitchStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.TryStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.WhileStmtToken;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.ParseException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenizerTest {

    private Environment environment = new Environment();

    @Test
    public void testSimple() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context(""));

        assertNull(tokenizer.nextToken());
        assertEquals("", tokenizer.getCode());

        tokenizer = new Tokenizer(new Context(" "));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context("  "));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context("\t"));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context("\n"));
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(new Context("\r"));
        assertNull(tokenizer.nextToken());
    }

    @Test
    public void testScalarTokens() throws IOException {
        Token token;
        Tokenizer tokenizer = new Tokenizer(new Context("10 3.3 'foo' '' \"xyz\" 0xCC 0b0011 true false"));

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
        assertEquals(DOUBLE, ((StringExprToken) token).getQuote());
        assertEquals("xyz", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof IntegerExprToken);
        assertEquals(new BigInteger("CC", 16).longValue(), ((IntegerExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof IntegerExprToken);
        assertEquals(new BigInteger("0011", 2).longValue(), ((IntegerExprToken) token).getValue());

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
    public void testStringSlashes() throws IOException {
        Token token;
        Tokenizer tokenizer = new Tokenizer(new Context(" 'foo\\'bar' \"foo\\\"bar\""));

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals("foo'bar", ((StringExprToken) token).getValue());

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals("foo\"bar", ((StringExprToken) token).getValue());
    }

    @Test
    public void testMagicString() throws IOException {
        Token token;
        Tokenizer tokenizer = new Tokenizer(new Context("\"\\.{$foo}\""));

        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals("\\.{$foo}", ((StringExprToken) token).getValue());
        assertEquals(1, ((StringExprToken) token).getSegments().size());

        StringExprToken.Segment segment = ((StringExprToken) token).getSegments().get(0);
        assertEquals(2, segment.from);
        assertEquals(8, segment.to);
    }

    @Test
    public void testEscapeBug307() throws IOException {
        Token token;
        Tokenizer tokenizer = new Tokenizer(new Context("\"Ymd\\THis\\Z\""));
        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals("Ymd\\THis\\Z", ((StringExprToken) token).getValue());
    }

    @Test
    public void testComplexOperators() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("== >= <= === !== != && ||"));

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
    public void testSimpleOperators() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("= + - / * % . and or new && || ! xor **"));

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
        assertTrue(tokenizer.nextToken() instanceof PowExprToken);
    }

    @Test
    public void testParseError() throws IOException {
        Throwable ex = null;
        Tokenizer tokenizer = new Tokenizer(new Context("  'foobar \n "));

        try {
            tokenizer.nextToken();
        } catch (Throwable e) {
            ex = e;
        }

        assertTrue(ex instanceof ParseException);
        TraceInfo traceInfo = ((ParseException) ex).getTraceInfo();
        assertNotNull(traceInfo);
        assertNull(traceInfo.getContext().getFileName());
        assertEquals(1, traceInfo.getStartLine());
        assertEquals(1, traceInfo.getEndLine());
        assertEquals(1, traceInfo.getStartPosition());
        assertEquals(1, traceInfo.getEndPosition());
    }

    @Test
    public void testComplex() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("0==10==='30';"));

        assertTrue(tokenizer.nextToken() instanceof IntegerExprToken);
        assertTrue(tokenizer.nextToken() instanceof EqualExprToken);
        assertTrue(tokenizer.nextToken() instanceof IntegerExprToken);
        assertTrue(tokenizer.nextToken() instanceof IdenticalExprToken);
        assertTrue(tokenizer.nextToken() instanceof StringExprToken);
        assertTrue(tokenizer.nextToken() instanceof SemicolonToken);

        tokenizer = new Tokenizer(new Context("F =; 20;"));

        Token token;
        assertTrue((token = tokenizer.nextToken()) instanceof NameToken);
        assertEquals("F", ((NameToken) token).getName());
        assertTrue(tokenizer.nextToken() instanceof AssignExprToken);
        assertTrue(tokenizer.nextToken() instanceof SemicolonToken);
        assertTrue(tokenizer.nextToken() instanceof IntegerExprToken);
        assertTrue(tokenizer.nextToken() instanceof SemicolonToken);

        tokenizer = new Tokenizer(new Context("123foobar MAX_64Bit"));
        token = tokenizer.nextToken();
        assertTrue(token instanceof IntegerExprToken);

        token = tokenizer.nextToken();
        assertTrue(token instanceof NameToken);
        assertEquals("foobar", token.getWord());
        assertTrue(tokenizer.nextToken() instanceof NameToken);
        assertNull(tokenizer.nextToken());

        assertNull(tokenizer.nextToken());
    }

    @Test
    public void testBraces() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context(" :: ->foobar('a', 1, 3.0);"));

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
    public void testVarVar() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("$$foo $ $bar $$$foobar"));

        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);

        tokenizer = new Tokenizer(new Context("${'foo;6bar'}"));
        assertTrue(tokenizer.nextToken() instanceof DollarExprToken);
        assertTrue(tokenizer.nextToken() instanceof BraceExprToken);
        assertTrue(tokenizer.nextToken() instanceof StringExprToken);
        assertTrue(tokenizer.nextToken() instanceof BraceExprToken);
    }

    @Test
    public void testMacro() throws IOException {
        Tokenizer tokenizer = new Tokenizer(
                new Context("__LINE__ __FILE__ __DIR__ __METHOD__ __FUNCTION__ __CLASS__ __NAMESPACE__ __TRAIT__")
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
    public void testStmt() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context(
                "class function private public protected static final try catch for if foreach switch while " +
                        "default return declare case do else elseif endif endfor endforeach endwhile endswitch " +
                        "abstract use namespace finally extends implements global"
        ));

        assertTrue(tokenizer.nextToken() instanceof ClassStmtToken);
        assertTrue(tokenizer.nextToken() instanceof FunctionStmtToken);
        assertTrue(tokenizer.nextToken() instanceof PrivateStmtToken);
        assertTrue(tokenizer.nextToken() instanceof PublicStmtToken);
        assertTrue(tokenizer.nextToken() instanceof ProtectedStmtToken);
        assertTrue(tokenizer.nextToken() instanceof StaticExprToken);
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

    @Test
    public void testSplitNot() throws IOException {
        Tokenizer tokenizer = new Tokenizer(
                new Context("!true")
        );
        assertTrue(tokenizer.nextToken() instanceof BooleanNotExprToken);
        assertTrue(tokenizer.nextToken() instanceof BooleanExprToken);
    }

    @Test
    public void testComments() throws IOException {
        Tokenizer tokenizer = new Tokenizer(
                new Context("/** FOO BAR \n\r100500 */")
        );
        Token token = tokenizer.nextToken();
        assertTrue(token instanceof CommentToken);
        assertEquals(CommentToken.Kind.DOCTYPE, ((CommentToken) token).getKind());
        assertEquals("FOO BAR\n\n100500", ((CommentToken) token).getComment());

        // simple
        tokenizer = new Tokenizer(
                new Context("// foobar \n $x")
        );
        token = tokenizer.nextToken();
        assertTrue(token instanceof CommentToken);
        assertEquals(CommentToken.Kind.SIMPLE, ((CommentToken) token).getKind());
        assertEquals(" foobar ", ((CommentToken) token).getComment());

        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);
        assertNull(tokenizer.nextToken());

        tokenizer = new Tokenizer(
                new Context("# // foobar \n $x")
        );
        token = tokenizer.nextToken();
        assertTrue(token instanceof CommentToken);
        assertEquals(CommentToken.Kind.SIMPLE, ((CommentToken) token).getKind());
        assertEquals(" // foobar ", ((CommentToken) token).getComment());

        assertTrue(tokenizer.nextToken() instanceof VariableExprToken);
        assertNull(tokenizer.nextToken());

        // block
        tokenizer = new Tokenizer(
                new Context("/* foobar \n */")
        );
        token = tokenizer.nextToken();
        assertTrue(token instanceof CommentToken);
        assertEquals(CommentToken.Kind.BLOCK, ((CommentToken) token).getKind());
    }

    @Test
    public void testHeredoc() throws IOException {
        Tokenizer tokenizer = new Tokenizer(
                new Context("<<<DOC\n <foobar> \nDOC;\n")
        );
        Token token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.DOC, ((StringExprToken) token).getQuote());
        assertEquals(" <foobar> ", ((StringExprToken) token).getValue());

        tokenizer = new Tokenizer(
                new Context("<<<\"DOC\"\n \\n<foobar> \nDOC;\n")
        );
        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.DOC, ((StringExprToken) token).getQuote());
        assertEquals(" \n<foobar> ", ((StringExprToken) token).getValue());

        tokenizer = new Tokenizer(
                new Context("<<<'DOC'\n \\n<foobar> \nDOC;\n")
        );
        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.DOC, ((StringExprToken) token).getQuote());
        assertEquals(" \\n<foobar> ", ((StringExprToken) token).getValue());

        tokenizer = new Tokenizer(
                new Context("a(<<<DOC\n<foobar>\nDOC\n)\n")
        );
        token = tokenizer.nextToken();
        assertTrue(token instanceof NameToken);
        token = tokenizer.nextToken();
        assertTrue(token instanceof BraceExprToken);
        token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.DOC, ((StringExprToken) token).getQuote());
        assertEquals("<foobar>", ((StringExprToken) token).getValue());
    }

    @Test
    public void testUnicodeEscape() {
        Stream.of(
                "\"\\u{61}\"",
                "\"\\u{061}\"",
                "\"\\u{0061}\"",
                "\"\\u{00061}\"",
                "\"\\u{000000000000000000000000000000000061}\""
        ).forEach(s -> assertThat(s).nextIsStringToken("\u0061", DOUBLE));

        assertThat("\"\\u{2603}\"").nextIsStringToken("☃", DOUBLE);
        assertThat("\"\\u{1F602}\"").nextIsStringToken("\uD83D\uDE02", DOUBLE);
        assertThat("\"\\u{0000001F602}\"").nextIsStringToken("\uD83D\uDE02", DOUBLE);
    }

    @Test
    public void testUnicodeEscapeIsCaseInsensitive() {
        assertThat("\"\\u{FF}\"").nextIsStringToken("ÿ", DOUBLE);
        assertThat("\"\\u{ff}\"").nextIsStringToken("ÿ", DOUBLE);
    }

    @Test
    public void testUnicodeEscapeLeadingZeroesArePermitted() {
        assertThat("\"\\u{0000001F602}\"").nextIsStringToken("\uD83D\uDE02", DOUBLE);
    }

    @Test
    public void testUnicodeEscapeSequence() {
        // continuous sequence
        assertThat("\"\\u{0061}\\u{0062}\\u{0063}\\u{0064}\"").nextIsStringToken("abcd", DOUBLE);

        // braking sequences
        Stream.of(
                "\"abcd\"",
                "\"\\u{0061}b\\u{0063}\\u{0064}\"",
                "\"\\u{0061}bc\\u{0064}\"",
                "\"\\u{0061}b\\u{0063}d\"",
                "\"ab\\u{0063}d\"",
                "\"ab\\u{0063}\\u{0064}\""
        )
                .forEach(content -> assertThat(content).nextIsStringToken("abcd", DOUBLE));
    }

    @Test
    public void testUnicodeEscapeRtl() {
        assertThat("\"\\u{202E}Reversed text\"").nextIsStringToken("\u202EReversed text");
    }

    @Test
    public void testUnicodeEscapeWithSingleQuote() {
        // unicode escape sequences in single
        // quoted string literals should be interpreted as is.
        assertThat("'\\u{0061}\\u{0062}\\u{0063}\\u{0064}'")
                .nextIsStringToken("\\u{0061}\\u{0062}\\u{0063}\\u{0064}", SINGLE);
    }

    @Test
    public void testUnicodeEscapeError() {
        assertThatExceptionOfType(ParseException.class)
                .isThrownBy(() -> new Tokenizer(new Context("\"\\u{}\"")).nextToken())
                .satisfies(e ->
                        Assertions.assertThat(e.getTraceInfo())
                                .extracting(TraceInfo::getStartLine, TraceInfo::getEndLine, TraceInfo::getStartPosition,
                                        TraceInfo::getEndPosition)
                                .containsExactly(0, 0, 3, 4)
                );
    }

    @Test
    public void testUnicodeEscapeSequenceInHeredoc() {
        assertThat("<<<DOC\nThe Armenian language: \\u{0540}\\u{0561}\\u{0575}\\u{0578}\\u{0581}" +
                " \\u{056c}\\u{0565}\\u{0566}\\u{0578}\\u{0582}\nDOC;\n")
                .nextIsStringToken("The Armenian language: Հայոց լեզու", DOC);
    }

    @Test
    public void testBug122() throws IOException {
        Tokenizer tokenizer = new Tokenizer(
                new Context("<<<ESC\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "<?php\n" +
                        "ESC;\n")
        );
        Token token = tokenizer.nextToken();
        assertTrue(token instanceof StringExprToken);
        assertEquals(StringExprToken.Quote.DOC, ((StringExprToken) token).getQuote());
        assertEquals("\n\n\n\n\n<?php", ((StringExprToken) token).getValue());
    }
}
