package org.develnext.jphp.ext.webserver;

import org.develnext.jphp.ext.webserver.classes.PWebRequest;
import org.develnext.jphp.ext.webserver.classes.PWebResponse;
import org.develnext.jphp.ext.webserver.classes.PWebServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.env.SplClassLoader;
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
    protected final static Map<String, PWebServer> webServerMap = new HashMap<>();

    @Value("${_server.id}")
    protected String serverId;

    @RequestMapping
    public void get(OutputStream stream, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        PWebServer webServer = webServerMap.get(serverId);

        Environment environment = webServer.getEnvironment();

        CompileScope scope = environment.getScope();

        Environment requestEnvironment;

        if (!webServer.isIsolated()) {
            requestEnvironment = new Environment(environment);
        } else {
            if (webServer.isHotReload()) {
                scope = new CompileScope(scope);
            }

            requestEnvironment = new Environment(scope);
        }

        requestEnvironment.getDefaultBuffer().setOutput(stream);

        if (webServer.isImportAutoloaders()) {
            for (SplClassLoader loader : environment.getClassLoaders()) {
                requestEnvironment.registerAutoloader(loader.forEnvironment(requestEnvironment), false);
            }
        }

        Invoker onRequest = webServer.getOnRequest().forEnvironment(requestEnvironment);

        PWebRequest webRequest = new PWebRequest(requestEnvironment, request);
        PWebResponse webResponse = new PWebResponse(requestEnvironment, response);

        requestEnvironment.setUserValue(webServer);
        requestEnvironment.setUserValue(webRequest);
        requestEnvironment.setUserValue(webResponse);

        try {
            onRequest.call(ObjectMemory.valueOf(webRequest), ObjectMemory.valueOf(webResponse));
        } catch (Throwable throwable) {
            Environment.catchThrowable(throwable, requestEnvironment);
        } finally {
            requestEnvironment.doFinal();
        }
    }

    public static void registerServer(PWebServer webServer) {
        webServerMap.put(webServer.getId(), webServer);
    }
}
