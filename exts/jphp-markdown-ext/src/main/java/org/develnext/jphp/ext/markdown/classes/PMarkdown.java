package org.develnext.jphp.ext.markdown.classes;

import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.develnext.jphp.ext.markdown.MarkdownExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Name("Markdown")
@Namespace(MarkdownExtension.NS)
public class PMarkdown extends BaseObject {
    private HtmlRenderer renderer;
    private Parser parser;

    public PMarkdown(Environment env, HtmlRenderer renderer, Parser parser) {
        super(env);
        this.renderer = renderer;
        this.parser = parser;
    }

    public PMarkdown(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        renderer = HtmlRenderer.builder().build();
        parser = Parser.builder().build();
    }

    @Signature
    public void __construct(PMarkdownOptions options) {
        renderer = HtmlRenderer.builder(options.getNativeOptions()).build();
        parser = Parser.builder(options.getNativeOptions()).build();
    }

    @Signature
    public String render(String content) {
        Document document = parser.parse(content.toString());
        return renderer.render(document);
    }
}
