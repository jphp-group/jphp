package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.ArrayGetExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.ArrayPushExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DynamicAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

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

    public Variable addVariable(ExprStmtToken v, ExprStmtToken index, List<Integer> indexes){
        Variable var = new Variable(v, index, indexes);
        variables.add(var);
        return var;
    }

    public Variable addVariable(ExprStmtToken v, int index, List<Integer> indexes){
        Variable var = new Variable(v, index, indexes);
        variables.add(var);
        return var;
    }

    public void addList(ListExprToken list){
        variables.addAll(list.variables);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }

    public class Variable {
        public final ExprStmtToken var;
        //public final String name;
        public final int index;
        public final ExprStmtToken exprIndex;
        public final List indexes;

        public Variable(ExprStmtToken var, int index, List indexes) {
            this.var = var;
            this.index = index;
            this.exprIndex = null;
            this.indexes = indexes;
        }

        public Variable(ExprStmtToken var, ExprStmtToken index, List indexes) {
            this.var = var;
            this.index = -1;
            this.exprIndex = index;
            this.indexes = indexes;
        }

        public boolean isArrayPush() {
            return var.getLast() instanceof ArrayPushExprToken;
        }

        public boolean isVariable(){
            return var.isSingle() && var.getSingle() instanceof VariableExprToken;
        }

        public boolean isArray(){
            return var.getLast() instanceof ArrayGetExprToken;
        }

        public boolean isDynamicProperty(){
            return var.getLast() instanceof DynamicAccessExprToken;
        }

        public boolean isStaticProperty(){
            return var.getLast() instanceof StaticAccessExprToken;
        }

        public String getVariableName(){
            return isVariable() ? ((VariableExprToken)var.getSingle()).getName() : null;
        }

        public boolean isWithKey() {
            return exprIndex != null;
        }
    }
}
