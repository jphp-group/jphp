package org.develnext.jphp.ext.webserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class WebServerConfig extends WebMvcConfigurerAdapter {
    public interface Handlers {
        void addResourceHandlers(ResourceHandlerRegistry registry);
    }

    public static Handlers HANDLERS;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (HANDLERS != null) {
            HANDLERS.addResourceHandlers(registry);
        }
    }
}
