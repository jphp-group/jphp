package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

import java.util.ArrayList;
import java.util.List;

public class ListExprToken extends ValueExprToken implements CallableExprToken {
    protected final List<Variable> variables;
    protected ExprStmtToken value;

    public ListExprToken(TokenMeta meta) {
        super(meta, TokenType.T_LIST);
        variables = new ArrayList<Variable>();
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public Variable addVariable(VariableExprToken v, int index, List<Integer> indexes){
        Variable var = new Variable(v, index, indexes);
        variables.add(var);
        return var;
    }

    public void addList(ListExprToken list){
        for(Variable v : list.variables)
            variables.add(v);
    }

    public class Variable {
        public final VariableExprToken var;
        public final String name;
        public final int index;
        public final List<Integer> indexes;

        public Variable(VariableExprToken var, int index, List<Integer> indexes) {
            this.var = var;
            this.name = var.getName();
            this.index = index;
            this.indexes = indexes;
        }
    }
}
