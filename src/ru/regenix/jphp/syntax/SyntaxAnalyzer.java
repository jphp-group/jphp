package ru.regenix.jphp.syntax;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.syntax.generators.*;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.CommentToken;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.*;

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

    private Map<String, ClassStmtToken> classes;
    private Map<String, FunctionStmtToken> functions;
    private Map<String, ConstStmtToken> constants;

    public SyntaxAnalyzer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;

        classes = new LinkedHashMap<String, ClassStmtToken>();
        functions = new LinkedHashMap<String, FunctionStmtToken>();
        constants = new LinkedHashMap<String, ConstStmtToken>();

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
        generators.add(new BodyGenerator(this));

        generators.add(new ExprGenerator(this));
        for (Generator generator : generators)
            map.put(generator.getClass(), generator);

        addLocalScope();
        process();
    }

    public void registerClass(ClassStmtToken clazz){
        classes.put(clazz.getFulledName().toLowerCase(), clazz);
    }

    public void registerFunction(FunctionStmtToken function){
        functions.put(function.getFulledName(), function);
    }

    public void registerConstant(ConstStmtToken constant){
        constants.put(constant.getFulledName(), constant);
    }

    public Collection<ClassStmtToken> getClasses() {
        return classes.values();
    }

    public Collection<FunctionStmtToken> getFunctions() {
        return functions.values();
    }

    public Collection<ConstStmtToken> getConstants(){
        return constants.values();
    }

    public ClassStmtToken findClass(String name){
        return classes.get(name.toLowerCase());
    }

    public FunctionStmtToken findFunction(String name){
        return functions.get(name.toLowerCase());
    }

    public ConstStmtToken findConstant(String name){
        return constants.get(name);
    }

    protected void process(){
        tokenizer.reset();
        tree.clear();

        Token token;
        while ((token = tokenizer.nextToken()) != null){
            if (token instanceof CommentToken){
                if (((CommentToken) token).getKind() != CommentToken.Kind.DOCTYPE)
                    continue;
            }
            tokens.add(token);
        }

        ListIterator<Token> iterator = tokens.listIterator();
        tree = process(iterator);
    }

    protected void registerToken(Token token){
        if (token instanceof ClassStmtToken)
            registerClass((ClassStmtToken)token);
        else if (token instanceof FunctionStmtToken)
            registerFunction((FunctionStmtToken)token);
        else if (token instanceof ConstStmtToken)
            registerConstant((ConstStmtToken)token);
    }

    public List<Token> process(ListIterator<Token> iterator){
        List<Token> result = new ArrayList<Token>();
        while (iterator.hasNext()){
            Token gen = processNext(iterator);
            if (gen instanceof NamespaceStmtToken){
                List<Token> tree = ((NamespaceStmtToken) gen).getTree();
                ((NamespaceStmtToken) gen).setTree(null);

                result.add(gen);
                registerToken(gen);

                result.addAll(tree);
                for(Token el : tree){
                    registerToken(el);
                }
            } else {
                result.add(gen);
                registerToken(gen);
            }
        }
        return result;
    }

    public Token processNext(ListIterator<Token> iterator){
        Token current = iterator.next();
        Token gen = generateToken(current, iterator);
        return (gen == null ? current : gen);
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

    public Context getContext(){
        return tokenizer.getContext();
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

    public ValueExprToken getRealName(ValueExprToken value){
        if (value == null)
            return null;

        if (value instanceof NameToken)
            return getRealName((NameToken)value);
        else
            return value;
    }

    public FulledNameToken getRealName(NameToken what) {
        if (what instanceof FulledNameToken && ((FulledNameToken) what).isAbsolutely())
            return (FulledNameToken) what;

        String name = what.getName();

        // check name in uses
        for(NamespaceUseStmtToken use : namespace.getUses()){
            if (use.getAs() == null){
                if (name.equalsIgnoreCase(use.getName().getLastName().getName()))
                    return use.getName();
            } else {
                if (name.equalsIgnoreCase(use.getAs().getName())){
                    return use.getName();
                }
            }
        }

        List<NameToken> names = namespace.getName() == null
                ? new ArrayList<NameToken>()
                : new ArrayList<NameToken>(namespace.getName().getNames());

        if (what instanceof FulledNameToken)
            names.addAll(((FulledNameToken) what).getNames());
        else
            names.add(what);

        return new FulledNameToken(what.getMeta(), names);
    }
}
