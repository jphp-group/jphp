package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.tokens.expr.*;
import ru.regenix.jphp.lexer.tokens.expr.operator.*;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.macro.*;
import ru.regenix.jphp.lexer.tokens.stmt.*;

import java.util.HashMap;

public class TokenFinder {

    private final static HashMap<String, Class<? extends Token>> patterns = new HashMap<String, Class<? extends Token>>(){{
        put("+", PlusExprToken.class);
        put("-", MinusExprToken.class);
        put("*", MulExprToken.class);
        put("/", DivExprToken.class);
        put("%", ModExprToken.class);
        put("=", AssignExprToken.class);
        put("\\", BackslashExprToken.class);
        put("==", EqualExprToken.class);
        put("!=", BooleanNotEqualExprToken.class);
        put("===", IdenticalExprToken.class);
        put("!==", NotIdenticalExprToken.class);
        put(">", GreaterExprToken.class);
        put(">=", GreaterOrEqualExprToken.class);
        put("<", SmallerExprToken.class);
        put("<=", SmallerOrEqualToken.class);
        put("^", PowExprToken.class);

        put("&&", BooleanAndExprToken.class);
        put("and", BooleanAndExprToken.class);
        put("||", BooleanOrExprToken.class);
        put("or", BooleanOrExprToken.class);
        put("!", BooleanNotExprToken.class);

        put("->", DynamicAccessExprToken.class);
        put("::", StaticAccessExprToken.class);
        put("=>", ArrayKeyValueExprToken.class);
        put("[]", ArrayPushExprToken.class);

        put("new", NewExprToken.class);
        put("instanceof", InstanceofExprToken.class);
        put(".", ConcatExprToken.class);
        put("true", BooleanExprToken.class);
        put("false", BooleanExprToken.class);
        put(";", SemicolonExprToken.class);
        put("&", AmpersandToken.class);
        put(",", CommaToken.class);

        put("{", BraceExprToken.class);
        put("[", BraceExprToken.class);
        put("(", BraceExprToken.class);
        put("}", BraceExprToken.class);
        put("]", BraceExprToken.class);
        put(")", BraceExprToken.class);

        put("static", StaticStmtToken.class);
        put("self", SelfExprToken.class);
        put("parent", ParentExprToken.class);
        put("$this", ThisExprToken.class);

        put("class", ClassStmtToken.class);
        put("if", IfStmtToken.class);
        put("function", FunctionStmtToken.class);
        put("const", ConstStmtToken.class);
        put("implements", ImplementsStmtToken.class);
        put("namespace", NamespaceStmtToken.class);
        put("abstract", AbstractStmtToken.class);
        put("final", FinalStmtToken.class);
        put("private", PrivateStmtToken.class);
        put("protected", ProtectedStmtToken.class);
        put("public", PublicStmtToken.class);
        put("try", TryStmtToken.class);
        put("catch", CatchStmtToken.class);
        put("finally", FinallyStmtToken.class);

        put("__line__", LineMacroToken.class);
        put("__file__", FileMacroToken.class);
        put("__dir__", DirMacroToken.class);
        put("__function__", FunctionMacroToken.class);
        put("__class__", ClassMacroToken.class);
        put("__method__", MethodMacroToken.class);
        put("__trait__", TraitMacroToken.class);
        put("__namespace__", NamespaceStmtToken.class);
    }};

    public TokenFinder() {
    }

    public Class<? extends Token> find(String word){
        word = word.toLowerCase();
        Class<? extends Token> token = patterns.get(word);
        if (token != null)
            return token;

        if (word.startsWith("$"))
            return VariableExprToken.class;

        if (word.matches("^[0-9]+$"))
            return IntegerExprToken.class;

        if (word.matches("^[0-9]+?\\.[0-9]+$"))
            return DoubleExprToken.class;

        if (word.matches("^0x[0-9a-f]+$"))
            return HexExprValue.class;

        if (word.matches("^[a-z_]{1}[a-z_0-9]+$"))
            return NameToken.class;

        return null;
    }

    public Class<? extends Token> find(TokenMeta meta){
        return find(meta.getWord());
    }
}
