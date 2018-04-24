package org.develnext.jphp.ext.markdown;

import org.develnext.jphp.ext.markdown.classes.PMarkdown;
import org.develnext.jphp.ext.markdown.classes.PMarkdownOptions;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class MarkdownExtension extends Extension {
    public static final String NS = "markdown";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        // register classes ...
        registerClass(scope, PMarkdownOptions.class);
        registerClass(scope, PMarkdown.class);
    }
}
