package org.develnext.jphp.framework.classes.web;

import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.exceptions.JPHPException;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.loader.StandaloneLoader;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.MethodEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrameworkServlet extends HttpServlet {
    protected StandaloneLoader loader;

    @Override
    public void init() throws ServletException {
        super.init();

        loader = new StandaloneLoader(this.getClass().getClassLoader());
        loader.run(".init");
    }

    protected void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Environment env = new Environment(loader.getScope(), resp.getOutputStream());

        try {
            ArrayMemory locals = new ArrayMemory();
            locals.refOfIndex("request").assign(new WrapHttpServletRequest(env, req));
            locals.refOfIndex("response").assign(new WrapHttpServletResponse(env, resp));

            loader.fetchModule(".route").includeNoThrow(env, locals);
        } catch (Exception e) {
            env.catchUncaught(e);
        } finally {
            try {
                env.doFinal();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        doRequest(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
        doRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        doRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
        doRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        doRequest(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
        doRequest(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
        doRequest(req, resp);
    }
}
