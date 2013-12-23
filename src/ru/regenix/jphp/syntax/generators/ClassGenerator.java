package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.SemicolonToken;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.expr.CommaToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.AssignExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.StaticExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.*;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.*;

public class ClassGenerator extends Generator<ClassStmtToken> {

    @SuppressWarnings("unchecked")
    private final static Class<? extends Token>[] modifiers = new Class[]{
        PrivateStmtToken.class,
        ProtectedStmtToken.class,
        PublicStmtToken.class,
        StaticExprToken.class,
        FinalStmtToken.class,
        AbstractStmtToken.class,
        VarStmtToken.class,
        ConstStmtToken.class
    };

    public ClassGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected void processName(ClassStmtToken result, ListIterator<Token> iterator){
        Token name = nextToken(iterator);
        if (name instanceof NameToken){
            result.setName((NameToken)name);
        } else
            unexpectedToken(name, TokenType.T_STRING);
    }

    protected void processExtends(ClassStmtToken result, ListIterator<Token> iterator){
        Token token = nextToken(iterator);
        if (token instanceof ExtendsStmtToken){
            ExtendsStmtToken extend = (ExtendsStmtToken)token;
            FulledNameToken what = analyzer.generator(NameGenerator.class).getToken(nextToken(iterator), iterator);
            extend.setName(analyzer.getRealName(what));
            result.setExtend(extend);
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

    protected List<ClassVarStmtToken> processProperty(VariableExprToken current, List<Token> modifiers,
                                                      ListIterator<Token> iterator){
        Token next = current;
        Token prev = null;
        Set<VariableExprToken> variables = new HashSet<VariableExprToken>();
        ExprStmtToken initValue = null;

        List<ClassVarStmtToken> result = new ArrayList<ClassVarStmtToken>();

        Modifier modifier = Modifier.PUBLIC;
        boolean isStatic = false;
        for(Token token : modifiers){
            if (token instanceof PrivateStmtToken)
                modifier = Modifier.PRIVATE;
            else if (token instanceof ProtectedStmtToken)
                modifier = Modifier.PROTECTED;
            else if (token instanceof StaticExprToken)
                isStatic = true;
        }

        do {
            if (next instanceof VariableExprToken){
                if (!variables.add((VariableExprToken)next))
                    throw new ParseException(
                            Messages.ERR_PARSE_IDENTIFIER_X_ALREADY_USED.fetch(next.getWord()),
                            next.toTraceInfo(analyzer.getContext())
                    );
            } else if (next instanceof CommaToken){
                if (!(prev instanceof VariableExprToken))
                    unexpectedToken(next);
            } else if (next instanceof AssignExprToken){
                if (!(prev instanceof VariableExprToken))
                    unexpectedToken(next);

                initValue = analyzer.generator(SimpleExprGenerator.class).getToken(nextToken(iterator), iterator, null, null);
                break;
            } else if (next instanceof SemicolonToken){
                if (!(prev instanceof VariableExprToken))
                    unexpectedToken(next);
                break;
            }

            prev = next;
            next = nextToken(iterator);
        } while (true);

        for(VariableExprToken variable : variables){
            ClassVarStmtToken classVar = new ClassVarStmtToken(variable.getMeta());
            classVar.setModifier(modifier);
            classVar.setStatic(isStatic);
            classVar.setValue(initValue);
            classVar.setVariable(variable);

            result.add(classVar);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected void processBody(ClassStmtToken result, ListIterator<Token> iterator){
        analyzer.setClazz(result);

        Token token = nextToken(iterator);
        if (token instanceof BraceExprToken){
            BraceExprToken brace = (BraceExprToken)token;
            if (brace.isBlockOpened()){

                List<ConstStmtToken> constants = new ArrayList<ConstStmtToken>();
                List<MethodStmtToken> methods = new ArrayList<MethodStmtToken>();
                List<ClassVarStmtToken> properties = new ArrayList<ClassVarStmtToken>();

                List<Token> modifiers = new ArrayList<Token>();
                while (iterator.hasNext()){
                    Token current = iterator.next();
                    if (current instanceof ExprStmtToken)
                        unexpectedToken(current, "expression");

                    if (current instanceof ConstStmtToken){
                        if (!modifiers.isEmpty())
                            unexpectedToken(modifiers.get(0));

                        ConstStmtToken one = analyzer.generator(ConstGenerator.class).getToken(current, iterator);
                        one.setClazz(result);
                        constants.add(one);
                        modifiers.clear();
                    } else if (isTokenClass(current, ClassGenerator.modifiers)){
                        for(Token modifier : modifiers){
                            if (modifier.getType() == current.getType())
                                unexpectedToken(current);
                        }
                        modifiers.add(current);
                    } else if (current instanceof VariableExprToken) {
                        for(Token modifier : modifiers){
                            if (isTokenClass(modifier, FinalStmtToken.class, AbstractStmtToken.class))
                                unexpectedToken(modifier);
                        }
                        properties.addAll(processProperty((VariableExprToken)current, modifiers, iterator));
                        modifiers.clear();
                    } else if (current instanceof FunctionStmtToken) {
                        FunctionStmtToken function = analyzer.generator(FunctionGenerator.class).getToken(current, iterator);
                        MethodStmtToken method = new MethodStmtToken(function);
                        method.setClazz(result);

                        for (Token modifier : modifiers){
                            if (modifier instanceof AbstractStmtToken)
                                method.setAbstract(true);
                            else if (modifier instanceof StaticExprToken)
                                method.setStatic(true);
                            else if (modifier instanceof FinalStmtToken){
                                method.setFinal(true);
                            } else if (modifier instanceof PublicStmtToken){
                                if (method.getModifier() != null)
                                    unexpectedToken(modifier);

                                method.setModifier(Modifier.PUBLIC);
                            } else if (modifier instanceof PrivateStmtToken){
                                if (method.getModifier() != null)
                                    unexpectedToken(modifier);

                                method.setModifier(Modifier.PRIVATE);
                            } else if (modifier instanceof ProtectedStmtToken)  {
                                if (method.getModifier() != null)
                                    unexpectedToken(modifier);

                                method.setModifier(Modifier.PROTECTED);
                            }
                        }
                        if (method.getModifier() == null)
                            method.setModifier(Modifier.PUBLIC);

                        methods.add(method);
                        modifiers.clear();
                    } else if (isClosedBrace(current, BraceExprToken.Kind.BLOCK)){
                        break;
                    } else
                        unexpectedToken(current);
                }

                result.setConstants(constants);
                result.setMethods(methods);
                result.setProperties(properties);
                analyzer.setClazz(null);
                return;
            }
        }

        unexpectedToken(token, "{");
    }

    @SuppressWarnings("unchecked")
    protected ClassStmtToken processDefine(Token current, ListIterator<Token> iterator){
        if (isTokenClass(current, FinalStmtToken.class, AbstractStmtToken.class)){
            Token next = nextToken(iterator);
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
            if (analyzer.getClazz() != null)
                unexpectedToken(current);

            analyzer.setClazz(result);
            result.setNamespace(analyzer.getNamespace());

            processName(result, iterator);
            processExtends(result, iterator);
            processImplements(result, iterator);
            processBody(result, iterator);
            analyzer.setClazz(null);
        }

        return result;
    }
}
