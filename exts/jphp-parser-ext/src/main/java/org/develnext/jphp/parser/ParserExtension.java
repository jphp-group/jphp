package org.develnext.jphp.parser;

import org.develnext.jphp.parser.classes.*;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class ParserExtension extends Extension {
    public static final String NS = "phpx\\parser";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, AbstractSourceRecord.class);
        registerClass(scope, ClassRecord.class);
        registerClass(scope, MethodRecord.class);
        registerClass(scope, NamespaceRecord.class);
        registerClass(scope, ModuleRecord.class);
        registerClass(scope, UseImportRecord.class);

        registerClass(scope, SourceWriter.class);
        registerClass(scope, SourceFile.class);
        registerClass(scope, SourceManager.class);

        registerClass(scope, SourceToken.class);
        registerClass(scope, SourceTokenizer.class);
    }
}
