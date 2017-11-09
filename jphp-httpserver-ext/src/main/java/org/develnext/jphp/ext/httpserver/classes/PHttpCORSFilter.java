package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Name("HttpCORSFilter")
@Namespace(HttpServerExtension.NS)
public class PHttpCORSFilter extends PHttpAbstractHandler {
    private Set<String> allowedDomain;
    private Set<String> allowedMethods;
    private Set<String> allowedHeaders;
    private boolean allowedCredentials;
    private long maxAge;

    public PHttpCORSFilter(Environment env) {
        super(env);
    }

    public PHttpCORSFilter(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __construct(Collections.singleton("*"));
    }

    @Signature
    public void __construct(Set<String> allowedDomain) {
        __construct(allowedDomain,
                new LinkedHashSet<>(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH")),
                Collections.emptySet()
        );
    }

    @Signature
    public void __construct(Set<String> allowedDomain,
                            Set<String> allowedMethods) {
        __construct(allowedDomain, allowedMethods, Collections.emptySet());
    }

    @Signature
    public void __construct(Set<String> allowedDomain,
                            Set<String> allowedMethods,
                            Set<String> allowedHeaders) {
        __construct(allowedDomain, allowedMethods, allowedHeaders, true);

    }

    @Signature
    public void __construct(Set<String> allowedDomain,
                            Set<String> allowedMethods,
                            Set<String> allowedHeaders,
                            boolean allowedCredentials) {
        __construct(allowedDomain, allowedMethods, allowedHeaders, allowedCredentials, 0);
    }

    @Signature
    public void __construct(Set<String> allowedDomain,
                            Set<String> allowedMethods,
                            Set<String> allowedHeaders,
                            boolean allowedCredentials,
                            long maxAge) {
        this.allowedDomain = allowedDomain;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.allowedCredentials = allowedCredentials;
        this.maxAge = maxAge;
    }

    @Signature
    public Set<String> allowDomains() {
        return allowedDomain;
    }

    @Signature
    public Set<String> allowMethods() {
        return allowedMethods;
    }

    @Signature
    public Set<String> allowHeaders() {
        return allowedHeaders;
    }

    @Signature
    public boolean allowCredentials() {
        return allowedCredentials;
    }

    @Signature
    public long maxAge() {
        return maxAge;
    }

    @Signature
    public void __invoke(PHttpServerRequest request, PHttpServerResponse response) {
        String method = request.method().toUpperCase();

        if ("OPTIONS".equals(method)) {
            handleOptions(request, response);
            request.end();
            return;
        }

        URI fetchOrigin = fetchOrigin(request);
        if (fetchOrigin != null) {
            String originDomain = fetchOriginDomain(fetchOrigin);

            if (allowedDomain.contains("*") || allowedDomain.contains(originDomain)) {
                if (allowedCredentials) {
                    response.getResponse()
                            .addHeader("Access-Control-Allow-Credentials", "true");
                }

                response.getResponse()
                        .addHeader("Access-Control-Allow-Origin", fetchOrigin.getScheme() + "://" + originDomain);
            }
        }
    }

    private URI fetchOrigin(PHttpServerRequest request) {
        String origin = request.header("Origin");

        if (origin == null || origin.trim().isEmpty()) {
            return null;
        }

        return URI.create(origin);
    }

    private String fetchOriginDomain(URI uri) {
        if (uri == null) {
            return null;
        }

        String host = uri.getHost();
        int port = uri.getPort();

        return host + (port == 80 || port == -1 ? "" : ":" + port);
    }

    @Signature
    public void handleOptions(PHttpServerRequest request, PHttpServerResponse response) {
        URI uri = fetchOrigin(request);
        if (uri == null) {
            return;
        }

        String originDomain = fetchOriginDomain(uri);

        if (allowedCredentials) {
            response.getResponse()
                    .addHeader("Access-Control-Allow-Credentials", "true");
        }

        if (allowedDomain.contains("*") || allowedDomain.contains(originDomain)) {
            response.getResponse()
                    .addHeader("Access-Control-Allow-Origin", uri.getScheme() + "://" + originDomain);
        }

        if (!allowedMethods.isEmpty()) {
            response.getResponse()
                    .addHeader("Access-Control-Request-Method", StringUtils.join(allowedMethods, ','));
        }

        if (!allowedHeaders.isEmpty()) {
            response.getResponse()
                    .addHeader("Access-Control-Allow-Headers", StringUtils.join(allowedHeaders, ','));
        }

        if (maxAge > 0) {
            response.getResponse()
                    .addHeader("Access-Control-Max-Age", String.valueOf(maxAge));
        }
    }
}
