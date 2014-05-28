package org.develnext.jphp.core.tokenizer.token.expr.operator.cast;

import php.runtime.common.Association;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

abstract public class CastExprToken extends OperatorExprToken {
    public CastExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 21;
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.RIGHT;
    }

    public static CastExprToken valueOf(String name, TokenMeta meta){
        String word = name.toLowerCase();
        if (word.equals("int") || word.equals("integer"))
            return new IntCastExprToken(meta);
        else if (word.equals("float") || word.equals("double") || word.equals("real"))
            return new DoubleCastExprToken(meta);
        else if (word.equals("string"))
            return new StringCastExprToken(meta);
        else if (word.equals("bool") || word.equals("boolean"))
            return new BooleanCastExprToken(meta);
        else if (word.equals("array"))
            return new ArrayCastExprToken(meta);
        else if (word.equals("object"))
            return new ObjectCastExprToken(meta);
        else if (word.equals("unset"))
            return new UnsetCastExprToken(meta);
        else if (word.equals("binary"))
            return new BinaryCastExprToken(meta);

        return null;
    }
}
