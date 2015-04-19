package org.develnext.jphp.ext.webserver;

import org.develnext.jphp.ext.webserver.classes.PWebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/**")
public class WebServerController {
    protected final static Map<SpringApplication, PWebServer> webServerMap = new HashMap<SpringApplication, PWebServer>();

    public static SpringApplication application;

    @RequestMapping
    public void get(OutputStream stream) throws Throwable {
        PWebServer webServer = webServerMap.get(application);

        Environment environment = webServer.getEnvironment();

        Environment requestEnvironment = new Environment(environment);
        requestEnvironment.getDefaultBuffer().setOutput(stream);

        Invoker onRequest = webServer.getOnRequest().forEnvironment(requestEnvironment);

        try {
            onRequest.call();
            requestEnvironment.flushAll();
        } catch (Exception e) {
            requestEnvironment.getDefaultBuffer().setOutput(System.err);
            requestEnvironment.catchUncaught(e);
            requestEnvironment.flushAll();
        }

        requestEnvironment.doFinal();
    }

    public static void registerServer(PWebServer webServer) {
        webServerMap.put(webServer.getApplication(), webServer);
    }
}
