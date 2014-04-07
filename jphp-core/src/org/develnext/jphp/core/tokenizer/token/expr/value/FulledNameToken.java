package org.develnext.jphp.core.tokenizer.token.expr.value;

import php.runtime.Information;
import php.runtime.common.StringUtils;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FulledNameToken extends NameToken {
    private List<NameToken> names;
    private boolean absolutely;
    private boolean processed;

    public FulledNameToken(FulledNameToken token) {
        this(token.meta, Information.NAMESPACE_SEP_CHAR);
        names = token.names;
        absolutely = token.absolutely;
    }

    public FulledNameToken(TokenMeta meta) {
        this(meta, Information.NAMESPACE_SEP_CHAR);
    }

    public FulledNameToken(TokenMeta meta, char sep){
        super(meta);
        this.names = new ArrayList<NameToken>();
        String word = meta.getWord();

        this.absolutely = word.startsWith(String.valueOf(sep));
        if (absolutely)
            word = word.substring(1);

        for(String name : StringUtils.split(word, sep)){
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

    public NameToken getLastName(){
        return names.get(names.size() - 1);
    }

    public boolean isAbsolutely() {
        return absolutely;
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

    @Override
    public String getName() {
        return toName();
    }

    public boolean isSingle(){
        return names.size() == 1;
    }

    public boolean isProcessed() {
        return isAbsolutely() || processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public static FulledNameToken valueOf(String... names){
        List<NameToken> tmp = new ArrayList<NameToken>();
        for(String value : names){
            tmp.add(NameToken.valueOf(value));
        }
        FulledNameToken fulledNameToken = new FulledNameToken(TokenMeta.empty(), tmp);
        return fulledNameToken;
    }
}
