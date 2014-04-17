package org.develnext.jphp.gendocs;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ConstStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;

import java.io.File;
import java.util.Collection;

public class ApiDocument {
    protected Collection<ClassStmtToken> classes;
    protected Collection<ConstStmtToken> constants;
    protected Collection<FunctionStmtToken> functions;

    public ApiDocument(SyntaxAnalyzer analyzer) {
        classes = analyzer.getClasses();
        constants = analyzer.getConstants();
        functions = analyzer.getFunctions();
    }

    public void generate(File targetDirectory) {
        for(ClassStmtToken cls : classes) {
            System.out.println(cls.getFulledName());
            if (cls.getDocComment() != null) {
                DocAnnotations annotations = new DocAnnotations(cls.getDocComment().getComment());
                System.out.println(annotations.getDescription());
                System.out.println();
            }
        }
    }
}
