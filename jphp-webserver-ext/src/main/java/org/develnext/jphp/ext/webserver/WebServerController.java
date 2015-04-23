package org.develnext.jphp.ext.webserver;

import org.develnext.jphp.ext.webserver.classes.PWebRequest;
import org.develnext.jphp.ext.webserver.classes.PWebResponse;
import org.develnext.jphp.ext.webserver.classes.PWebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ObjectMemory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/**")
public class WebServerController {
    protected final static Map<SpringApplication, PWebServer> webServerMap = new HashMap<SpringApplication, PWebServer>();

    public static SpringApplication application;

    @RequestMapping
    public void get(OutputStream stream, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        PWebServer webServer = webServerMap.get(application);

        Environment environment = webServer.getEnvironment();

        Environment requestEnvironment;

        CompileScope scope = environment.getScope();

        if (!webServer.isIsolated()) {
            requestEnvironment = new Environment(environment);
        } else {
            if (webServer.isHotReload()) {
                scope = new CompileScope(scope);
            }

            requestEnvironment = new Environment(scope);
        }

        requestEnvironment.getDefaultBuffer().setOutput(stream);

        Invoker onRequest = webServer.getOnRequest().forEnvironment(requestEnvironment);

        try {
            onRequest.call(
                    ObjectMemory.valueOf(new PWebRequest(requestEnvironment, request)),
                    ObjectMemory.valueOf(new PWebResponse(requestEnvironment, response))
            );
        } catch (Throwable throwable) {
            Environment.catchThrowable(throwable);
        } finally {
            requestEnvironment.doFinal();
        }
    }

    public static void registerServer(PWebServer webServer) {
        webServerMap.put(webServer.getApplication(), webServer);
    }
}
