package ru.regenix.jphp.tokenizer;

import ru.regenix.jphp.tokenizer.token.*;
import ru.regenix.jphp.tokenizer.token.expr.*;
import ru.regenix.jphp.tokenizer.token.expr.operator.*;
import ru.regenix.jphp.tokenizer.token.expr.value.*;
import ru.regenix.jphp.tokenizer.token.expr.value.macro.*;
import ru.regenix.jphp.tokenizer.token.stmt.*;

import java.util.HashMap;

public class TokenFinder {

    private final static HashMap<String, Class<? extends Token>> patterns = new HashMap<String, Class<? extends Token>>(){{
        put("<?", OpenTagToken.class);
        put("<?php", OpenTagToken.class);
        put("?>", BreakToken.class);
        put("<?=", OpenEchoTagToken.class);
        put("/*", CommentToken.class);
        put("/**", CommentToken.class);
        put("//", CommentToken.class);

        put("+", PlusExprToken.class);
        put("-", MinusExprToken.class);
        put("*", MulExprToken.class);
        put("/", DivExprToken.class);
        put("%", ModExprToken.class);
        put("=", AssignExprToken.class);
        put("\\", BackslashExprToken.class);
        put("==", EqualExprToken.class);
        put("!=", BooleanNotEqualExprToken.class);
        put("<>", BooleanNotEqualExprToken.class);
        put("===", IdenticalExprToken.class);
        put("!==", NotIdenticalExprToken.class);
        put(">", GreaterExprToken.class);
        put(">=", GreaterOrEqualExprToken.class);
        put("<", SmallerExprToken.class);
        put("<=", SmallerOrEqualToken.class);

        put("&&", BooleanAndExprToken.class);
        put("and", BooleanAnd2ExprToken.class);
        put("||", BooleanOrExprToken.class);
        put("or", BooleanOr2ExprToken.class);
        put("xor", BooleanXorExprToken.class);
        put("!", BooleanNotExprToken.class);

        put("?", ValueIfElseToken.class);

        put("~", NotExprToken.class);
        put("^", XorExprToken.class);
        put("|", OrExprToken.class);
        put("&", AndExprToken.class);
        put(">>", ShiftRightExprToken.class);
        put("<<", ShiftLeftExprToken.class);

        put("@", SilentToken.class);

        put("->", DynamicAccessExprToken.class);
        put("::", StaticAccessExprToken.class);
        put("=>", KeyValueExprToken.class);

        put("+=", AssignPlusExprToken.class);
        put("-=", AssignMinusExprToken.class);
        put("*=", AssignMulExprToken.class);
        put("/=", AssignDivExprToken.class);
        put("%=", AssignModExprToken.class);
        put(".=", AssignConcatExprToken.class);
        put("^=", AssignXorExprToken.class);
        put("&=", AssignAndExprToken.class);
        put("|=", AssignOrExprToken.class);
        put("++", IncExprToken.class);
        put("--", DecExprToken.class);
        put(">>=", AssignShiftRightExprToken.class);
        put("<<=", AssignShiftLeftExprToken.class);

        put("new", NewExprToken.class);
        put("instanceof", InstanceofExprToken.class);
        put(".", ConcatExprToken.class);
        put(":", ColonToken.class);
        put("true", BooleanExprToken.class);
        put("false", BooleanExprToken.class);
        put("null", NullExprToken.class);
        put(";", SemicolonToken.class);
        put("&", AmpersandRefToken.class);
        put(",", CommaToken.class);
        put("$", DollarExprToken.class);

        put("{", BraceExprToken.class);
        put("[", BraceExprToken.class);
        put("(", BraceExprToken.class);
        put("}", BraceExprToken.class);
        put("]", BraceExprToken.class);
        put(")", BraceExprToken.class);

        put("static", StaticExprToken.class);
        put("self", SelfExprToken.class);
        put("parent", ParentExprToken.class);
        put("$this", ThisExprToken.class);

        put("array", ArrayExprToken.class);
        put("as", AsStmtToken.class);
        put("if", IfStmtToken.class);
        put("else", ElseStmtToken.class);
        put("elseif", ElseIfStmtToken.class);
        put("while", WhileStmtToken.class);
        put("do", DoStmtToken.class);
        put("for", ForStmtToken.class);
        put("foreach", ForeachStmtToken.class);
        put("switch", SwitchStmtToken.class);
        put("case", CaseStmtToken.class);
        put("default", DefaultStmtToken.class);
        put("declare", DeclareStmtToken.class);
        put("return", ReturnStmtToken.class);
        put("endif", EndifStmtToken.class);
        put("endforeach", EndforeachStmtToken.class);
        put("endfor", EndforStmtToken.class);
        put("endwhile", EndwhileStmtToken.class);
        put("endswitch", EndswitchStmtToken.class);
        put("enddeclare", EnddeclareStmtToken.class);
        put("break", BreakStmtToken.class);
        put("continue", ContinueStmtToken.class);

        put("class", ClassStmtToken.class);
        put("function", FunctionStmtToken.class);
        put("const", ConstStmtToken.class);
        put("implements", ImplementsStmtToken.class);
        put("namespace", NamespaceStmtToken.class);
        put("use", NamespaceUseStmtToken.class);
        put("abstract", AbstractStmtToken.class);
        put("final", FinalStmtToken.class);
        put("private", PrivateStmtToken.class);
        put("protected", ProtectedStmtToken.class);
        put("public", PublicStmtToken.class);
        put("try", TryStmtToken.class);
        put("catch", CatchStmtToken.class);
        put("finally", FinallyStmtToken.class);
        put("extends", ExtendsStmtToken.class);
        put("implements", ImplementsStmtToken.class);
        put("global", GlobalStmtToken.class);

        put("__line__", LineMacroToken.class);
        put("__file__", FileMacroToken.class);
        put("__dir__", DirMacroToken.class);
        put("__function__", FunctionMacroToken.class);
        put("__class__", ClassMacroToken.class);
        put("__method__", MethodMacroToken.class);
        put("__trait__", TraitMacroToken.class);
        put("__namespace__", NamespaceMacroToken.class);

        put("include", IncludeExprToken.class);
        put("include_once", IncludeOnceExprToken.class);
        put("require", RequireExprToken.class);
        put("require_once", RequireOnceExprToken.class);
        put("echo", EchoStmtToken.class);
    }};

    public TokenFinder() {
    }

    public Class<? extends Token> find(String word){
        word = word.toLowerCase();
        Class<? extends Token> token = patterns.get(word);
        if (token != null)
            return token;

        if (word.matches("^[1-9][0-9]*$") || word.matches("^[0]+$"))
            return IntegerExprToken.class;

        if (word.matches("^[0-9]+\\.[0-9]+$")) // 1.234
            return DoubleExprToken.class;

        if (word.matches("^[0-9]+\\.[0-9]e[0-9]$")) // 1.2e3;
            return DoubleExprToken.class;

        if (word.matches("^[0-9]+e[\\-|\\+][0-9]+$")) // 7E-10
            return DoubleExprToken.class;

        if (word.matches("^0x[0-9a-f]+$"))
            return HexExprValue.class;

        if (word.matches("^\\\\?[a-z_\\x7f-\\xff][\\\\a-z0-9_\\x7f-\\xff]*$")){
            if (word.indexOf('\\') > -1)
                return FulledNameToken.class;
            else
                return NameToken.class;
        }

        if (word.matches("^[\\$][a-z_\\x7f-\\xff][a-z0-9_\\x7f-\\xff]*$"))
            return VariableExprToken.class;

        return null;
    }

    public Class<? extends Token> find(TokenMeta meta){
        return find(meta.getWord());
    }
}
