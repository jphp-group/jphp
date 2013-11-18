package ru.regenix.jphp.tokenizer.token.expr;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FulledNameToken extends NameToken {
    private List<NameToken> names;

    public FulledNameToken(TokenMeta meta) {
        super(meta);
    }

    public FulledNameToken(TokenMeta meta, char sep){
        super(meta);
        this.names = new ArrayList<NameToken>();
        for(String name : StringUtils.split(meta.getWord(), sep)){
            this.names.add(new NameToken(TokenMeta.of( name, this )));
        }
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
