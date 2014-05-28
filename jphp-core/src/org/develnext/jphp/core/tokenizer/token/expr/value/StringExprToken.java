package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.util.ArrayList;
import java.util.List;

public class StringExprToken extends ValueExprToken {

    public enum Quote { SINGLE, DOUBLE, SHELL, DOC;

        public boolean isMagic() {
            return this == DOUBLE || this == SHELL;
        }
    }

    public static class Segment {
        public final int from;
        public final int to;
        public final boolean isVariable;

        public Segment(int from, int to, boolean isVariable) {
            this.from = from;
            this.to = to;
            this.isVariable = isVariable;
        }
    }

    protected final Quote quote;
    private String value;

    protected List<Segment> segments = new ArrayList<Segment>();
    protected ExprStmtToken expression;

    protected boolean binary;

    public StringExprToken(TokenMeta meta, Quote quote) {
        super(meta, TokenType.T_CONSTANT_ENCAPSED_STRING);
        this.quote = quote;
        this.value = meta.getWord();
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public Quote getQuote() {
        return quote;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object toNumeric(){
        int len = value.length();
        boolean real = false;
        int i = 0;
        for(; i < len; i++){
            char ch = value.charAt(i);
            if (!('9' >= ch && ch >= '0')){
                if (ch == '.'){
                    if (real)
                        break;
                    real = true;
                    continue;
                }
                if (i == 0)
                    return 0;
                else
                    break;
            }
        }
        if (real) {
            if (len == i)
                return Double.parseDouble(value);
            else
                return Double.parseDouble(value.substring(0, i));
        } else {
            if (len == i)
                return Long.parseLong(value);
            else
                return Long.parseLong(value.substring(0, i));
        }
    }

    public ExprStmtToken getExpression() {
        return expression;
    }

    public void setExpression(ExprStmtToken expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }
}
