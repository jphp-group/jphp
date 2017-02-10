package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BackslashExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.CommaToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import php.runtime.Information;
import php.runtime.common.LangMode;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.Package;
import php.runtime.env.PackageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import static org.develnext.jphp.core.tokenizer.token.stmt.NamespaceUseStmtToken.UseType.CLASS;
import static org.develnext.jphp.core.tokenizer.token.stmt.NamespaceUseStmtToken.UseType.CONSTANT;
import static org.develnext.jphp.core.tokenizer.token.stmt.NamespaceUseStmtToken.UseType.FUNCTION;

public class UseGenerator extends Generator<NamespaceUseStmtToken> {

    public UseGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    public void parseBody(NamespaceUseStmtToken use, Token current, ListIterator<Token> iterator, FulledNameToken prefix,
                          NamespaceUseStmtToken.UseType prefixUseType) {
        boolean first = true;
        NamespaceUseStmtToken.UseType useType = prefixUseType;


        Environment environment = this.analyzer.getEnvironment();

        PackageManager packageManager = null;
        if (environment != null) {
            packageManager = environment.getPackageManager();
        }

        do {
            Token next = nextToken(iterator);

            if (next instanceof FunctionStmtToken) {
                if ((!first && prefix == null) || (prefixUseType != CLASS)) {
                    unexpectedToken(next);
                }

                useType = FUNCTION;
                next = nextToken(iterator);
            } else if (next instanceof ConstStmtToken) {
                if ((!first && prefix == null) || (prefixUseType != CLASS)) {
                    unexpectedToken(next);
                }

                useType = CONSTANT;
                next = nextToken(iterator);
            }

            use.setUseType(useType);

            if (prefix != null && next instanceof FulledNameToken && next.getMeta().getWord().startsWith("\\")) {
                unexpectedToken(new BackslashExprToken(TokenMeta.of("\\", next)), "identifier or function or const", false);
            }

            FulledNameToken name = analyzer.generator(NameGenerator.class).getToken(
                    next, iterator
            );

            if (name == null) {
                unexpectedToken(iterator.previous());
                return;
            }

            if (prefix == null) {
                use.setName(name);
            } else {
                ArrayList<NameToken> nameTokens = new ArrayList<>(prefix.getNames());
                nameTokens.addAll(name.getNames());

                use.setName(new FulledNameToken(name.getMeta(), nameTokens));
            }

            Token token = nextToken(iterator);

            if (token instanceof AsStmtToken){
                token = nextToken(iterator);
                if (token instanceof NameToken){
                    use.setAs((NameToken)token);
                    token = nextToken(iterator);
                } else
                    unexpectedToken(token);
            } else if (isOpenedBrace(token, BraceExprToken.Kind.BLOCK)) {
                if (prefix == null) {
                    parseBody(use, current, iterator, name, useType);
                    return;
                }
            } else if (token instanceof BackslashExprToken) {
                next = nextToken(iterator);

                if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK) && prefix == null) {
                    parseBody(use, current, iterator, name, useType);
                    return;
                }
            }

            NamespaceStmtToken namespace = analyzer.getNamespace();

            if (analyzer.getEnvironment() != null && analyzer.getEnvironment().scope.getLangMode() == LangMode.MODERN) {
                if (packageManager != null && use.isPackageImport()) {
                    Package aPackage = packageManager.tryFind(use.getName().toName(), use.toTraceInfo(analyzer.getContext()));

                    if (aPackage != null) {
                        for (String cls : aPackage.getClasses()) {
                            FulledNameToken nameToken = FulledNameToken.valueOf(StringUtils.split(cls, Information.NAMESPACE_SEP_CHAR));

                            NamespaceUseStmtToken useStmtToken = new NamespaceUseStmtToken(TokenMeta.of(cls, use));
                            useStmtToken.setName(nameToken);
                            useStmtToken.setUseType(NamespaceUseStmtToken.UseType.CLASS);

                            namespace.getUses().add(useStmtToken);
                        }

                        for (String s : aPackage.getFunctions()) {
                            FulledNameToken nameToken = FulledNameToken.valueOf(StringUtils.split(s, Information.NAMESPACE_SEP_CHAR));

                            NamespaceUseStmtToken useStmtToken = new NamespaceUseStmtToken(TokenMeta.of(s, use));
                            useStmtToken.setName(nameToken);
                            useStmtToken.setUseType(NamespaceUseStmtToken.UseType.FUNCTION);

                            namespace.getUses().add(useStmtToken);
                        }

                        for (String s : aPackage.getConstants()) {
                            FulledNameToken nameToken = FulledNameToken.valueOf(StringUtils.split(s, Information.NAMESPACE_SEP_CHAR));

                            NamespaceUseStmtToken useStmtToken = new NamespaceUseStmtToken(TokenMeta.of(s, use));
                            useStmtToken.setName(nameToken);
                            useStmtToken.setUseType(NamespaceUseStmtToken.UseType.CONSTANT);

                            namespace.getUses().add(useStmtToken);
                        }
                    } else {
                        namespace.getUses().add(use);
                    }
                } else {
                    namespace.getUses().add(use);
                }
            } else {
                namespace.getUses().add(use);
            }

            if (token instanceof CommaToken){
                use = new NamespaceUseStmtToken(current.getMeta());
            } else if (!(token instanceof SemicolonToken)){
                if (prefix != null && isClosedBrace(token, BraceExprToken.Kind.BLOCK)) {
                    nextAndExpected(iterator, SemicolonToken.class);
                    break;
                }

                unexpectedToken(token);
            } else
                break;

            first = false;

        } while (true);
    }

    @Override
    public NamespaceUseStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof NamespaceUseStmtToken){
            if (analyzer.getClazz() != null)
                unexpectedToken(current);

            NamespaceUseStmtToken use = (NamespaceUseStmtToken) current;
            parseBody(use, current, iterator, null, CLASS);

            return use;
        }

        return null;
    }
}
