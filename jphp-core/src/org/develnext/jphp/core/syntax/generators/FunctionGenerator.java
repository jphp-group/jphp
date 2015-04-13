package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.tokenizer.token.expr.operator.ArgumentUnpackExprToken;
import php.runtime.common.HintType;
import php.runtime.common.LangMode;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.syntax.generators.manually.BodyGenerator;
import org.develnext.jphp.core.syntax.generators.manually.SimpleExprGenerator;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.CommaToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.AmpersandRefToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.AssignExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.*;

import java.util.*;

public class FunctionGenerator extends Generator<FunctionStmtToken> {

    public FunctionGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected final static Set<String> scalarTypeHints = new HashSet<String>(){{
        add("array");
        add("callable");
    }};

    protected final static Set<String> jphp_scalarTypeHints = new HashSet<String>(){{
        add("scalar");
        add("number");
        add("string");
        add("int");
        add("integer");
        add("double");
        add("float");
        add("bool");
        add("boolean");
    }};

    @SuppressWarnings("unchecked")
    protected ArgumentStmtToken processArgument(ListIterator<Token> iterator){
        boolean isReference = false;
        boolean isVariadic  = false;

        VariableExprToken variable = null;
        ExprStmtToken value = null;

        Token next = nextToken(iterator);
        if (next instanceof CommaToken || isClosedBrace(next, BraceExprToken.Kind.SIMPLE))
            return null;

        NameToken hintTypeClass = null;
        HintType hintType = null;

        if (next instanceof NameToken){
            String word = ((NameToken) next).getName().toLowerCase();
            if (scalarTypeHints.contains(word)
                    || (analyzer.getLangMode() == LangMode.JPHP && jphp_scalarTypeHints.contains(word)))
                hintType = HintType.of(word);
            else {
                hintType = analyzer.getLangMode() == LangMode.PHP && jphp_scalarTypeHints.contains(word)
                        ? null : HintType.of(word);
                if (hintType == null)
                    hintTypeClass = analyzer.getRealName((NameToken)next);
            }

            next = nextToken(iterator);
        }

        if (next instanceof AmpersandRefToken){
            isReference = true;
            next = nextToken(iterator);
        }

        if (next instanceof ArgumentUnpackExprToken) {
            isVariadic = true;
            next = nextToken(iterator);
        }

        if (next instanceof VariableExprToken){
            variable = (VariableExprToken)next;
        } else
            unexpectedToken(next);

        next = nextToken(iterator);
        if (next instanceof AssignExprToken) {
            if (isVariadic) {
                unexpectedToken(next);
            }

            value = analyzer.generator(SimpleExprGenerator.class).getToken(
                    nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE
            );
        } else {
            if (next instanceof CommaToken || isClosedBrace(next, BraceExprToken.Kind.SIMPLE)){
                if (next instanceof BraceExprToken) {
                    iterator.previous();
                } else {
                    if (isVariadic) {
                        unexpectedToken(next);
                    }
                }
            } else
                unexpectedToken(next);
        }

        ArgumentStmtToken argument = new ArgumentStmtToken(variable.getMeta());
        argument.setName(variable);
        argument.setHintType(hintType);
        argument.setHintTypeClass(hintTypeClass);
        argument.setReference(isReference);
        argument.setVariadic(isVariadic);
        argument.setValue(value);

        if (argument.isReference() && argument.getValue() != null)
            analyzer.getFunction().variable(argument.getName()).setUsed(true);

        return argument;
    }

    protected void processArguments(FunctionStmtToken result, ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);
        List<ArgumentStmtToken> arguments = new ArrayList<ArgumentStmtToken>();
        while (iterator.hasNext()){
            ArgumentStmtToken argument = processArgument(iterator);
            if (argument == null)
                break;

            arguments.add(argument);
        }



        result.setArguments(arguments);
    }

    protected void processUses(FunctionStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (next instanceof NamespaceUseStmtToken){
            next = nextToken(iterator);
            if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
                unexpectedToken(next, "(");

            List<ArgumentStmtToken> arguments = new ArrayList<ArgumentStmtToken>();
            while (iterator.hasNext()) {
                ArgumentStmtToken argument = processArgument(iterator);
                if (argument == null)
                    break;

                if (argument.getValue() != null)
                    unexpectedToken(argument.getValue().getSingle());
                arguments.add(argument);

                FunctionStmtToken parent = analyzer.getFunction(true);
                if (parent != null) {
                    parent.variable(argument.getName()).setUsed(true);

                    if (argument.isReference()){
                        parent.variable(argument.getName())
                                .setPassed(true)
                                .setUnstable(true);
                    }

                    parent = analyzer.peekClosure();
                    if (parent != null){
                        parent.variable(argument.getName()).setUnstable(true);
                    }
                }
            }

            result.setUses(arguments);
        } else {
            result.setUses(new ArrayList<ArgumentStmtToken>());
            iterator.previous();
        }
    }

    protected void processBody(FunctionStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){
            BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(next, iterator);
            result.setBody(body);
        } else if (next instanceof SemicolonToken) {
            result.setInterfacable(true);
            result.setBody(null);
        } else
            unexpectedToken(next);
    }

    public FunctionStmtToken getToken(Token current, ListIterator<Token> iterator, boolean closureAllowed) {
        if (current instanceof FunctionStmtToken){
            FunctionStmtToken result = (FunctionStmtToken)current;
            result.setStatic(analyzer.getFunction() == null);

            Token next = nextToken(iterator);
            if (next instanceof AmpersandRefToken){
                result.setReturnReference(true);
                next = nextToken(iterator);
            }

            if (next instanceof NameToken){
                /*if (analyzer.getFunction() != null)
                    unexpectedToken(current);*/

                analyzer.addScope(true);
                FunctionStmtToken oldFunction = analyzer.getFunction();
                analyzer.setFunction(result);

                BraceExprToken brace = nextAndExpected(iterator, BraceExprToken.class);
                if (!brace.isSimpleOpened())
                    unexpectedToken(brace, "(");

                result.setNamespace(analyzer.getNamespace());
                result.setName((NameToken)next);
                processArguments(result, iterator);
                processBody(result, iterator);

                result.setLabels(analyzer.getScope().getLabels());
                result.setLocal(analyzer.removeScope().getVariables());

                analyzer.setFunction(oldFunction);
                return result;
            } else if (next instanceof BraceExprToken){
                // xClosure
                if (((BraceExprToken) next).isSimpleOpened()){
                    if (closureAllowed){
                        analyzer.pushClosure(result);

                        analyzer.addScope(true);
                        processArguments(result, iterator);
                        processUses(result, iterator);
                        processBody(result, iterator);
                        //boolean thisExists = result.isThisExists();
                        result.setLabels(analyzer.getScope().getLabels());
                        result.setLocal(analyzer.removeScope().getVariables());
                        //result.setThisExists(thisExists);

                        analyzer.popClosure();

                        FunctionStmtToken prevClosure = analyzer.peekClosure();
                        if (prevClosure != null){
                            if (result.isThisExists()) {
                                analyzer.getScope().addVariable(FunctionStmtToken.thisVariable);
                                //prevClosure.variable(FunctionStmtToken.thisVariable).setUsed(true);
                                //prevClosure.setThisExists(true);
                            }
                        }

                        List<VariableExprToken> uses = new ArrayList<VariableExprToken>();
                        for(ArgumentStmtToken argument : result.getUses()){
                            if (argument.isReference()){
                                if (analyzer.getFunction() != null){
                                    analyzer.getFunction().variable(argument.getName()).setReference(true);
                                }
                            }
                            uses.add(argument.getName());
                        }

                        analyzer.getScope().addVariables( uses );
                        return result;
                    }

                    iterator.previous();
                    return null;
                }
            }

            unexpectedToken(next);
        }

        return null;
    }

    @Override
    public FunctionStmtToken getToken(Token current, ListIterator<Token> iterator) {
        return getToken(current, iterator, false);
    }
}
