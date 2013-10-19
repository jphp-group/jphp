package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExtendsStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ImplementsStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ListIterator;

public class ClassGenerator extends Generator<ClassStmtToken> {

    public ClassGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected void processExtends(ClassStmtToken result, ListIterator<Token> iterator){
        Token token = iterator.next();
        if (token instanceof ExtendsStmtToken){
            Token extend = analyzer.generateToken(token, iterator);
            result.setExtend((ExtendsStmtToken)extend);
        }
    }

    protected void processImplements(ClassStmtToken result, ListIterator<Token> iterator){
        Token token = iterator.next();
        if (token instanceof ImplementsStmtToken){
            Token implement = analyzer.generateToken(token, iterator);
            result.setImplement((ImplementsStmtToken) implement);
        }
    }

    protected void processBody(ClassStmtToken result, ListIterator<Token> iterator){

    }

    @Override
    public ClassStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof ClassStmtToken){
            checkUnexpectedEnd(current, iterator);

            Token next = iterator.next();
            Token name = analyzer.generateToken(next, iterator, NameGenerator.class);
            if (name != null && ((FulledNameToken) name).isSingle()){
                ClassStmtToken result = (ClassStmtToken)current;

                processExtends(result, iterator);
                processImplements(result, iterator);
                processBody(result, iterator);

                return result;
            } else
                throw new ParseException(
                        Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(next.getType(), "{"),
                        next.getMeta().toTraceInfo(analyzer.getFile())
                );
        }
        return null;
    }
}
