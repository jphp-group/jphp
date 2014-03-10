package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.TokenMeta;

import java.util.ArrayList;
import java.util.List;

public class ConstStmtToken extends StmtToken {
    private ClassStmtToken clazz;
    private NamespaceStmtToken namespace;

    public final List<Item> items;

    public ConstStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CONST);
        items = new ArrayList<Item>();
    }

    public ClassStmtToken getClazz() {
        return clazz;
    }

    public Item add(NameToken name, ExprStmtToken value){
        Item el = new Item(name, value);
        items.add(el);
        return el;
    }

    public void setClazz(ClassStmtToken clazz) {
        this.clazz = clazz;
        this.namespace = null;
    }

    public NamespaceStmtToken getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceStmtToken namespace) {
        if (clazz != null)
            throw new IllegalArgumentException("Cannot set namespace for a class constant");
        this.namespace = namespace;
    }

    public class Item {
        public final NameToken name;
        public final ExprStmtToken value;

        public Item(NameToken name,
            ExprStmtToken value) {
            this.name = name;
            this.value = value;
        }

        public String getFulledName(char delimiter){
            return namespace == null || namespace.getName() == null
                ? name.getName()
                : namespace.getName().toName(delimiter) + delimiter + name.getName();
        }

        public String getFulledName(){
            return getFulledName('\\');
        }
    }
}
