package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.common.Separator;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.syntax.generators.manually.BodyGenerator;
import org.develnext.jphp.core.syntax.generators.manually.SimpleExprGenerator;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.AmpersandRefToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.KeyValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import php.runtime.env.CompileScope;

import java.util.*;

public class LambdaGenerator extends Generator<LambdaStmtToken> {
    public LambdaGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    private void processBody(FunctionStmtToken func, ListIterator<Token> iterator, Separator separator, BraceExprToken.Kind braceKind) {
        Token token = nextToken(iterator);

        if (isOpenedBrace(token, BraceExprToken.Kind.BLOCK)) {
            prevToken(iterator);
            analyzer.generator(FunctionGenerator.class).processBody(func, iterator);
        } else {
            ExprStmtToken expression = analyzer.generator(SimpleExprGenerator.class).getNextExpression(token, iterator, separator, braceKind);
            ReturnStmtToken returnStmtToken = new ReturnStmtToken(expression.getMeta());
            returnStmtToken.setValue(expression);

            BodyStmtToken body = new BodyStmtToken(returnStmtToken.getMeta());
            body.setInstructions(Arrays.asList(new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), returnStmtToken)));
            func.setBody(body);

            token = prevToken(iterator);

            if (!isBreak(token)) {
                nextToken(iterator);
            }
        }
    }

    private void processArguments(FunctionStmtToken func, ListIterator<Token> iterator) {
        Token token = nextToken(iterator);

        if (isOpenedBrace(token, BraceExprToken.Kind.SIMPLE)) {
            FunctionGenerator funcGenerator = analyzer.generator(FunctionGenerator.class);
            funcGenerator.processArguments(func, iterator, true);
        } else {
            prevToken(iterator);
            func.setArguments(new ArrayList<>());
        }
    }

    @Override
    public LambdaStmtToken getToken(Token current, ListIterator<Token> iterator) {
        return getToken(current, iterator, null, null);
    }

    public LambdaStmtToken getToken(Token current, ListIterator<Token> iterator, Separator separator, BraceExprToken.Kind braceKind) {
        if (current instanceof LambdaStmtToken) {
            LambdaStmtToken lambda = (LambdaStmtToken) current;

            iterator.previous();

            FunctionStmtToken func = new FunctionStmtToken(current.getMeta());
            func.setUses(new ArrayList<>());
            func.setAutoUses(true);
            analyzer.pushClosure(func);
            analyzer.addScope(true);

            nextToken(iterator);

            Token tk = nextToken(iterator);
            if (tk instanceof AmpersandRefToken) {
                func.setReturnReference(true);
            } else {
                prevToken(iterator);
            }

            processArguments(func, iterator);
            nextAndExpected(iterator, KeyValueExprToken.class);
            processBody(func, iterator, separator, braceKind);

            func.setTypeInfo(analyzer.getScope().getTypeInfo());
            func.setLabels(analyzer.getScope().getLabels());
            func.setStaticExists(analyzer.getScope().isStaticExists());
            func.setLocal(analyzer.removeScope().getVariables());

            analyzer.popClosure();

            FunctionStmtToken prevClosure = analyzer.peekClosure();
            if (prevClosure != null){
                if (func.isThisExists()) {
                    analyzer.getScope().addVariable(FunctionStmtToken.thisVariable);
                }
            }

            Set<VariableExprToken> variables = analyzer.getScope().getVariables();
            if (analyzer.getFunction() != null) {
                for (ArgumentStmtToken token : analyzer.getFunction().getArguments()) {
                    variables.add(token.getName());
                }
            }

            FunctionStmtToken peekClosure = analyzer.peekClosure();
            if (peekClosure != null && peekClosure.getUses() != null) {
                for (ArgumentStmtToken token : peekClosure.getUses()) {
                    variables.add(token.getName());
                }
            }

            Set<VariableExprToken> local = func.getLocal();
            Set<VariableExprToken> arguments = new HashSet<>();
            List<ArgumentStmtToken> uses = func.getUses();

            for (ArgumentStmtToken argument : func.getArguments()) {
                arguments.add(argument.getName());
            }

            for (VariableExprToken l : local) {
                if (!arguments.contains(l) && !l.equals(FunctionStmtToken.thisVariable)) {
                    CompileScope compileScope = this.analyzer.getEnvironment().getScope();

                    if (compileScope.superGlobals.contains(l.getName())) {
                        continue;
                    }

                    ArgumentStmtToken use = new ArgumentStmtToken(l.getMeta());
                    use.setName(l);
                    use.setReference(false);

                    if (analyzer.getFunction() != null) {
                        analyzer.getFunction().variable(l).setUsed(true);
                    }

                    uses.add(use);

                    if (peekClosure != null && peekClosure.isAutoUses() && peekClosure.getUses() != null) {
                        peekClosure.getUses().add(use);
                    }
                }
            }

            func.setUses(uses);

            Token previous = iterator.previous();
            func.getMeta().setEndLine(previous.getMeta().getStartLine());
            func.getMeta().setEndPosition(previous.getMeta().getStartPosition());
            iterator.next();

            lambda.setFunction(func);
            lambda.setParentFunction(peekClosure);

            return lambda;
        }

        return null;
    }
}
