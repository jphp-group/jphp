package ru.regenix.jphp.syntax;

import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.FunctionStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.NamespaceStmtToken;
import ru.regenix.jphp.syntax.generators.*;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.syntax.generators.manually.ConstExprGenerator;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;

import java.io.File;
import java.util.*;

public class SyntaxAnalyzer {
    private Tokenizer tokenizer;
    private List<Token> tokens;
    private List<Token> tree;

    private List<Generator> generators;
    private final Map<Class<? extends Generator>, Generator> map = new HashMap<Class<? extends Generator>, Generator>();

    private NamespaceStmtToken namespace = NamespaceStmtToken.getDefault();
    private ClassStmtToken clazz;
    private FunctionStmtToken function;

    private Stack<Set<VariableExprToken>> localStack;

    public SyntaxAnalyzer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;

        tokens = new LinkedList<Token>();
        tree = new ArrayList<Token>();
        localStack = new Stack<Set<VariableExprToken>>();
        generators = new ArrayList<Generator>(50);

        generators.add(new NamespaceGenerator(this));
        generators.add(new UseGenerator(this));
        generators.add(new ClassGenerator(this));
        generators.add(new ConstGenerator(this));
        generators.add(new FunctionGenerator(this));
        generators.add(new NameGenerator(this));

        // non-automatic
        generators.add(new SimpleExprGenerator(this));
        generators.add(new ConstExprGenerator(this));
        generators.add(new BodyGenerator(this));

        generators.add(new ExprGenerator(this));
        for (Generator generator : generators)
            map.put(generator.getClass(), generator);

        addLocalScope();
        process();
        removeLocalScope();
    }

    protected void process(){
        tokenizer.reset();
        tree.clear();

        Token token;
        while ((token = tokenizer.nextToken()) != null){
            tokens.add(token);
        }

        ListIterator<Token> iterator = tokens.listIterator();
        while (iterator.hasNext()){
            Token current = iterator.next();
            Token gen = generateToken(current, iterator);
            tree.add(gen == null ? current : gen);
        }
    }

    public Set<VariableExprToken> addLocalScope(){
        Set<VariableExprToken> local = new HashSet<VariableExprToken>();
        localStack.push(local);
        return local;
    }

    public Set<VariableExprToken> removeLocalScope(){
        return localStack.pop();
    }

    public Set<VariableExprToken> getLocalScope(){
        return localStack.peek();
    }

    @SuppressWarnings("unchecked")
    public <T extends Generator> T generator(Class<T> clazz){
        Generator generator = map.get(clazz);
        if (generator == null)
            throw new AssertionError("Generator '"+clazz.getName()+"' not found");

        return (T) generator;
    }

    public Token generateToken(Token current, ListIterator<Token> iterator){
        Token gen = null;

        for(Generator generator : generators){
            if (!generator.isAutomatic())
                continue;

            gen = generator.getToken(current, iterator);
            if (gen != null)
                break;
        }

        return gen;
    }

    public List<Token> getTree(){
        return tree;
    }

    public File getFile(){
        return tokenizer.getFile();
    }

    public NamespaceStmtToken getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceStmtToken namespace) {
        this.namespace = namespace;
    }

    public ClassStmtToken getClazz() {
        return clazz;
    }

    public void setClazz(ClassStmtToken clazz) {
        this.clazz = clazz;
    }

    public FunctionStmtToken getFunction() {
        return function;
    }

    public void setFunction(FunctionStmtToken function) {
        this.function = function;
    }
}
