package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;
import ru.regenix.jphp.lexer.tokens.stmt.*;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ClassGenerator extends Generator<ClassStmtToken> {

    @SuppressWarnings("unchecked")
    private final static Class<? extends Token>[] modifiers = new Class[]{
        PrivateStmtToken.class,
        ProtectedStmtToken.class,
        PublicStmtToken.class,
        StaticStmtToken.class,
        FinalStmtToken.class
    };

    public ClassGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected void processName(ClassStmtToken result, ListIterator<Token> iterator){
        Token name = nextToken(iterator);
        if (name instanceof NameToken){
            result.setName((NameToken)name);
        } else
            throw new ParseException(
                    Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(name.getType(), TokenType.T_STRING),
                    name.toTraceInfo(analyzer.getFile())
            );
    }

    protected void processExtends(ClassStmtToken result, ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);

        Token token = iterator.next();
        if (token instanceof ExtendsStmtToken){
            Token extend = analyzer.generateToken(token, iterator);
            result.setExtend((ExtendsStmtToken)extend);
        } else
            iterator.previous();
    }

    protected void processImplements(ClassStmtToken result, ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);
        Token token = iterator.next();
        if (token instanceof ImplementsStmtToken){
            Token implement = analyzer.generateToken(token, iterator);
            result.setImplement((ImplementsStmtToken) implement);
        } else
            iterator.previous();
    }

    @SuppressWarnings("unchecked")
    protected void processBody(ClassStmtToken result, ListIterator<Token> iterator){
        analyzer.setClazz(result);

        Token token = nextToken(iterator);
        if (token instanceof BraceExprToken){
            BraceExprToken brace = (BraceExprToken)token;
            if (brace.isBlockOpened()){

                List<ConstStmtToken> constants = new ArrayList<ConstStmtToken>();
                while (iterator.hasNext()){
                    Token current = iterator.next();
                    if (current instanceof ExprStmtToken)
                        throw new ParseException(
                            Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch("T_EXPR", "T_NOT_EXPR"),
                            current.toTraceInfo(analyzer.getFile())
                        );

                    if (current instanceof ConstStmtToken){
                        ConstStmtToken one = analyzer.generator(ConstGenerator.class).getToken(current, iterator);
                        one.setClazz(result);
                        constants.add(one);
                    } else if (isTokenClass(current, modifiers)){

                    } else {

                    }
                }

                result.setConstants(constants);
                analyzer.setClazz(null);
                return;
            }
        }
        throw new ParseException(
            Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(token.getType(), "{"),
            token.toTraceInfo(analyzer.getFile())
        );
    }

    @SuppressWarnings("unchecked")
    protected ClassStmtToken processDefine(Token current, ListIterator<Token> iterator){
        if (isTokenClass(current, FinalStmtToken.class, AbstractStmtToken.class)){
            Token next = iterator.next();
            if (next instanceof ClassStmtToken){
                ClassStmtToken result = (ClassStmtToken)next;
                result.setAbstract(current instanceof AbstractStmtToken);
                result.setFinal(current instanceof FinalStmtToken);

                return result;
            } else {
                iterator.previous();
            }
        }

        if (current instanceof ClassStmtToken)
            return (ClassStmtToken)current;

        return null;
    }

    @Override
    public ClassStmtToken getToken(Token current, ListIterator<Token> iterator) {
        ClassStmtToken result = processDefine(current, iterator);

        if (result != null){
            processName(result, iterator);
            processExtends(result, iterator);
            processImplements(result, iterator);
            processBody(result, iterator);
        }

        return result;
    }
}
