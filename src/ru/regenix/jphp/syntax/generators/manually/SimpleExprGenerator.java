package ru.regenix.jphp.syntax.generators.manually;


import ru.regenix.jphp.common.Callback;
import ru.regenix.jphp.common.Separator;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.ExprGenerator;
import ru.regenix.jphp.syntax.generators.FunctionGenerator;
import ru.regenix.jphp.syntax.generators.Generator;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.BreakToken;
import ru.regenix.jphp.tokenizer.token.ColonToken;
import ru.regenix.jphp.tokenizer.token.SemicolonToken;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.*;
import ru.regenix.jphp.tokenizer.token.expr.operator.*;
import ru.regenix.jphp.tokenizer.token.expr.operator.cast.CastExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.cast.UnsetCastExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.*;
import ru.regenix.jphp.tokenizer.token.stmt.AsStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.FunctionStmtToken;

import java.util.*;

public class SimpleExprGenerator extends Generator<ExprStmtToken> {

    private boolean isRef = false;
    private boolean canStartByReference = false;
    private static final Set<String> dynamicLocalFunctions = new HashSet<String>(){{
        add("extract");
        add("compact");
        add("get_defined_vars");
        add("eval");
    }};

    public SimpleExprGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    public SimpleExprGenerator setCanStartByReference(boolean canStartByReference) {
        this.canStartByReference = canStartByReference;
        return this;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    protected Token processClosure(Token current, Token next, ListIterator<Token> iterator){
        FunctionStmtToken functionStmtToken = analyzer.generator(FunctionGenerator.class).getToken(
            current, iterator, true
        );

        if (functionStmtToken.getName() == null){
            //unexpectedToken(functionStmtToken.getName());

            ClosureStmtToken result = new ClosureStmtToken(current.getMeta());
            result.setFunction(functionStmtToken);
            analyzer.registerClosure(result);

            return result;
        } else {
            analyzer.registerFunction(functionStmtToken);
            return functionStmtToken;
        }
    }

    protected ListExprToken processList(Token current, ListIterator<Token> iterator, List<Integer> indexes,
                                        BraceExprToken.Kind closedBraceKind, int braceOpened){
        ListExprToken result = (ListExprToken)current;

        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, "(");

        Token prev = null;
        int i = 0;
        while (true){
            next = nextToken(iterator);
            if (next instanceof VariableExprToken){
                if (prev != null && !(prev instanceof CommaToken))
                    unexpectedToken(next);

                analyzer.getLocalScope().add((VariableExprToken)next);
                result.addVariable(((VariableExprToken) next), i, indexes);
            } else if (next instanceof ListExprToken){
                if (prev != null && !(prev instanceof CommaToken))
                    unexpectedToken(next);

                List<Integer> indexes_ = new ArrayList<Integer>();
                if (indexes != null)
                    indexes_.addAll(indexes);
                indexes_.add(i);

                ListExprToken tmp = processList(next, iterator, indexes_, null, -1);
                result.addList(tmp);
            } else if (next instanceof CommaToken){
                // skip
                i++;
            } else if (isClosedBrace(next, BraceExprToken.Kind.SIMPLE)){
                break;
            }

            prev = next;
        }

        if (braceOpened != -1){
            next = nextToken(iterator);
            if (!(next instanceof AssignExprToken))
                unexpectedToken(next, "=");

            ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                    nextToken(iterator), iterator, closedBraceKind
            );
            result.setValue(value);
        }

        return result;
    }

    protected DieExprToken processDie(Token current, Token next, ListIterator<Token> iterator){
        DieExprToken die = (DieExprToken)current;
        if (isOpenedBrace(next, BraceExprToken.Kind.SIMPLE)){
            die.setValue(
                    analyzer.generator(ExprGenerator.class).getInBraces(BraceExprToken.Kind.SIMPLE, iterator)
            );
        }

        return die;
    }

    protected EmptyExprToken processEmpty(Token current, ListIterator<Token> iterator){
        ExprStmtToken value = analyzer.generator(ExprGenerator.class).getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        if (value == null)
            unexpectedToken(iterator.previous());

        assert value != null;
        if (!(value.getSingle() instanceof VariableValueExprToken))
            unexpectedToken(value, "$var");

        Token last = value.getLast();
        if (last instanceof DynamicAccessExprToken){
            last = new DynamicAccessEmptyExprToken((DynamicAccessExprToken)last);
            value.getTokens().set(value.getTokens().size() - 1, last);
        }

        EmptyExprToken result = (EmptyExprToken)current;
        result.setValue(value);
        return result;
    }

    protected IssetExprToken processIsset(Token previous, Token current, ListIterator<Token> iterator){
        Token next = nextTokenAndPrev(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, "(");

        CallExprToken call = processCall(current, nextToken(iterator), iterator);

        for(ExprStmtToken param : call.getParameters()){
            List<Token> tokens = param.getTokens();
            Token last = tokens.get(tokens.size() - 1);
            Token newToken = null;

            if (!(param.getSingle() instanceof VariableValueExprToken))
                unexpectedToken(param.getSingle());

            if (last instanceof DynamicAccessExprToken){
                newToken = new DynamicAccessIssetExprToken((DynamicAccessExprToken)last);
                if (analyzer.getClazz() != null && !"__isset".equals(analyzer.getFunction().getFulledName())){
                    ((DynamicAccessIssetExprToken)newToken).setWithMagic(false);
                }
            }

            if (newToken != null)
                tokens.set(tokens.size() - 1, newToken);
        }

        IssetExprToken result = (IssetExprToken)current;
        result.setParameters(call.getParameters());
        return result;
    }

    protected UnsetExprToken processUnset(Token previous, Token current, ListIterator<Token> iterator){
        Token next = nextTokenAndPrev(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, "(");

        CallExprToken call = processCall(current, nextToken(iterator), iterator);

        for(ExprStmtToken param : call.getParameters()){
            List<Token> tokens = param.getTokens();
            Token last = tokens.get(tokens.size() - 1);
            Token newToken = null;

            if (param.getSingle() instanceof StaticAccessExprToken && ((StaticAccessExprToken) param.getSingle()).isGetStaticField()) {
                // allow class::$var
            } else if (!(param.getSingle() instanceof VariableValueExprToken))
                unexpectedToken(param);

            if (last instanceof VariableExprToken || last instanceof GetVarExprToken){
                newToken = last;
                // nop
            } else if (last instanceof ArrayGetExprToken){
                ArrayGetUnsetExprToken el = new ArrayGetUnsetExprToken(last.getMeta());
                el.setParameters(((ArrayGetExprToken) last).getParameters());
                newToken = el;
            } else if (last instanceof DynamicAccessExprToken){
                newToken = new DynamicAccessUnsetExprToken((DynamicAccessExprToken)last);
            } else if (last instanceof StaticAccessExprToken){
                newToken = new StaticAccessUnsetExprToken((StaticAccessExprToken)last);
            } else
                unexpectedToken(last);

            tokens.set(tokens.size() - 1, newToken);
        }

        UnsetExprToken result = (UnsetExprToken)current;
        result.setParameters(call.getParameters());
        return result;
    }

    protected CallExprToken processCall(Token previous, Token current, ListIterator<Token> iterator){
        ExprStmtToken param;

        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
        do {
            param = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE);

            if (param != null) {
                parameters.add(param);
                if (param.isSingle()){
                    if (param.getTokens().get(0) instanceof VariableExprToken) {
                        if (analyzer.getFunction() != null)
                            analyzer.getFunction().getPassedLocal().add((VariableExprToken)param.getTokens().get(0));
                    }
                }
            }

        } while (param != null);
        nextToken(iterator);

        CallExprToken result = new CallExprToken(TokenMeta.of(previous, current));
        if (previous instanceof ValueExprToken) {
            result.setName(analyzer.getRealName((ValueExprToken)previous));
            if (analyzer.getFunction() != null){
                if (result.getName() instanceof NameToken)
                if (dynamicLocalFunctions.contains(((NameToken) result.getName()).getName().toLowerCase()))
                    analyzer.getFunction().setDynamicLocal(true);
            }
        } else {
            if (previous instanceof DynamicAccessExprToken) {
                result.setName((ExprToken)previous);
            } else
                result.setName(null);
        }

        result.setParameters(parameters);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setCallsExist(true);
        }

        return result;
    }

    protected ImportExprToken processImport(Token current, Token next, ListIterator<Token> iterator,
                                              BraceExprToken.Kind closedBrace, int braceOpened){
        ImportExprToken result = (ImportExprToken)current;
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                nextToken(iterator), iterator, closedBrace
        );
        result.setValue(value);

        if (analyzer.getFunction() != null)
            analyzer.getFunction().setDynamicLocal(true);

        return result;
    }

    protected CallExprToken processPrint(Token current, Token next, ListIterator<Token> iterator,
                                         BraceExprToken.Kind closedBrace, int braceOpened){
        CallExprToken callExprToken = new CallExprToken(current.getMeta());
        callExprToken.setName((ExprToken)current);

        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                nextToken(iterator), iterator, closedBrace
        );

        if (value == null)
            unexpectedToken(iterator.previous());

        callExprToken.setParameters(Arrays.asList(value));
        return callExprToken;
    }

    protected DynamicAccessExprToken processDynamicAccess(Token current, Token next, Token prev, ListIterator<Token> iterator,
            BraceExprToken.Kind closedBraceKind, int braceOpened){
        DynamicAccessExprToken result = (DynamicAccessExprToken)current;
        if (next instanceof NameToken || next instanceof VariableExprToken){
            result.setField((ValueExprToken) next);
            iterator.next();
        } else if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){
            ExprStmtToken name = analyzer.generator(ExprGenerator.class).getInBraces(
                    BraceExprToken.Kind.BLOCK, iterator
            );
            result.setFieldExpr(name);
        }

        if (iterator.hasNext()){
            next = iterator.next();
            if (next instanceof AssignableOperatorToken){
                DynamicAccessAssignExprToken dResult = new DynamicAccessAssignExprToken(result);
                dResult.setAssignOperator(next);

                analyzer.generator(SimpleExprGenerator.class);
                ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class)
                        .setCanStartByReference(true).getToken(
                                nextToken(iterator), iterator, Separator.SEMICOLON, closedBraceKind
                        );
                if (closedBraceKind == null || braceOpened < 1)
                    iterator.previous();
                dResult.setValue(value);
                return dResult;
            }
            iterator.previous();
        }

        return result;
    }

    protected GetVarExprToken processVarVar(Token current, Token next, ListIterator<Token> iterator){
        ExprStmtToken name = null;
        if (next instanceof VariableExprToken){ // $$var
            name = new ExprStmtToken(next);
            nextToken(iterator);
        } else if (next instanceof DollarExprToken){ // $$$var
            current = nextToken(iterator);
            next    = nextToken(iterator);
            name    = new ExprStmtToken(processVarVar(current, next, iterator));
            iterator.previous();
        } else if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){ // ${var}
            name = analyzer.generator(ExprGenerator.class).getInBraces(
                    BraceExprToken.Kind.BLOCK, iterator
            );
        }

        if (name == null)
            unexpectedToken(next);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setDynamicLocal(true);
            analyzer.getFunction().setVarsExists(true);
        }

        GetVarExprToken result = new GetVarExprToken(TokenMeta.of(current, name));
        result.setName(name);
        return result;
    }

    protected Token processValueIfElse(ValueIfElseToken current, Token next, ListIterator<Token> iterator,
                                       BraceExprToken.Kind closedBrace, int braceOpened){
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getToken(
                nextToken(iterator), iterator, Separator.COLON, closedBrace
        );
        /*if (closedBrace == null || braceOpened < 1)
            iterator.previous();*/
        current.setValue(value);

        if (!((next = iterator.previous()) instanceof ColonToken))
            unexpectedToken(next, ":");

        iterator.next();
        ExprStmtToken alternative = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                nextToken(iterator), iterator, BraceExprToken.Kind.ANY
        );

        if (alternative == null)
            unexpectedToken(iterator.next());

        current.setAlternative(alternative);
        return current;
    }

    protected Token processNew(Token current, ListIterator<Token> iterator){
        NewExprToken result = (NewExprToken)current;
        Token next = nextToken(iterator);
        if (next instanceof NameToken){
            FulledNameToken nameToken = analyzer.getRealName((NameToken)next);
            result.setName(nameToken);
        } else if (next instanceof VariableExprToken){
            result.setName((VariableExprToken)next);
        } else if (next instanceof StaticExprToken){
            result.setName((StaticExprToken)next);
        } else if (next instanceof SelfExprToken){
            if (analyzer.getClazz() == null)
                unexpectedToken(next);

            result.setName(new FulledNameToken(next.getMeta(), new ArrayList<Token>(){{
                if (analyzer.getClazz().getNamespace().getName() != null)
                    addAll(analyzer.getClazz().getNamespace().getName().getNames());
                add(analyzer.getClazz().getName());
            }}));

        } else
            unexpectedToken(next);

        next = nextToken(iterator);
        if (isOpenedBrace(next, BraceExprToken.Kind.SIMPLE)){
            ExprStmtToken param;
            List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
            do {
                param = analyzer.generator(SimpleExprGenerator.class)
                        .getToken(nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE);

                if (param != null)
                    parameters.add(param);
            } while (param != null);
            nextToken(iterator);
            result.setParameters(parameters);
        } else {
            result.setParameters(new ArrayList<ExprStmtToken>());
            iterator.previous();
        }

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setCallsExist(true);
        }

        return result;
    }

    protected Token processString(StringExprToken string){
        if (string.getSegments().isEmpty()){
            return string;
        }

        List<Token> tokens = new ArrayList<Token>();
        int i = 0;
        String value = string.getValue();
        TokenMeta meta = string.getMeta();

        for(StringExprToken.Segment segment : string.getSegments()){
            String prev = value.substring(i, segment.from);
            if (!prev.isEmpty()) {
                StringExprToken item = new StringExprToken(new TokenMeta(
                        prev, meta.getStartLine() + i, meta.getEndLine(), meta.getStartLine(), meta.getEndLine()
                ), StringExprToken.Quote.SINGLE);

                tokens.add(item);
            }

            String dynamic = value.substring(segment.from, segment.to);
            if (!segment.isVariable)
                dynamic = dynamic.substring(1, dynamic.length() - 1);

            Tokenizer tokenizer = new Tokenizer(dynamic + ";", analyzer.getContext());

            try {
                SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(analyzer.getEnvironment(), tokenizer, analyzer.getFunction());
                List<Token> tree = syntaxAnalyzer.getTree();
                analyzer.getLocalScope().addAll(syntaxAnalyzer.getLocalScope());

                assert tree.size() > 0;

                Token item = tree.get(0);
                if (!(item instanceof ExprStmtToken))
                    unexpectedToken(item);

                ExprStmtToken expr = (ExprStmtToken)item;
                if (expr.isSingle()){
                    tokens.add(expr.getSingle());
                } else
                    tokens.add(expr);
            } catch (ParseException e){
                TraceInfo oldTrace = e.getTraceInfo();
                e.setTraceInfo(new TraceInfo(
                                analyzer.getContext(),
                                meta.getStartLine() + oldTrace.getStartLine(),
                                meta.getEndLine() + oldTrace.getEndLine(),
                                meta.getStartLine() + oldTrace.getStartLine(),
                                meta.getEndLine() + oldTrace.getEndLine()
                ));
                throw e;
            }

            i = segment.to;
        }

        String prev = value.substring(i);
        if (!prev.isEmpty()){
            StringExprToken item = new StringExprToken(new TokenMeta(
                    prev, meta.getStartLine() + i, meta.getEndLine(), meta.getStartLine(), meta.getEndLine()
            ), StringExprToken.Quote.SINGLE);

            tokens.add(item);
        }

        return new StringBuilderExprToken(meta, tokens);
    }

    protected Token processSimpleToken(Token current, Token previous, Token next, ListIterator<Token> iterator,
                                       BraceExprToken.Kind closedBraceKind, int braceOpened){
        if (current instanceof DynamicAccessExprToken){
            return processDynamicAccess(current, next, previous, iterator, closedBraceKind, braceOpened);
        }

        if (current instanceof OperatorExprToken) {
            isRef = false;
        }

        if (current instanceof InstanceofExprToken){
            if (next instanceof NameToken || next instanceof VariableExprToken){
                nextToken(iterator);
                InstanceofExprToken instanceOf = (InstanceofExprToken)current;
                if (next instanceof NameToken)
                    instanceOf.setWhat( analyzer.getRealName((NameToken)next) );
                else
                    instanceOf.setWhatVariable( (VariableExprToken)next );
                return instanceOf;
            } else
                unexpectedToken(next, TokenType.T_STRING);
        }

        if (current instanceof ImportExprToken)
            return processImport(current, next, iterator, closedBraceKind, braceOpened);

        if (current instanceof PrintNameToken){
            return processPrint(current, next, iterator, closedBraceKind, braceOpened);
        }

        if (current instanceof NewExprToken)
            return processNew(current, iterator);

        if (current instanceof DollarExprToken){
            return processVarVar(current, next, iterator);
        }

        if (current instanceof VariableExprToken){
            analyzer.getLocalScope().add((VariableExprToken) current);
            if (analyzer.getFunction() != null)
                analyzer.getFunction().setVarsExists(true);
        }

        if (current instanceof ValueIfElseToken){
            return processValueIfElse((ValueIfElseToken)current, next, iterator, closedBraceKind, braceOpened);
        }

        // &
        if (current instanceof AmpersandRefToken){
            /*if (previous == null)
                unexpectedToken(current);*/

            isRef = true;
            if (next instanceof VariableExprToken)
                if (analyzer.getFunction() != null)
                    analyzer.getFunction().getRefLocal().add((VariableExprToken)next);

            if (previous instanceof AssignExprToken || previous instanceof KeyValueExprToken
                    || (canStartByReference && previous == null)) {
                if (previous instanceof AssignExprToken)
                    ((AssignExprToken) previous).setAsReference(true);

                iterator.previous();
                Token token = iterator.previous(); // =
                if (iterator.hasPrevious()) {
                    token = iterator.previous();
                    if (token instanceof VariableExprToken && analyzer.getFunction() != null){
                        analyzer.getFunction().getRefLocal().add((VariableExprToken)token);
                       // analyzer.getFunction().getUnstableLocal().add((VariableExprToken)token); TODO: check is needed?
                    }

                    iterator.next();
                }
                iterator.next();
                iterator.next();
                if (!(next instanceof ValueExprToken))
                    unexpectedToken(token);

            } else {
                if (next instanceof ValueExprToken)
                    return new AndExprToken(current.getMeta());
                else
                    unexpectedToken(current);
            }

            return current;
        }
        // &$var, &$obj->prop['x'], &class::$prop, &$arr['x'], &call()->x;
        if (previous instanceof AmpersandRefToken){
            if (current instanceof VariableExprToken)
                if (analyzer.getFunction() != null)
                    analyzer.getFunction().getRefLocal().add((VariableExprToken)current);
        }

        if ((current instanceof MinusExprToken || current instanceof PlusExprToken)
                && (next instanceof IntegerExprToken || next instanceof DoubleExprToken
                        || next instanceof HexExprValue)){

            if (!(previous instanceof ValueExprToken
                    || previous instanceof ArrayGetExprToken || previous instanceof DynamicAccessExprToken
                    || isClosedBrace(previous, BraceExprToken.Kind.SIMPLE))){
                iterator.next();
                // if it minus
                if (current instanceof MinusExprToken){
                    if (next instanceof IntegerExprToken){
                        return new IntegerExprToken(TokenMeta.of(current, next));
                    } else if (next instanceof DoubleExprToken){
                        return new DoubleExprToken(TokenMeta.of(current, next));
                    } else {
                        return new HexExprValue(TokenMeta.of(current, next));
                    }
                }

                // if it plus nothing
                return next;
            }
        }

        if (current instanceof MinusExprToken){
            if (!(previous instanceof ValueExprToken
                    || previous instanceof ArrayGetExprToken
                    || previous instanceof DynamicAccessExprToken
                    || isClosedBrace(previous))){
                return new UnarMinusExprToken(current.getMeta());
            }
        }

        if (current instanceof LogicOperatorExprToken){
            if (next == null)
                unexpectedToken(current);

            final LogicOperatorExprToken logic = (LogicOperatorExprToken)current;
            ExprStmtToken result = analyzer.generator(SimpleExprGenerator.class).getToken(
                    nextToken(iterator), iterator, null,
                    braceOpened > 0 ? BraceExprToken.Kind.SIMPLE : closedBraceKind,
                    new Callback<Boolean, Token>() {
                        @Override
                        public Boolean call(Token param) {
                            if (param instanceof OperatorExprToken &&
                                    !(param instanceof LogicOperatorExprToken)){
                                if (logic.getPriority() < ((OperatorExprToken) param).getPriority())
                                    return true;
                            }
                            return false;
                        }
                    }
            );

            if (closedBraceKind == null && braceOpened < 1)
                iterator.previous();

            logic.setRightValue(result);
            return logic;
        }

        if (next instanceof StaticAccessExprToken){
            if (current instanceof NameToken || current instanceof VariableExprToken
                    || current instanceof SelfExprToken || current instanceof StaticExprToken
                    || current instanceof ParentExprToken){
                StaticAccessExprToken result = (StaticAccessExprToken)next;
                ValueExprToken clazz = (ValueExprToken)current;
                if (clazz instanceof NameToken){
                    clazz = analyzer.getRealName((NameToken)clazz);
                } else if (clazz instanceof SelfExprToken){
                    if (analyzer.getClazz() == null)
                        unexpectedToken(clazz);

                    clazz = new FulledNameToken(clazz.getMeta(), new ArrayList<Token>(){{
                        if (analyzer.getClazz().getNamespace().getName() != null)
                            addAll(analyzer.getClazz().getNamespace().getName().getNames());
                        add(analyzer.getClazz().getName());
                    }});
                }

                result.setClazz(clazz);
                nextToken(iterator);

                next = nextToken(iterator);
                if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){
                    ExprStmtToken expr = getToken(nextToken(iterator), iterator, false, BraceExprToken.Kind.BLOCK);
                    result.setFieldExpr(expr);
                    nextAndExpected(iterator, BraceExprToken.class);
                } else if (next instanceof NameToken || next instanceof VariableExprToken){
                    result.setField((ValueExprToken)next);
                } else
                    unexpectedToken(next);

                return result;
            } else
                unexpectedToken(current);
        }

        if (current instanceof StringExprToken){
            return processString((StringExprToken) current);
        }

        return null;
    }

    protected Token processNewArray(Token current, ListIterator<Token> iterator){
        ArrayExprToken result = new ArrayExprToken(current.getMeta());
        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();

        Token next;
        BraceExprToken.Kind braceKind;
        if (isOpenedBrace(current, BraceExprToken.Kind.ARRAY)) {
            next = current;
            braceKind = BraceExprToken.Kind.ARRAY;
        } else {
            next = nextToken(iterator);
            if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
                unexpectedToken(next, "(");
            braceKind = BraceExprToken.Kind.SIMPLE;
        }

        do {
            SimpleExprGenerator generator = analyzer.generator(SimpleExprGenerator.class);
            generator.setCanStartByReference(true);

            ExprStmtToken argument = generator.getToken(nextToken(iterator), iterator, Separator.COMMA, braceKind);
            if (argument == null)
                break;

            parameters.add(argument);
        } while (true);
        nextToken(iterator); // skip )

        result.setParameters(parameters);
        return result;
    }

    protected Token processArrayToken(Token previous, Token current, ListIterator<Token> iterator){
        BraceExprToken.Kind braceKind = BraceExprToken.Kind.ARRAY;
        Separator separator = Separator.ARRAY;
        if (isOpenedBrace(current, BraceExprToken.Kind.BLOCK)){
            braceKind = BraceExprToken.Kind.BLOCK;
            separator = Separator.ARRAY_BLOCK;
        }

        if (previous instanceof VariableExprToken) {
            if (analyzer.getFunction() != null){
                analyzer.getFunction().getArrayAccessLocal().add((VariableExprToken)previous);
            }
        }

        Token next = nextToken(iterator);
        if (isClosedBrace(next, braceKind)){
            if (nextToken(iterator) instanceof AssignableOperatorToken) {
                iterator.previous();
                return new ArrayPushExprToken(TokenMeta.of(current, next));
            } else
                unexpectedToken(next);
        } else
            iterator.previous();

        ExprStmtToken param;
        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
        boolean lastPush = false;
        do {
            param = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, separator, braceKind);

            if (param != null) {
                parameters.add(param);

                if (iterator.hasNext()){
                    Token tmp = nextToken(iterator);
                    if (isOpenedBrace(tmp, BraceExprToken.Kind.ARRAY)){
                        braceKind = BraceExprToken.Kind.ARRAY;
                        separator = Separator.ARRAY;
                        continue;
                    } else if (isOpenedBrace(tmp, BraceExprToken.Kind.BLOCK)){
                        braceKind = BraceExprToken.Kind.BLOCK;
                        separator = Separator.ARRAY_BLOCK;
                        continue;
                    }
                    iterator.previous();
                    break;
                }
            }  else {
                Token prev = iterator.previous();
                if (isClosedBrace(prev, braceKind)){
                    iterator.previous();
                    lastPush = true;
                } else
                    iterator.next();
            }

        } while (param != null);
        //nextToken(iterator); // skip ]

        ArrayGetExprToken result;
        result = new ArrayGetExprToken(current.getMeta());
        result.setParameters(parameters);

        if (isRef){
            result = new ArrayGetRefExprToken(result);
            ((ArrayGetRefExprToken)result).setShortcut(true);
        } else if (iterator.hasNext()){
            next = iterator.next();
            if (next instanceof AssignableOperatorToken || lastPush){
                result = new ArrayGetRefExprToken(result);
            }
            iterator.previous();
        }

        return result;
    }

    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  boolean commaSeparator, BraceExprToken.Kind closedBraceKind) {
        return getToken(
                current, iterator, commaSeparator ? Separator.COMMA : Separator.SEMICOLON, closedBraceKind,
                null
        );
    }

    public ExprStmtToken getNextExpression(Token current, ListIterator<Token> iterator, BraceExprToken.Kind closedBraceKind){
        ExprStmtToken value = getToken(
                current, iterator, Separator.SEMICOLON, BraceExprToken.Kind.ANY
        );
        if (!isBreak(iterator.previous())){
            iterator.next();
        }
        return value;
    }

    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Separator separator, BraceExprToken.Kind closedBraceKind) {
        return getToken(
                current, iterator, separator, closedBraceKind,
                null
        );
    }

    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Separator separator, BraceExprToken.Kind closedBraceKind,
                                  Callback<Boolean, Token> breakCallback) {
        isRef = false;

        List<Token> tokens = new ArrayList<Token>();
        Token previous = null;
        Token next = iterator.hasNext() ? iterator.next() : null;
        if (next != null)
            iterator.previous();

        int braceOpened = 0;
        boolean needBreak = false;
        do {
            if (breakCallback != null && current != null && breakCallback.call(current)){
                break;
            }
            if (isOpenedBrace(current, BraceExprToken.Kind.SIMPLE)){
                boolean isFunc = false;
                if (previous instanceof NameToken && previous.getMeta().getWord().equalsIgnoreCase("array")){
                    iterator.previous();
                    tokens.set(tokens.size() - 1, current = processNewArray(previous, iterator));
                } else {
                    if (previous instanceof NameToken
                            || previous instanceof VariableExprToken
                            || previous instanceof ClosureStmtToken
                            || previous instanceof ArrayGetExprToken)
                        isFunc = true;
                    else if (previous instanceof StaticAccessExprToken){
                        isFunc = true; // !((StaticAccessExprToken)previous).isGetStaticField(); TODO check it!
                    } else if (previous instanceof DynamicAccessExprToken){
                        isFunc = true;
                    }

                    if (isFunc){
                        CallExprToken call = processCall(previous, current, iterator);
                        if (call.getName() != null) {
                            current = call;
                            tokens.set(tokens.size() - 1, call);
                        } else {
                            tokens.add(current = new CallOperatorToken(call));
                        }
                    } else {
                        if (needBreak)
                            unexpectedToken(current);

                        braceOpened += 1;
                        tokens.add(current);
                    }
                }
            } else if (braceOpened > 0 && isClosedBrace(current, BraceExprToken.Kind.SIMPLE)){
                braceOpened -= 1;
                tokens.add(current);
                if (isOpenedBrace(previous, BraceExprToken.Kind.SIMPLE))
                    unexpectedToken(current);
            } else if (isOpenedBrace(current, BraceExprToken.Kind.ARRAY)
                    || isOpenedBrace(current, BraceExprToken.Kind.BLOCK)){
                if (isTokenClass(previous,
                        NameToken.class,
                        VariableExprToken.class,
                        GetVarExprToken.class,
                        CallExprToken.class,
                        ArrayGetExprToken.class,
                        DynamicAccessExprToken.class,
                        StringExprToken.class,
                        StringBuilderExprToken.class,
                        CallOperatorToken.class)){
                    // array
                    tokens.add(current = processArrayToken(previous, current, iterator));
                } else if (previous instanceof OperatorExprToken
                        || previous == null
                        || isOpenedBrace(previous, BraceExprToken.Kind.SIMPLE)
                        || isOpenedBrace(previous, BraceExprToken.Kind.BLOCK)) {
                    tokens.add(current = processNewArray(current, iterator));
                } else
                    unexpectedToken(current);
            } else if (braceOpened == 0 && isClosedBrace(current, BraceExprToken.Kind.ARRAY)){
                if (separator == Separator.ARRAY)
                    break;
                if (closedBraceKind == BraceExprToken.Kind.ARRAY){
                    //if (tokens.isEmpty())
                    iterator.previous();
                    break;
                }
                unexpectedToken(current);
            } else if (separator == Separator.ARRAY_BLOCK
                    && braceOpened == 0 && isClosedBrace(current, BraceExprToken.Kind.BLOCK)){
                break;
            } else if (current instanceof FunctionStmtToken){
                current = processClosure(current, next, iterator);
                tokens.add(current);
            } else if (current instanceof ListExprToken){
                current = processList(current, iterator, null, closedBraceKind, braceOpened);
                tokens.add(current);
            } else if (current instanceof DieExprToken){
                processDie(current, next, iterator);
                tokens.add(current);
            } else if (current instanceof EmptyExprToken){
                processEmpty(current, iterator);
                tokens.add(current);
            } else if (current instanceof IssetExprToken){
                processIsset(previous, current, iterator);
                tokens.add(current);
            } else if (current instanceof UnsetExprToken){
                if (isOpenedBrace(previous, BraceExprToken.Kind.SIMPLE)
                        && isClosedBrace(next, BraceExprToken.Kind.SIMPLE)){
                    current = new UnsetCastExprToken(current.getMeta());
                    tokens.set(tokens.size() - 1, current);
                    iterator.next();
                    braceOpened--;
                } else {
                    if (previous != null)
                        unexpectedToken(current);

                    processUnset(previous, current, iterator);
                    tokens.add(current);
                    needBreak = true;
                }
            } else if (current instanceof CommaToken){
                if (separator == Separator.COMMA || separator == Separator.COMMA_OR_SEMICOLON){
                    if (tokens.isEmpty())
                        unexpectedToken(current);
                    break;
                } else {
                    unexpectedToken(current);
                }
            } else if (current instanceof AsStmtToken){
                if (separator == Separator.AS)
                    break;
                unexpectedToken(current);
            } else if (isClosedBrace(current, closedBraceKind)){
                iterator.previous();
                break;
            } else if (current instanceof BreakToken){
                break;
            } else if (current instanceof ColonToken){
                if (separator == Separator.COLON) {
                    if (tokens.isEmpty())
                        unexpectedToken(current);
                    break;
                }
                unexpectedToken(current);
            } else if (current instanceof SemicolonToken){ // TODO refactor!
                if (separator == Separator.SEMICOLON || separator == Separator.COMMA_OR_SEMICOLON) {
                    if (tokens.isEmpty())
                        unexpectedToken(current);
                    break;
                }

                if (separator == Separator.COMMA || closedBraceKind != null || tokens.isEmpty())
                    unexpectedToken(current);
                break;
            } else if (current instanceof BraceExprToken){
                if (closedBraceKind == BraceExprToken.Kind.ANY && isClosedBrace(current)) {
                    iterator.previous();
                    break;
                }
                unexpectedToken(current);
            } else if (current instanceof ArrayExprToken){
                if (needBreak)
                    unexpectedToken(current);

                tokens.add(current = processNewArray(current, iterator));
            } else if (current instanceof ExprToken) {
                if (needBreak)
                    unexpectedToken(current);

                CastExprToken cast = null;
                if (current instanceof NameToken && isOpenedBrace(previous, BraceExprToken.Kind.SIMPLE)
                        && isClosedBrace(next, BraceExprToken.Kind.SIMPLE)){
                    cast = CastExprToken.valueOf(((NameToken)current).getName(), current.getMeta());
                    if (cast != null){
                        current = cast;
                        iterator.next();
                        braceOpened--;
                        tokens.set(tokens.size() - 1, current);
                    }
                }

                if (cast == null){
                    Token token = processSimpleToken(current, previous, next, iterator, closedBraceKind, braceOpened);
                    if (token != null)
                        current = token;

                    tokens.add(current);
                }
            } else
                unexpectedToken(current);

            previous = current;
            if (iterator.hasNext()){
                current = nextToken(iterator);
                next = iterator.hasNext() ? iterator.next() : null;
                if (next != null)
                    iterator.previous();
            } else
                current = null;

            if (current == null)
                nextToken(iterator);

        } while (current != null);

        if (tokens.isEmpty())
            return null;

        if (braceOpened != 0)
            unexpectedToken(iterator.previous());

        return new ExprStmtToken(tokens);
    }

    @Override
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator){
        return getToken(current, iterator, false, null);
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
