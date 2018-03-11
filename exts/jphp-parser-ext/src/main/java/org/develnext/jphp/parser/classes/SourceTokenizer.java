package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.exceptions.ParseException;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Namespace(ParserExtension.NS)
public class SourceTokenizer extends BaseObject {
    protected Tokenizer tokenizer;
    private InputStream stream;

    public SourceTokenizer(Environment env, Tokenizer tokenizer) {
        super(env);
        this.tokenizer = tokenizer;
    }

    public SourceTokenizer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, Memory path, String moduleName, String charset) throws IOException {
        stream = Stream.getInputStream(env, path);

        Context context = new Context(stream, moduleName, Charset.forName(charset));

        tokenizer = new Tokenizer(context);
    }

    @Signature
    public SourceToken next(Environment env) {
        try {
            Token token = tokenizer.nextToken();

            if (token == null) {
                return null;
            }

            return new SourceToken(env, token);
        } catch (ParseException e) {
            return null;
        }
    }

    @Signature
    public List<SourceToken> fetchAll(Environment env) {
        Token token;
        List<SourceToken> result = new ArrayList<>();

        while ((token = tokenizer.nextToken()) != null) {
            result.add(new SourceToken(env, token));
        }

        return result;
    }

    @Signature
    public void close(Environment env) {
        try {
            stream.close();
        } catch (IOException e) {
            ;
        }
    }

    @Signature
    public static List<SourceToken> parseAll(Environment env, String text) {
        Tokenizer tokenizer = new Tokenizer(text, new Context(text));

        Token token;
        List<SourceToken> result = new ArrayList<>();

        try {
            while ((token = tokenizer.nextToken()) != null) {
                result.add(new SourceToken(env, token));
            }
        } catch (ParseException|StringIndexOutOfBoundsException e){ // TODO remove StringIndexOutOfBoundsException bug in jphp!
            return null;
        }

        return result;
    }
}
