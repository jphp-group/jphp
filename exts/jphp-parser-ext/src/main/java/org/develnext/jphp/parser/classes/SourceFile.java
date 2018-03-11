package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ConstStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.lang.BaseObject;
import php.runtime.lang.exception.BaseParseError;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;

@Namespace(ParserExtension.NS)
public class SourceFile extends BaseObject {
    protected Memory path;
    protected String uniqueId;

    private Context context;
    private ModuleRecord moduleRecord;

    public SourceFile(Environment env) {
        super(env);
    }

    public SourceFile(Environment env, ClassEntity clazz) {
        super(env, clazz);
        moduleRecord = new ModuleRecord(env);
    }

    @Signature
    public void __construct(Environment env, Memory path, String uniqueId) {
        this.path = path;
        this.uniqueId = uniqueId;

        try {
            this.context = new Context(Stream.getInputStream(env, path));
        } catch (WrapIOException e) {
            this.context = new Context(new File(path.toString()));
        }
    }

    @Getter
    public String getUniqueId() {
        return uniqueId;
    }

    @Getter
    public Memory getPath() {
        return path;
    }

    @Getter
    public ModuleRecord getModuleRecord() {
        return moduleRecord;
    }

    @Setter
    public void setModuleRecord(ModuleRecord moduleRecord) {
        this.moduleRecord = moduleRecord;
    }

    @Signature
    public boolean isReadOnly() {
        return !getContext().isLikeFile();
    }

    @Signature({
            @Arg(value = "manager", nativeType = SourceManager.class)
    })
    public Memory update(Environment env, Memory... args) {
        SourceManager manager = args[0].toObject(SourceManager.class);

        try {
            moduleRecord.clear();
            Tokenizer tokenizer = new Tokenizer(context);

            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(manager.env, tokenizer);

            for (ClassStmtToken token : analyzer.getClasses()) {
                moduleRecord.synchronize(env, token, tokenizer);
            }

            for (FunctionStmtToken token : analyzer.getFunctions()) {
                moduleRecord.synchronize(env, token, tokenizer);
            }

            for (ConstStmtToken token : analyzer.getConstants()) {
                moduleRecord.synchronize(env, token, tokenizer);
            }
        } catch (BaseParseError | IOException e) {
        }

        return Memory.NULL;
    }

    public Context getContext() {
        return context;
    }
}
