package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FulledNameToken extends NameToken {
    private List<NameToken> names;

    public FulledNameToken(TokenMeta meta) {
        super(meta);
    }

    public FulledNameToken(TokenMeta meta, List<? extends Token> names) {
        super(meta);
        this.names = new ArrayList<NameToken>();
        for (Token name : names)
            if (name instanceof NameToken)
                this.names.add((NameToken)name);
    }

    public List<NameToken> getNames() {
        return names;
    }

    public String toName(char delimiter){
        StringBuilder builder = new StringBuilder();
        ListIterator<NameToken> iterator = names.listIterator();
        while (iterator.hasNext()){
            NameToken tk = iterator.next();
            builder.append(tk.getName());
            if (iterator.hasNext())
                builder.append(delimiter);
        }
        return builder.toString();
    }

    public String toName(){
        return toName('\\');
    }

    public boolean isSingle(){
        return names.size() == 1;
    }
}
