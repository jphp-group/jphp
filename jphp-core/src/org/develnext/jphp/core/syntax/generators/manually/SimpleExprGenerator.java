package org.develnext.jphp.core.syntax.generators.manually;


import org.develnext.jphp.core.common.Separator;
import org.develnext.jphp.core.compiler.common.ASMExpression;
import org.develnext.jphp.core.syntax.ExpressionInfo;
import org.develnext.jphp.core.syntax.Scope;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.syntax.generators.ExprGenerator;
import org.develnext.jphp.core.syntax.generators.FunctionGenerator;
import org.develnext.jphp.core.syntax.generators.Generator;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.BreakToken;
import org.develnext.jphp.core.tokenizer.token.ColonToken;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.*;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.operator.cast.CastExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.cast.UnsetCastExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.MacroToken;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import php.runtime.common.Callback;
import php.runtime.common.Messages;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.FatalException;
import php.runtime.exceptions.ParseException;
import php.runtime.exceptions.support.ErrorType;

import java.util.*;

import static org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken.Kind.ARRAY;
import static org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken.Kind.BLOCK;
import static org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken.Kind.SIMPLE;

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
            result.setOwnerClass(analyzer.getClazz());
            analyzer.registerClosure(result);

            return result;
        } else {
            analyzer.registerFunction(functionStmtToken);
            return functionStmtToken;
        }
    }

    public ListExprToken processSingleList(Token current, ListIterator<Token> iterator) {
        return processList(current, iterator, null, null, -1);
    }

    @SuppressWarnings("unchecked")
    protected ListExprToken processList(Token current, ListIterator<Token> iterator, List indexes,
                                        BraceExprToken.Kind closedBraceKind, int braceOpened) {

        boolean arraySyntax = isOpenedBrace(current, BraceExprToken.Kind.ARRAY);

        ListExprToken result = arraySyntax ? new ListExprToken(current.getMeta()) : (ListExprToken)current;

        Token next;

        if (!arraySyntax) {
            next = nextToken(iterator);
            if (!isOpenedBrace(next, SIMPLE)) {
                unexpectedToken(next, "(");
            }
        }

        int i = 0;
        boolean withKeys = false;
        ExprStmtToken keyExpr = null;

        while (true){
            next = nextToken(iterator);
            if (next instanceof ListExprToken || isOpenedBrace(next, ARRAY)) {
                if ((arraySyntax && next instanceof ListExprToken) || (!arraySyntax && isOpenedBrace(next, ARRAY))) {
                    analyzer.getEnvironment().error(
                            next.toTraceInfo(analyzer.getContext()),
                            ErrorType.E_ERROR,
                            Messages.ERR_CANNOT_MIX_ARRAY_AND_LIST
                    );
                }

                List indexes_ = new ArrayList();
                if (indexes != null) {
                    indexes_.addAll(indexes);
                }

                indexes_.add(keyExpr == null ? i : keyExpr);

                ListExprToken tmp = processList(next, iterator, indexes_, null, -1);
                result.addList(tmp);

                if (nextTokenAndPrev(iterator) instanceof CommaToken) {
                    iterator.next();
                }

                i++;
            } else if (isClosedBrace(next, arraySyntax ? ARRAY : SIMPLE)){
                break;
            } else if (next instanceof CommaToken){
                i++;
            } else {
                SimpleExprGenerator generator = analyzer.generator(SimpleExprGenerator.class);
                int currIndex = iterator.nextIndex() - 1;

                ExprStmtToken var = generator.getToken(next, iterator, Separator.COMMA, arraySyntax ? ARRAY :  SIMPLE);

                int k = 0;
                keyExpr = null;
                ExprStmtToken valueExpr = var;

                for (Token token : var.getTokens()) {
                    if (token instanceof KeyValueExprToken) {
                        keyExpr = new ExprStmtToken(
                                analyzer.getEnvironment(), analyzer.getContext(), var.getTokens().subList(0, k)
                        );

                        valueExpr = new ExprStmtToken(
                                analyzer.getEnvironment(),
                                analyzer.getContext(),
                                var.getTokens().subList(k + 1, var.getTokens().size())
                        );

                        if (valueExpr.getSingle() instanceof ArrayExprToken || valueExpr.getSingle() instanceof ListExprToken) {
                            if ((arraySyntax && valueExpr.getSingle() instanceof ListExprToken) || (!arraySyntax && valueExpr.getSingle() instanceof ArrayExprToken)) {
                                analyzer.getEnvironment().error(
                                        next.toTraceInfo(analyzer.getContext()),
                                        ErrorType.E_ERROR,
                                        Messages.ERR_CANNOT_MIX_ARRAY_AND_LIST
                                );
                            }

                            int doneIndex = iterator.nextIndex() - 1;

                            int prevCount = doneIndex - currIndex;

                            for (int m = 0; m < prevCount; m++) {
                                iterator.previous();
                            }

                            do {
                                current = iterator.next();
                                if (current instanceof KeyValueExprToken) {
                                    current = iterator.next();
                                    break;
                                }
                            } while (iterator.hasNext());

                            List indexes_ = new ArrayList();
                            if (indexes != null) {
                                indexes_.addAll(indexes);
                            }

                            indexes_.add(keyExpr);


                            ListExprToken listExprToken = processList(
                                    current, iterator, indexes_, null, -1
                            );

                            result.addList(listExprToken);

                            if (nextTokenAndPrev(iterator) instanceof CommaToken) {
                                iterator.next();
                            }
                        }

                        break;
                    }

                    k++;
                }

                Token single = valueExpr.getLast();

                if (single instanceof ArrayExprToken || single instanceof ListExprToken) {
                    i++;
                    continue;
                }

                if (!(single instanceof VariableExprToken
                        || single instanceof ArrayGetExprToken
                        || single instanceof DynamicAccessExprToken
                        || single instanceof ArrayPushExprToken
                        || (single instanceof StaticAccessExprToken && ((StaticAccessExprToken) single).isGetStaticField()))){
                    unexpectedToken(single);
                }

                if (single instanceof ArrayGetExprToken){
                    single = new ArrayGetRefExprToken((ArrayGetExprToken)single);
                    valueExpr.getTokens().set(var.getTokens().size() - 1, single);
                    valueExpr.updateAsmExpr(analyzer.getEnvironment(), analyzer.getContext());
                }

                if (keyExpr == null) {
                    if (withKeys) {
                        analyzer.getEnvironment().error(
                                next.toTraceInfo(analyzer.getContext()),
                                ErrorType.E_ERROR,
                                Messages.ERR_CANNOT_MIX_KEYED_AND_UNKEYED_ARRAY_ENTRIES
                        );
                    }

                    result.addVariable(valueExpr, i, indexes);
                } else {
                    if (!withKeys && i > 0) {
                        analyzer.getEnvironment().error(
                                next.toTraceInfo(analyzer.getContext()),
                                ErrorType.E_ERROR,
                                Messages.ERR_CANNOT_MIX_KEYED_AND_UNKEYED_ARRAY_ENTRIES
                        );
                    }

                    withKeys = true;
                    result.addVariable(valueExpr, keyExpr, indexes);
                }

                cont:
                i++;
            }
        }

        if (braceOpened != -1){
            next = nextToken(iterator);
            if (!(next instanceof AssignExprToken)) {
                iterator.previous();
                return result;
                //unexpectedToken(next, "=");
            }

            ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                    nextToken(iterator), iterator, BraceExprToken.Kind.ANY
            );
            result.setValue(value);
        }

        return result;
    }

    protected DieExprToken processDie(Token current, Token next, ListIterator<Token> iterator){
        DieExprToken die = (DieExprToken)current;
        if (isOpenedBrace(next, SIMPLE)){
            die.setValue(
                    analyzer.generator(ExprGenerator.class).getInBraces(SIMPLE, iterator)
            );
        }

        return die;
    }

    protected EmptyExprToken processEmpty(Token current, ListIterator<Token> iterator){
        ExprStmtToken value = analyzer.generator(ExprGenerator.class).getInBraces(SIMPLE, iterator);
        if (value == null)
            unexpectedToken(iterator.previous());

        assert value != null;

        Token last = value.getLast();
        if (last instanceof DynamicAccessExprToken){
            last = new DynamicAccessEmptyExprToken((DynamicAccessExprToken)last);
            value.getTokens().set(value.getTokens().size() - 1, last);
        } else if (last instanceof VariableExprToken || last instanceof GetVarExprToken){
            // nop
        } else if (last instanceof StaticAccessExprToken && ((StaticAccessExprToken) last).isGetStaticField()){
            last = new StaticAccessIssetExprToken((StaticAccessExprToken)last);
            value.getTokens().set(value.getTokens().size() - 1, last);
        } else if (last instanceof ArrayGetExprToken){
            ArrayGetEmptyExprToken el = new ArrayGetEmptyExprToken(last.getMeta());
            el.setParameters(((ArrayGetExprToken) last).getParameters());
            value.getTokens().set(value.getTokens().size() - 1, el);
        } else {
            // is only in < php 5.5 as unexcpected.
            //unexpectedToken(last);
        }

        EmptyExprToken result = (EmptyExprToken)current;
        value.updateAsmExpr(analyzer.getEnvironment(), analyzer.getContext());
        result.setValue(value);
        return result;
    }

    protected IssetExprToken processIsset(Token previous, Token current, ListIterator<Token> iterator){
        Token next = nextTokenAndPrev(iterator);
        if (!isOpenedBrace(next, SIMPLE))
            unexpectedToken(next, "(");

        CallExprToken call = processCall(current, nextToken(iterator), iterator);

        for(ExprStmtToken param : call.getParameters()){
            List<Token> tokens = param.getTokens();
            Token last = tokens.get(tokens.size() - 1);
            Token newToken = null;

            if (last instanceof DynamicAccessExprToken){
                newToken = new DynamicAccessIssetExprToken((DynamicAccessExprToken)last);
                if (analyzer.getClazz() != null && !"__isset".equals(analyzer.getFunction().getFulledName())){
                    ((DynamicAccessIssetExprToken)newToken).setWithMagic(false);
                }
            } else if (last instanceof VariableExprToken || last instanceof GetVarExprToken){
                // nop
            } else if (last instanceof StaticAccessExprToken && ((StaticAccessExprToken) last).isGetStaticField()){
                newToken = new StaticAccessIssetExprToken((StaticAccessExprToken)last);
            } else if (last instanceof ArrayGetExprToken){
                ArrayGetIssetExprToken el = new ArrayGetIssetExprToken(last.getMeta());
                el.setParameters(((ArrayGetExprToken) last).getParameters());
                newToken = el;
            } else
                unexpectedToken(param.getSingle());

            if (newToken != null) {
                tokens.set(tokens.size() - 1, newToken);
                param.updateAsmExpr(analyzer.getEnvironment(), analyzer.getContext());
            }
        }

        IssetExprToken result = (IssetExprToken)current;
        result.setParameters(call.getParameters());
        return result;
    }

    protected UnsetExprToken processUnset(Token previous, Token current, ListIterator<Token> iterator){
        Token next = nextTokenAndPrev(iterator);
        if (!isOpenedBrace(next, SIMPLE))
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

            param.updateAsmExpr(analyzer.getEnvironment(), analyzer.getContext());
        }

        UnsetExprToken result = (UnsetExprToken)current;
        result.setParameters(call.getParameters());
        return result;
    }

    protected CallExprToken processCall(Token previous, Token current, ListIterator<Token> iterator){
        ExprStmtToken param;

        List<ExprStmtToken> parameters = new ArrayList<>();
        do {
            param = analyzer.generator(SimpleExprGenerator.class)
                    .getNextExpression(nextToken(iterator), iterator, Separator.COMMA, SIMPLE);

            if (param != null) {
                parameters.add(param);
                if (param.isSingle()){
                    if (param.getTokens().get(0) instanceof VariableExprToken) {
                        if (analyzer.getFunction() != null)
                            analyzer.getFunction().variable((VariableExprToken)param.getTokens().get(0)).setPassed(true);
                    }
                }
            }

            if (isClosedBrace(nextToken(iterator), SIMPLE)) {
                break;
            }

        } while (param != null);
        //nextToken(iterator);

        CallExprToken result = new CallExprToken(TokenMeta.of(previous, current));
        if (previous instanceof ValueExprToken) {
            result.setName(analyzer.getRealName((ValueExprToken)previous, NamespaceUseStmtToken.UseType.FUNCTION));

            if (analyzer.getFunction() != null){
                if (result.getName() instanceof NameToken) {
                    String name = ((NameToken) result.getName()).getName().toLowerCase();
                    if (result.getName() instanceof FulledNameToken) {
                        name = ((FulledNameToken) result.getName()).getLastName().getName().toLowerCase();
                    }

                    if (dynamicLocalFunctions.contains(name.toLowerCase())) {
                        analyzer.getFunction().setDynamicLocal(true);
                    }

                    if ("get_called_class".equalsIgnoreCase(name)) {
                        analyzer.getScope().setStaticExists(true);
                    }
                }
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

    protected Token processYield(Token current, Token next, ListIterator<Token> iterator, BraceExprToken.Kind closedBrace) {
        if (analyzer.getFunction() == null) {
            analyzer.getEnvironment().error(
                    current.toTraceInfo(analyzer.getContext()), Messages.ERR_YIELD_CAN_ONLY_INSIDE_FUNCTION.fetch()
            );
        }

        analyzer.getFunction().setGenerator(true);

        YieldExprToken result = (YieldExprToken) current;

        // if yield from <iterable>
        if (next instanceof FromExprToken) {
            nextToken(iterator);
            result.setDelegating(true);
        }

        if (next instanceof OperatorExprToken && ((OperatorExprToken) next).isBinary()) {
            result.setValue(null);
        } else {
            ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                    nextToken(iterator), iterator, BraceExprToken.Kind.ANY
            );
            result.setValue(value);
        }

        return result;
    }

    protected ImportExprToken processImport(Token current, Token next, ListIterator<Token> iterator,
                                              BraceExprToken.Kind closedBrace, int braceOpened){
        ImportExprToken result = (ImportExprToken)current;
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                nextToken(iterator), iterator, BraceExprToken.Kind.ANY
        );
        result.setValue(value);

        FunctionStmtToken function = analyzer.getFunction();
        if (function != null) {
            function.setDynamicLocal(true);
            function.setCallsExist(true);
            function.setThisExists(true);
        }

        return result;
    }

    protected CallExprToken processPrint(Token current, Token next, ListIterator<Token> iterator,
                                         BraceExprToken.Kind closedBrace, int braceOpened){
        CallExprToken callExprToken = new CallExprToken(current.getMeta());
        callExprToken.setName((ExprToken) current);

        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                nextToken(iterator), iterator, BraceExprToken.Kind.ANY
        );

        if (value == null)
            unexpectedToken(iterator.previous());

        callExprToken.setParameters(Arrays.asList(value));
        return callExprToken;
    }

    protected Token processStaticAccess(Token current, Token previous, ListIterator<Token> iterator) {
        Token name = previous;

        if (name != null && !isTokenClass(name, SelfExprToken.class, StaticExprToken.class, ParentExprToken.class)) {
            name = makeSensitive(previous);
        }

        if (name == null || name instanceof NameToken || name instanceof VariableExprToken
                || name instanceof SelfExprToken || name instanceof StaticExprToken
                || name instanceof ParentExprToken) {
            if (name instanceof StaticExprToken) {
                analyzer.getScope().setStaticExists(true);
            }

            StaticAccessExprToken result = (StaticAccessExprToken) current;
            ValueExprToken clazz = (ValueExprToken) name;

            if (clazz instanceof NameToken){
                clazz = analyzer.getRealName((NameToken)clazz);
            } else if (clazz instanceof SelfExprToken){
                if (analyzer.getClazz() == null) {
                    ;
                } else {
                    if (!analyzer.getClazz().isTrait()) {
                        clazz = new FulledNameToken(clazz.getMeta(), new ArrayList<Token>() {{
                            if (analyzer.getClazz().getNamespace().getName() != null)
                                addAll(analyzer.getClazz().getNamespace().getName().getNames());
                            add(analyzer.getClazz().getName());
                        }});
                    }
                }
            }

            result.setClazz(clazz);

            if (name != null) {
                nextToken(iterator);
            }

            current = nextToken(iterator);

            if (!isTokenClass(current, ClassStmtToken.class)) {
                current = makeSensitive(current);
            }

            if (isOpenedBrace(current, BLOCK)){
                ExprStmtToken expr = getToken(nextToken(iterator), iterator, false, BLOCK);
                result.setFieldExpr(expr);
                nextAndExpected(iterator, BraceExprToken.class);
            } else if (current instanceof NameToken || current instanceof VariableExprToken){
                result.setField((ValueExprToken)current);
            } else if (current instanceof DollarExprToken) {
                Token nm = nextToken(iterator);
                if (nm instanceof VariableExprToken) {
                    result.setFieldExpr(new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), nm));
                } else if (nm instanceof DollarExprToken) {
                    result.setFieldExpr(new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), processVarVar(nm, nextTokenAndPrev(iterator), iterator)));
                } else if (isOpenedBrace(nm, BLOCK)) {
                    iterator.previous();
                    result.setFieldExpr(
                            analyzer.generator(ExprGenerator.class).getInBraces(BLOCK, iterator)
                    );
                } else
                    unexpectedToken(current);
            } else if (current instanceof ClassStmtToken) { // PHP 5.5 ::class
                if (clazz instanceof ParentExprToken || clazz instanceof StaticExprToken) {
                    if (clazz instanceof StaticExprToken) {
                        analyzer.getScope().setStaticExists(true);
                    }

                    result.setField(new ClassExprToken(current.getMeta()));
                } else if (clazz instanceof NameToken) {
                    return new StringExprToken(
                            TokenMeta.of(((NameToken) clazz).getName(), clazz),
                            StringExprToken.Quote.SINGLE
                    );
                } else
                    unexpectedToken(current);
            } else
                unexpectedToken(current);

            if (name == null) {
                return new StaticAccessOperatorExprToken(result);
            }

            return result;
        } else
            unexpectedToken(name);

        return null;
    }

    protected DynamicAccessExprToken processDynamicAccess(Token current, Token next, ListIterator<Token> iterator,
            BraceExprToken.Kind closedBraceKind, int braceOpened){
        if (next != null && next.isNamedToken() && !(next instanceof NameToken))
            next = new NameToken(next.getMeta());

        DynamicAccessExprToken result = (DynamicAccessExprToken)current;
        if (next instanceof NameToken || next instanceof VariableExprToken){
            result.setField((ValueExprToken) next);
            next = iterator.next();
            if (result.getField() instanceof VariableExprToken) {
                if (isOpenedBrace(nextTokenAndPrev(iterator), ARRAY)){
                    Token arr = processArrayToken(next, nextToken(iterator), iterator);
                    result.setFieldExpr(new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), result.getField(), arr));
                    result.setField(null);
                }
            }
        } else if (isOpenedBrace(next, BLOCK)){
            ExprStmtToken name = analyzer.generator(ExprGenerator.class).getInBraces(
                    BLOCK, iterator
            );
            result.setFieldExpr(name);
        }

        if (iterator.hasNext()){
            next = iterator.next();
            if (next instanceof AssignableOperatorToken){
                DynamicAccessAssignExprToken dResult = new DynamicAccessAssignExprToken(result);
                dResult.setAssignOperator(next);

                ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class)
                        .setCanStartByReference(true)
                        .getNextExpression(nextToken(iterator), iterator, BraceExprToken.Kind.ANY);

                dResult.setValue(value);
                return dResult;
            }
            iterator.previous();
        }

        return result;
    }

    public GetVarExprToken processVarVar(Token current, Token next, ListIterator<Token> iterator){
        ExprStmtToken name = null;
        if (next instanceof VariableExprToken){ // $$var
            name = new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), next);
            nextToken(iterator);
        } else if (next instanceof DollarExprToken){ // $$$var
            current = nextToken(iterator);
            next    = nextToken(iterator);
            name    = new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), processVarVar(current, next, iterator));
            iterator.previous();
        } else if (isOpenedBrace(next, BLOCK)){ // ${var}
            name = analyzer.generator(ExprGenerator.class).getInBraces(
                    BLOCK, iterator
            );
        } else if (next == null) {
            unexpectedEnd(current);
        }

        if (name == null) {
            unexpectedToken(next);
        }

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setDynamicLocal(true);
            analyzer.getFunction().setVarsExists(true);
        }

        GetVarExprToken result = new GetVarExprToken(TokenMeta.of(current, name));
        result.setName(name);
        return result;
    }

    protected Token processValueIfElse(ValueIfElseToken current, Token next, ListIterator<Token> iterator,
                                       BraceExprToken.Kind closedBrace, int braceOpened, Separator separator){
        ExprStmtToken value = null;

        if (current instanceof ValueNullCoalesceIfElseToken) {
            value = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                    nextToken(iterator), iterator, separator, BraceExprToken.Kind.ANY
            );

            current.setAlternative(value);
        } else {
            value = analyzer.generator(SimpleExprGenerator.class).getToken(
                    nextToken(iterator), iterator, Separator.COLON, closedBrace
            );

            current.setValue(value);

            if (!((next = iterator.previous()) instanceof ColonToken)) {
                unexpectedToken(next, ":");
            }

            iterator.next();
            ExprStmtToken alternative = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                    nextToken(iterator), iterator, separator, BraceExprToken.Kind.ANY
            );

            if (alternative == null)
                unexpectedToken(iterator.next());

            current.setAlternative(alternative);
        }

        return current;
    }

    protected ExprStmtToken processNewExpr(Token next, BraceExprToken.Kind closedBrace, int braceOpened,
            ListIterator<Token> iterator, boolean first) {
        List<Token> tmp = new ArrayList<Token>();
        if (first) {
            if (next instanceof VariableExprToken) {
                analyzer.getScope().addVariable((VariableExprToken) next);
                if (analyzer.getFunction() != null) {
                    analyzer.getFunction().setVarsExists(true);
                    analyzer.getFunction().variable((VariableExprToken) next).setUsed(true);
                }
            }

            tmp.add(next);
        }

        Token previous = next;
        Token token = nextToken(iterator);

        if (isOpenedBrace(token, ARRAY) || isOpenedBrace(token, BLOCK)) {
            tmp.add(processArrayToken(previous, token, iterator));
            if (iterator.hasNext()) {
                if (nextTokenAndPrev(iterator) instanceof DynamicAccessExprToken)
                    tmp.addAll(processNewExpr(next, closedBrace, braceOpened, iterator, false).getTokens());
            }
        } else if (token instanceof DynamicAccessExprToken) {
            next = null;
            if (iterator.hasNext()) {
                next = iterator.next();
                iterator.previous();
            }

            tmp.add(processDynamicAccess(token, next, iterator, closedBrace, braceOpened));
            if (iterator.hasNext()) {
                token = nextTokenAndPrev(iterator);
                if (isOpenedBrace(token, ARRAY)
                        || isOpenedBrace(token, BLOCK)
                        || token instanceof DynamicAccessExprToken) {
                    tmp.addAll(processNewExpr(next, closedBrace, braceOpened, iterator, false).getTokens());
                }
            }
        } else
            iterator.previous();

        if (!first) {
            return new ExprStmtToken(null, null, tmp);
        }

        return new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), tmp);
    }

    protected Token processNew(Token current, BraceExprToken.Kind closedBrace, int braceOpened, ListIterator<Token> iterator){
        NewExprToken result = (NewExprToken)current;
        Token next = nextToken(iterator);

        if (!isTokenClass(next, StaticExprToken.class, ParentExprToken.class, SelfExprToken.class)) {
            next = makeSensitive(next);
        }

        if (next instanceof NameToken){
            FulledNameToken nameToken = analyzer.getRealName((NameToken)next);
            result.setName(nameToken);

            iterator.previous();
            iterator.previous();

            if (iterator.hasPrevious()) {
                Token previous = iterator.previous();

                if (previous instanceof AssignExprToken) {
                    if (iterator.hasPrevious()) {
                        previous = iterator.previous();

                        if (previous instanceof VariableExprToken) {
                            analyzer.getScope().typeInfoOf(previous).addType(nameToken.getName());
                        }

                        iterator.next();
                    }
                }

                iterator.next();
            }

            iterator.next();
            iterator.next();
        } else if (next instanceof VariableExprToken) {
            result.setName(processNewExpr(next, closedBrace, braceOpened, iterator, true));
        } else if (next instanceof StaticExprToken) {
            Scope scope = analyzer.getScope();
            scope.setStaticExists(true);
            result.setName((StaticExprToken)next);
        } else if (next instanceof SelfExprToken){
            if (analyzer.getClazz() == null) {
                result.setName((SelfExprToken) next);
            } else {
                if (analyzer.getClazz().isTrait()) {
                    result.setName((SelfExprToken) next);
                } else {
                    result.setName(new FulledNameToken(next.getMeta(), new ArrayList<Token>() {{
                        if (analyzer.getClazz().getNamespace().getName() != null)
                            addAll(analyzer.getClazz().getNamespace().getName().getNames());
                        add(analyzer.getClazz().getName());
                    }}));
                }
            }

        } else
            unexpectedToken(next);

        next = nextToken(iterator);
        if (isOpenedBrace(next, SIMPLE)){
            ExprStmtToken param;
            List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
            do {
                param = analyzer.generator(SimpleExprGenerator.class)
                        .getToken(nextToken(iterator), iterator, true, SIMPLE);

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

    protected Token processString(StringExprToken string) {
        if (string.getSegments().isEmpty()){
            if (string.getQuote() == StringExprToken.Quote.SHELL) {
                return new ShellExecExprToken(string.getMeta(), Arrays.<Token>asList(string));
            }

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
                analyzer.getScope().addVariables(syntaxAnalyzer.getScope().getVariables());

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

        if (string.getQuote() == StringExprToken.Quote.SHELL) {
            return new ShellExecExprToken(meta, tokens);
        }

        StringBuilderExprToken result = new StringBuilderExprToken(meta, tokens);
        result.setBinary(string.isBinary());
        return result;
    }

    protected Token processSimpleToken(Token current, Token previous, Token next, ListIterator<Token> iterator,
                                       BraceExprToken.Kind closedBraceKind, int braceOpened, Separator separator){
        if (current instanceof DynamicAccessExprToken){
            return processDynamicAccess(current, next, iterator, closedBraceKind, braceOpened);
        }

        if (current instanceof OperatorExprToken) {
            isRef = false;

            if (current instanceof InstanceofExprToken && next instanceof NameToken) {
                if (previous instanceof VariableExprToken) {
                    analyzer.getScope().typeInfoOf(previous).addType(analyzer.getRealName((NameToken) next, NamespaceUseStmtToken.UseType.CLASS).getName());
                }
            }
        }

        if (current instanceof NameToken && next instanceof StringExprToken) {
            // binary string
            if (((NameToken) current).getName().equalsIgnoreCase("b")) {
                ((StringExprToken) next).setBinary(true);
                iterator.next();
                return processString((StringExprToken)next);
            }
        }

        if (current instanceof YieldExprToken) {
            return processYield(current, next, iterator, closedBraceKind);
        }

        if (current instanceof ImportExprToken) {
            return processImport(current, next, iterator, closedBraceKind, braceOpened);
        }

        if (current instanceof PrintNameToken) {
            return processPrint(current, next, iterator, closedBraceKind, braceOpened);
        }

        if (current instanceof NewExprToken) {
            return processNew(current, closedBraceKind, braceOpened, iterator);
        }

        if (current instanceof DollarExprToken){
            return processVarVar(current, next, iterator);
        }

        if (current instanceof VariableExprToken){
            analyzer.getScope().addVariable((VariableExprToken) current);
            if (analyzer.getFunction() != null) {
                analyzer.getFunction().setVarsExists(true);
                analyzer.getFunction().variable((VariableExprToken)current).setUsed(true);
            }
        }

        // Если переменная меняется, значит она нестабильна и не может быть заменена на костантное значение.
        if ((current instanceof AssignOperatorExprToken || current instanceof IncExprToken || current instanceof DecExprToken)
                && previous instanceof VariableExprToken) {

            if (analyzer.getFunction() != null) {
                analyzer.getFunction().variable((VariableExprToken) previous).setUnstable(true);
            }
        }

        if (current instanceof AssignExprToken && previous instanceof VariableExprToken && next instanceof VariableExprToken) {
            ExpressionInfo info = analyzer.getScope().typeInfoOf(next);
            analyzer.getScope().typeInfoOf(previous).addTypes(info.getTypes());
        }

        if (current instanceof ValueIfElseToken){
            return processValueIfElse((ValueIfElseToken)current, next, iterator, closedBraceKind, braceOpened, separator);
        }

        // &
        if (current instanceof AmpersandRefToken){
            /*if (previous == null)
                unexpectedToken(current);*/

            isRef = true;
            if (next instanceof VariableExprToken)
                if (analyzer.getFunction() != null) {
                    analyzer.getFunction().variable((VariableExprToken)next)
                        .setReference(true)
                        .setMutable(true);
                }

            if (previous instanceof AssignExprToken || previous instanceof KeyValueExprToken
                    || (canStartByReference && previous == null)) {
                if (previous instanceof AssignExprToken)
                    ((AssignExprToken) previous).setAsReference(true);

                iterator.previous();
                Token token = iterator.previous(); // =
                if (iterator.hasPrevious()) {
                    token = iterator.previous();
                    if (token instanceof VariableExprToken && analyzer.getFunction() != null){
                        analyzer.getFunction().variable((VariableExprToken)token)
                                .setReference(true)
                                .setMutable(true);
                       // analyzer.getFunction().getUnstableLocal().add((VariableExprToken)token); TODO: check is needed?
                    }

                    iterator.next();
                }
                iterator.next();
                iterator.next();
                if (!(next instanceof ValueExprToken))
                    unexpectedToken(token);

            } else {
                return new AndExprToken(current.getMeta());
            }

            return current;
        }
        // &$var, &$obj->prop['x'], &class::$prop, &$arr['x'], &call()->x;
        if (previous instanceof AmpersandRefToken){
            if (current instanceof VariableExprToken)
                if (analyzer.getFunction() != null)
                    analyzer.getFunction().variable((VariableExprToken)current).setReference(true);
        }

        if ((current instanceof MinusExprToken || current instanceof PlusExprToken)
                && (next instanceof IntegerExprToken || next instanceof DoubleExprToken)){

            if (!(previous instanceof ValueExprToken
                    || previous instanceof ArrayGetExprToken || previous instanceof DynamicAccessExprToken
                    || isClosedBrace(previous, SIMPLE))){
                iterator.next();
                // if it minus
                if (current instanceof MinusExprToken){
                    if (next instanceof IntegerExprToken){
                        return new IntegerExprToken(TokenMeta.of(current, next));
                    } else if (next instanceof DoubleExprToken){
                        return new DoubleExprToken(TokenMeta.of(current, next));
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
            ExprStmtToken result = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                nextToken(iterator),
                iterator,
                separator,
                braceOpened > 0 ? SIMPLE : closedBraceKind
            );

            logic.setRightValue(result);
            return logic;
        }

        if (next instanceof StaticAccessExprToken) {
            return processStaticAccess(next, current, iterator);
        }

        if (current instanceof StaticAccessExprToken) {
            return processStaticAccess(current, null, iterator);
        }

        if (current instanceof StringExprToken){
            return processString((StringExprToken) current);
        }

        if (current instanceof NameToken) {
            if (previous instanceof InstanceofExprToken) {
                return analyzer.getRealName((NameToken) current, NamespaceUseStmtToken.UseType.CLASS);
            } else {
                return analyzer.getRealName((NameToken) current, NamespaceUseStmtToken.UseType.CONSTANT);
            }
        }

        if (current instanceof MacroToken) {
            return null;
        }

        if (current instanceof OperatorExprToken) {
            return null;
        }

        if (current.isNamedToken()) {
            return makeSensitive(current);
        }

        return null;
    }

    protected Token processNewArray(Token current, ListIterator<Token> iterator){
        ArrayExprToken result = new ArrayExprToken(current.getMeta());
        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();

        Token next;
        BraceExprToken.Kind braceKind;
        if (isOpenedBrace(current, ARRAY)) {
            next = current;
            braceKind = ARRAY;
            result.setShortSyntax(true);
        } else {
            next = nextToken(iterator);

            if (!isOpenedBrace(next, SIMPLE)) {
                unexpectedToken(next, "(");
            }

            braceKind = SIMPLE;
            result.setShortSyntax(false);
        }

        do {
            SimpleExprGenerator generator = analyzer.generator(SimpleExprGenerator.class);
            generator.setCanStartByReference(true);

            Token nextToken = nextToken(iterator);

            if (nextToken instanceof CommaToken && result.isShortSyntax()) {
                result.setListSyntax(true);
                parameters.add(null);
                continue;
            }

            ExprStmtToken argument = generator.getToken(nextToken, iterator, Separator.COMMA, braceKind);
            if (argument == null)
                break;

            parameters.add(argument);
        } while (true);
        nextToken(iterator); // skip )

        result.setParameters(parameters);
        return result;
    }

    protected Token processArrayToken(Token previous, Token current, ListIterator<Token> iterator){
        BraceExprToken.Kind braceKind = ARRAY;
        Separator separator = Separator.ARRAY;
        if (isOpenedBrace(current, BLOCK)){
            braceKind = BLOCK;
            separator = Separator.ARRAY_BLOCK;
        }

        VariableExprToken var = null;
        if (previous instanceof VariableExprToken) {
            if (analyzer.getFunction() != null){
                analyzer.getFunction().variable(var = (VariableExprToken)previous).setArrayAccess(true);
            }
        }

        Token next = nextToken(iterator);
        if (isClosedBrace(next, braceKind)){
            //Token tk = nextTokenAndPrev(iterator);
            //if (tk instanceof AssignableOperatorToken || isOpenedBrace(tk, BraceExprToken.Kind.ARRAY)) {
            // !!! allow [] anywhere
            return new ArrayPushExprToken(TokenMeta.of(current, next));
            /*} else
                unexpectedToken(tk);*/
        } else
            iterator.previous();

        ExprStmtToken param;
        List<ExprStmtToken> parameters = new ArrayList<>();
        boolean lastPush = false;

        do {
            Token token = nextToken(iterator);

            if (isClosedBrace(token, ARRAY)) {
                iterator.previous();

                if (iterator.hasPrevious()) {
                    iterator.previous();
                }

                lastPush = true;
                break;
            } else {
                param = analyzer.generator(SimpleExprGenerator.class).getNextExpression(token, iterator, separator, braceKind);
            }

            if (param != null) {
                parameters.add(param);

                if (iterator.hasNext()){
                    iterator.next();

                    if (iterator.hasNext()) {
                        Token tmp = nextToken(iterator);
                        if (isOpenedBrace(tmp, ARRAY)) {
                            braceKind = ARRAY;
                            separator = Separator.ARRAY;
                            continue;
                        } else if (isOpenedBrace(tmp, BLOCK)) {
                            braceKind = BLOCK;
                            separator = Separator.ARRAY_BLOCK;
                            continue;
                        }
                        iterator.previous();
                    }

                    break;
                }
            }

        } while (param != null);
        //nextToken(iterator); // skip ]

        ArrayGetExprToken result;
        result = new ArrayGetExprToken(current.getMeta());
        result.setParameters(parameters);

        if (isRef) {
            result = new ArrayGetRefExprToken(result);
            ((ArrayGetRefExprToken)result).setShortcut(true);

            if (var != null && analyzer.getFunction() != null) {
                analyzer.getFunction().variable(var).setMutable(true);
            }
        } else if (iterator.hasNext()){
            next = iterator.next();

            if (next instanceof AssignableOperatorToken || lastPush){
                result = new ArrayGetRefExprToken(result);

                if (var != null && analyzer.getFunction() != null) {
                    analyzer.getFunction().variable(var).setMutable(true);
                }
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

    public ExprStmtToken getNextExpression(Token current, ListIterator<Token> iterator,
                                           BraceExprToken.Kind closedBraceKind){
        return getNextExpression(current, iterator, Separator.COMMA_OR_SEMICOLON, closedBraceKind);
    }

    public ExprStmtToken getNextExpression(Token current, ListIterator<Token> iterator,
                                           Separator separator,
                                           BraceExprToken.Kind closedBraceKind){
        ExprStmtToken value = getToken(
                current, iterator, separator, closedBraceKind
        );
        Token tk = iterator.previous();
        if (!isBreak(tk) && (separator == null || !separator.is(tk))){
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

            if (isOpenedBrace(current, SIMPLE)){
                boolean isFunc = false;
                if (previous instanceof NameToken && previous.getMeta().getWord().equalsIgnoreCase("array")){
                    iterator.previous();
                    tokens.set(tokens.size() - 1, current = processNewArray(previous, iterator));
                } else {
                    if (previous instanceof NameToken
                            || previous instanceof VariableExprToken
                            || previous instanceof ClosureStmtToken
                            || previous instanceof ArrayGetExprToken
                            || previous instanceof CallExprToken)
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
            } else if (braceOpened > 0 && isClosedBrace(current, SIMPLE)){
                braceOpened -= 1;
                tokens.add(current);
                if (isOpenedBrace(previous, SIMPLE))
                    unexpectedToken(current);
            } else if (isOpenedBrace(current, ARRAY)
                    || isOpenedBrace(current, BLOCK)){
                if (isTokenClass(previous,
                        NameToken.class,
                        VariableExprToken.class,
                        GetVarExprToken.class,
                        CallExprToken.class,
                        ArrayGetExprToken.class,
                        DynamicAccessExprToken.class,
                        StringExprToken.class,
                        StringBuilderExprToken.class,
                        CallOperatorToken.class,
                        ArrayPushExprToken.class) ||
                        (previous instanceof StaticAccessExprToken && ((StaticAccessExprToken)previous).isGetStaticField())){
                    // array
                    current = processArrayToken(previous, current, iterator);
                    if (previous instanceof DynamicAccessExprToken && current instanceof ArrayGetRefExprToken){
                        tokens.set(tokens.size() - 1, new DynamicAccessGetRefExprToken((DynamicAccessExprToken)previous));
                    }
                    tokens.add(current);
                } else if (previous instanceof OperatorExprToken
                        || previous == null
                        || isOpenedBrace(previous, SIMPLE)
                        || isOpenedBrace(previous, BLOCK)) {
                    int currIndex = iterator.nextIndex() - 1;
                    ArrayExprToken arrToken = (ArrayExprToken) processNewArray(current, iterator);

                    boolean nextAssign = nextTokenAndPrev(iterator) instanceof AssignExprToken;

                    if (nextAssign) {
                        int doneIndex = iterator.nextIndex();
                        int prevCount = doneIndex - currIndex;

                        for (int i = 0; i < prevCount; i++) {
                            current = iterator.previous();
                        }

                        iterator.next();

                        tokens.add(processList(current, iterator, null, closedBraceKind, braceOpened));
                    } else {
                        if (arrToken.isListSyntax()) {
                            unexpectedToken(nextToken(iterator), "=");
                        }

                        tokens.add(current = arrToken);
                    }
                } else {
                    unexpectedToken(current);
                }
            } else if (braceOpened == 0 && isClosedBrace(current, ARRAY)){
                if (separator == Separator.ARRAY)
                    break;
                if (closedBraceKind == ARRAY || closedBraceKind == BraceExprToken.Kind.ANY){
                    //if (tokens.isEmpty())
                    iterator.previous();
                    break;
                }
                unexpectedToken(current);
            } else if (separator == Separator.ARRAY_BLOCK
                    && braceOpened == 0 && isClosedBrace(current, BLOCK)){
                break;
            } else if (current instanceof FunctionStmtToken){
                current = processClosure(current, next, iterator);
                tokens.add(current);
            } else if (current instanceof ListExprToken && isOpenedBrace(next, SIMPLE)){
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
                if (isOpenedBrace(previous, SIMPLE)
                        && isClosedBrace(next, SIMPLE)){
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
                    /*if (tokens.isEmpty()) see: issues/93
                        unexpectedToken(current);*/
                    break;
                }
                unexpectedToken(current);
            } else if (current instanceof SemicolonToken){ // TODO refactor!
                if (separator == Separator.SEMICOLON || separator == Separator.COMMA_OR_SEMICOLON) {
                    /*if (tokens.isEmpty()) see: issues/94
                        unexpectedToken(current);*/
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
                if (current instanceof NameToken && isOpenedBrace(previous, SIMPLE)
                        && isClosedBrace(next, SIMPLE)){
                    cast = CastExprToken.valueOf(((NameToken)current).getName(), current.getMeta());
                    if (cast != null){
                        current = cast;
                        iterator.next();
                        braceOpened--;
                        tokens.set(tokens.size() - 1, current);
                    }
                }

                if (cast == null){
                    Token token = processSimpleToken(
                            current, previous, next, iterator, closedBraceKind, braceOpened, separator
                    );
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

        ExprStmtToken exprStmtToken = new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), tokens);

        return exprStmtToken;
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
