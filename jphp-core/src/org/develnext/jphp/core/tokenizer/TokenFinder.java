package org.develnext.jphp.core.tokenizer;

import org.develnext.jphp.core.tokenizer.token.*;
import org.develnext.jphp.core.tokenizer.token.expr.*;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.*;
import org.develnext.jphp.core.tokenizer.token.stmt.*;

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
        put("<<<", StringStartDocToken.class);

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
        put("clone", CloneExprToken.class);
        put("instanceof", InstanceofExprToken.class);
        put("insteadof", InsteadofStmtToken.class);
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
        //put("$this", ThisExprToken.class);

        //put("array", ArrayExprToken.class);
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
        put("goto", GotoStmtToken.class);

        put("unset", UnsetExprToken.class);
        put("isset", IssetExprToken.class);
        put("empty", EmptyExprToken.class);
        put("die", DieExprToken.class);
        put("exit", DieExprToken.class);

        put("class", ClassStmtToken.class);
        put("interface", InterfaceStmtToken.class);
        put("trait", TraitStmtToken.class);
        put("function", FunctionStmtToken.class);
        put("const", ConstStmtToken.class);
        put("namespace", NamespaceStmtToken.class);
        put("use", NamespaceUseStmtToken.class);
        //put("uses", UsesStmtToken.class);
        put("abstract", AbstractStmtToken.class);
        put("final", FinalStmtToken.class);
        put("private", PrivateStmtToken.class);
        put("protected", ProtectedStmtToken.class);
        put("public", PublicStmtToken.class);
        put("var", VarStmtToken.class);
        put("try", TryStmtToken.class);
        put("catch", CatchStmtToken.class);
        put("finally", FinallyStmtToken.class);
        put("throw", ThrowStmtToken.class);
        put("extends", ExtendsStmtToken.class);
        put("implements", ImplementsStmtToken.class);
        put("global", GlobalStmtToken.class);
        put("list", ListExprToken.class);

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
        put("print", PrintNameToken.class);
    }};

    public TokenFinder() {
    }

    public Class<? extends Token> find(String word){
        word = word.toLowerCase();
        Class<? extends Token> token = patterns.get(word);
        if (token != null)
            return token;

        int length = word.length();

        boolean isVar = false;
        boolean isHex = false;
        boolean isBinary = false;
        boolean isInt = true;
        boolean isFloat = false;
        boolean isName = true;
        boolean isFulledName = false;

        boolean e_ch = false;
        boolean p_ch = false;
        boolean sign_ch = false;
        for(int i = 0; i < length; i++){
            char ch = word.charAt(i);

            switch (ch){
                case '$':
                    isVar = true;
                    if (i == 0){
                        for(int j = i + 1; j < length; j++){
                            ch = word.charAt(j);
                            if (j == i + 1 && Character.isDigit(ch)) {
                                isVar = false;
                                break;
                            }
                            if (ch != '_' && !Character.isLetter(ch) && !Character.isDigit(ch)) {
                                isVar = false;
                                break;
                            }
                        }
                    } else
                        isVar = false;
                    break;
                case '\\':
                    if (isName){
                        isFulledName = true;
                    }
                    isFloat = isHex = isInt = isVar = false;
                    break;
                case 'x':case 'X':
                    if (i == 1 && word.charAt(i - 1) == '0') {
                        isHex = true;
                        isFloat = false;
                        isInt = false;

                        for(int j = i + 1; j < length; j++){
                            ch = word.charAt(j);
                            if (!(ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f' || Character.isDigit(ch))){
                                isHex = false;
                                break;
                            }
                        }
                    } else {
                        isInt = false;
                    }
                    break;
                case 'b':
                    if (i == 1 && word.charAt(i - 1) == '0') {
                        isBinary = true;
                        isFloat = false;
                        isInt = true;

                        for(int j = i + 1; j < length; j++){
                            ch = word.charAt(j);
                            if (ch != '0' && ch != '1'){
                                isBinary = false;
                                break;
                            }
                        }
                    } else {
                        isInt = false;
                    }
                    break;
                case '_':
                    isInt = false;
                    break;
                case '-':case '+':
                    if (!sign_ch && isInt){
                        ch = i + 1 < length ? word.charAt(i + 1) : 0;

                        if (ch == 'e' || ch == 'E'){
                            isFloat = true;
                            sign_ch = true;
                        } else
                            return null;
                    } else {
                        return null;
                    }
                    break;
                case '.':
                    if (!p_ch && isInt){
                        ch = i + 1 < length ? word.charAt(i + 1) : 0;
                        if (Character.isDigit(ch)){
                            isFloat = true;
                        } else
                            return null;
                    } else
                        return null;
                    break;
                case 'e':case 'E':
                    if (!e_ch){
                        isFloat = true;
                        ch = i + 1 < length ? word.charAt(i + 1) : 0;
                        if (!Character.isDigit(ch)){
                            if (ch == '-' || ch == '+'){
                                ch = i + 2 < length ? word.charAt(i + 2) : 0;
                                if (!Character.isDigit(ch)) {
                                    return null;
                                }
                                i += 1;
                            } else {
                                isInt = false;
                                //i += 0;
                            }
                        }
                        e_ch = true;
                    }
                    break;
                default:
                    if (Character.isDigit(ch)){
                        if (i == 0){
                            isName = false;
                        }
                    } else if (Character.isLetter(ch)){
                        isInt = false;
                    } else {
                        return null;
                    }
            }
        }

        if (isVar)
            return VariableExprToken.class;

        if (isHex)
            return HexExprValue.class;

        if (isBinary)
            return BinaryExprValue.class;

        if (isInt && isFloat)
            return DoubleExprToken.class;

        if (isInt)
            return IntegerExprToken.class;

        if (isName && isFulledName)
            return FulledNameToken.class;

        if (isName)
            return NameToken.class;

        return null;
    }

    public Class<? extends Token> find(TokenMeta meta){
        return find(meta.getWord());
    }
}
