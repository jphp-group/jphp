package org.develnext.jphp.ext.markdown.classes;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.SubscriptExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.LinkResolver;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.superscript.SuperscriptExtension;
import com.vladsch.flexmark.util.options.DataKey;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import org.develnext.jphp.ext.markdown.MarkdownExtension;
import org.develnext.jphp.ext.markdown.support.LinkRendererExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Optional;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name("MarkdownOptions")
@Namespace(MarkdownExtension.NS)
public class PMarkdownOptions extends BaseObject {
    private MutableDataSet options = new MutableDataSet();
    private List<Extension> parserExtensions = Collections.synchronizedList(new ArrayList<>());
    private String softBreak = null;
    private String hardBreak = null;

    public PMarkdownOptions(Environment env, MutableDataSet options) {
        super(env);
        this.options = options;
    }

    public PMarkdownOptions(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public MutableDataSet getNativeOptions() {
        MutableDataSet dataSet = new MutableDataSet();
        dataSet.set(Parser.EXTENSIONS, parserExtensions);

        for (Entry<DataKey, Object> entry : options.getAll().entrySet()) {
            dataSet.set(entry.getKey(), entry.getValue());
        }

        return dataSet;
    }

    @Signature
    public void __construct() {
    }

    @Signature
    public PMarkdownOptions setRenderSoftBreak(String value) {
        options.set(HtmlRenderer.SOFT_BREAK, softBreak);
        return this;
    }

    @Signature
    public PMarkdownOptions setRenderHardBreak(String value) {
        options.set(HtmlRenderer.HARD_BREAK, hardBreak);
        return this;
    }

    @Signature
    public PMarkdownOptions setRenderEscapeHtml(boolean value) {
        options.set(HtmlRenderer.ESCAPE_HTML, value);
        return this;
    }

    @Signature
    public PMarkdownOptions setRenderSuppressHtml(boolean value) {
        options.set(HtmlRenderer.SUPPRESS_HTML, value);
        return this;
    }

    @Signature
    public PMarkdownOptions setRenderIndentSize(int value) {
        options.set(HtmlRenderer.INDENT_SIZE, value);
        return this;
    }

    @Signature
    public PMarkdownOptions addTableExtension() {
        parserExtensions.add(TablesExtension.create());
        return this;
    }

    @Signature
    public PMarkdownOptions addAnchorLinkExtension() {
        parserExtensions.add(AnchorLinkExtension.create());
        return this;
    }

    @Signature
    public PMarkdownOptions addWikiLinkExtension(@Arg(type = HintType.ARRAY) @Optional("null") Memory options) {
        parserExtensions.add(WikiLinkExtension.create());

        Memory linkPrefix = options.valueOfIndex(getEnvironment(), "linkPrefix");
        if (linkPrefix.isNotNull()) {
            this.options.set(WikiLinkExtension.LINK_PREFIX, linkPrefix.toString());
        }

        Memory linkPrefixAbsolute = options.valueOfIndex(getEnvironment(), "linkPrefixAbsolute");
        if (linkPrefixAbsolute.isNotNull()) {
            this.options.set(WikiLinkExtension.LINK_PREFIX_ABSOLUTE, linkPrefixAbsolute.toString());
        }

        Memory imagePrefix = options.valueOfIndex(getEnvironment(), "imagePrefix");
        if (imagePrefix.isNotNull()) {
            this.options.set(WikiLinkExtension.IMAGE_PREFIX, imagePrefix.toString());
        }

        Memory imagePrefixAbsolute = options.valueOfIndex(getEnvironment(), "imagePrefixAbsolute");
        if (imagePrefixAbsolute.isNotNull()) {
            this.options.set(WikiLinkExtension.IMAGE_PREFIX_ABSOLUTE, imagePrefixAbsolute.toString());
        }

        Memory imageFileExtension = options.valueOfIndex(getEnvironment(), "imageFileExtension");
        if (imageFileExtension.isNotNull()) {
            this.options.set(WikiLinkExtension.IMAGE_FILE_EXTENSION, imageFileExtension.toString());
        }

        Memory imageLinks = options.valueOfIndex(getEnvironment(), "imageLinks");
        if (imageLinks.isNotNull()) {
            this.options.set(WikiLinkExtension.IMAGE_LINKS, imageLinks.toBoolean());
        }

        Memory disableRendering = options.valueOfIndex(getEnvironment(), "disableRendering");
        if (disableRendering.isNotNull()) {
            this.options.set(WikiLinkExtension.DISABLE_RENDERING, disableRendering.toBoolean());
        }

        Memory allowAnchors = options.valueOfIndex(getEnvironment(), "allowAnchors");
        if (allowAnchors.isNotNull()) {
            this.options.set(WikiLinkExtension.ALLOW_ANCHORS, allowAnchors.toBoolean());
        }

        Memory allowInlines = options.valueOfIndex(getEnvironment(), "allowInlines");
        if (allowInlines.isNotNull()) {
            this.options.set(WikiLinkExtension.ALLOW_INLINES, allowInlines.toBoolean());
        }

        return this;
    }

    @Signature
    public PMarkdownOptions addSubscriptExtension(@Arg(type = HintType.ARRAY) @Optional("null") Memory options) {
        parserExtensions.add(SubscriptExtension.create());

        Memory htmlOpen = options.valueOfIndex(getEnvironment(), "htmlOpen");
        if (htmlOpen.isNotNull()) {
            this.options.set(SubscriptExtension.SUBSCRIPT_STYLE_HTML_OPEN, htmlOpen.toString());
        }

        Memory htmlClose = options.valueOfIndex(getEnvironment(), "htmlClose");
        if (htmlOpen.isNotNull()) {
            this.options.set(SubscriptExtension.SUBSCRIPT_STYLE_HTML_CLOSE, htmlClose.toString());
        }

        return this;
    }

    @Signature
    public PMarkdownOptions addSuperscriptExtension(@Arg(type = HintType.ARRAY) @Optional("null") Memory options) {
        parserExtensions.add(SuperscriptExtension.create());

        Memory htmlOpen = options.valueOfIndex(getEnvironment(), "htmlOpen");
        if (htmlOpen.isNotNull()) {
            this.options.set(SuperscriptExtension.SUPERSCRIPT_STYLE_HTML_OPEN, htmlOpen.toString());
        }

        Memory htmlClose = options.valueOfIndex(getEnvironment(), "htmlClose");
        if (htmlOpen.isNotNull()) {
            this.options.set(SuperscriptExtension.SUPERSCRIPT_STYLE_HTML_CLOSE, htmlClose.toString());
        }

        return this;
    }

    @Signature
    public PMarkdownOptions addEmojiExtension(@Arg(type = HintType.ARRAY) @Optional("null") Memory options) {
        parserExtensions.add(EmojiExtension.create());

        Memory imageType = options.valueOfIndex(getEnvironment(), "imageType");
        if (!imageType.isNull()) {
            this.options.set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.valueOf(imageType.toString()));
        }

        Memory shortcutType = options.valueOfIndex(getEnvironment(), "shortcutType");
        if (!shortcutType.isNull()) {
            this.options.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.valueOf(shortcutType.toString()));
        }

        Memory imagePath = options.valueOfIndex(getEnvironment(), "imagePath");
        if (!shortcutType.isNull()) {
            this.options.set(EmojiExtension.ROOT_IMAGE_PATH, imagePath.toString());
        }

        Memory imageClass = options.valueOfIndex(getEnvironment(), "imageClass");
        if (!shortcutType.isNull()) {
            this.options.set(EmojiExtension.ATTR_IMAGE_CLASS, imageClass.toString());
        }

        Memory imageSize = options.valueOfIndex(getEnvironment(), "imageSize");
        if (!shortcutType.isNull()) {
            this.options.set(EmojiExtension.ATTR_IMAGE_SIZE, imageSize.toString());
        }

        return this;
    }

    /*@Signature
    public PMarkdownOptions addLinkRendererExtension(Environment env, Invoker invoker) {
        parserExtensions.add(LinkRendererExtension.create(new LinkResolver() {
            @Override
            public ResolvedLink resolveLink(Node node, LinkResolverContext context, ResolvedLink link) {
                Memory memory = invoker.callAny(ArrayMemory.ofNullableBean(env, link));

                return new ResolvedLink(LinkType, memory.valueOfIndex("url"))
            }
        }));
        return this;
    }*/
}
