package org.develnext.jphp.ext.markdown.support;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.LinkResolver;
import com.vladsch.flexmark.html.LinkResolverFactory;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import java.util.Set;

public class LinkRendererExtension implements HtmlRenderer.HtmlRendererExtension  {
    private final LinkResolver linkResolver;

    public LinkRendererExtension(LinkResolver linkResolver) {
        this.linkResolver = linkResolver;
    }

    @Override
    public void rendererOptions(MutableDataHolder options) {

    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        rendererBuilder.linkResolverFactory(new LinkResolverFactory() {
            @Override
            public Set<Class<? extends LinkResolverFactory>> getAfterDependents() {
                return null;
            }

            @Override
            public Set<Class<? extends LinkResolverFactory>> getBeforeDependents() {
                return null;
            }

            @Override
            public boolean affectsGlobalScope() {
                return false;
            }

            @Override
            public LinkResolver create(LinkResolverContext context) {
                return linkResolver;
            }
        });
    }

    public static LinkRendererExtension create(LinkResolver linkResolver) {
        return new LinkRendererExtension(linkResolver);
    }
}
