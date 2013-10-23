package ru.regenix.jphp.compiler;

import ru.regenix.jphp.syntax.SyntaxAnalyzer;

abstract public class AbstractCompiler {

    private final SyntaxAnalyzer analyzer;

    public AbstractCompiler(SyntaxAnalyzer analyzer){
        this.analyzer = analyzer;
    }

    public SyntaxAnalyzer getAnalyzer() {
        return analyzer;
    }
}
