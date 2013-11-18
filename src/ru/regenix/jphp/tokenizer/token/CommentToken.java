package ru.regenix.jphp.tokenizer.token;

import ru.regenix.jphp.tokenizer.TokenType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class CommentToken extends Token {

    public enum Kind { SIMPLE, BLOCK, DOCTYPE }

    private Kind kind;
    private String comment;

    public CommentToken(TokenMeta meta) {
        super(meta, TokenType.T_DOC_COMMENT);
        if (meta.getWord().startsWith("/**")){
            kind = Kind.DOCTYPE;
        } else if (meta.getWord().startsWith("//")){
            kind = Kind.SIMPLE;
        } else
            kind = Kind.BLOCK;

        this.comment = getComment();
    }

    public Kind getKind() {
        return kind;
    }

    public String getComment(){
        if (comment != null)
            return comment;

        switch (kind){
            case DOCTYPE: {
                int i = 0;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new StringReader(getWord().substring(3)));
                String line;
                try {
                    while ((line = reader.readLine()) != null){
                        line = line.trim();
                        if (line.startsWith("*"))
                            line = line.substring(1).trim();

                        if (!line.isEmpty()){
                            if (i != 0)
                                builder.append("\n");
                            builder.append(line);
                            i++;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return builder.toString();
            }
            default:
                return getWord().substring(2);
        }
    }
}
