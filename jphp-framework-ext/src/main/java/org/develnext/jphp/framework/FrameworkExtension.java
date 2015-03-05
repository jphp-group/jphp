package org.develnext.jphp.framework;

import org.develnext.jphp.framework.classes.loader.PClassLoader;
import org.develnext.jphp.framework.classes.loader.PFileClassLoader;
import org.develnext.jphp.framework.classes.loader.PStandardClassLoader;
import org.develnext.jphp.framework.classes.web.WrapCookie;
import org.develnext.jphp.framework.classes.web.WrapHttpServletRequest;
import org.develnext.jphp.framework.classes.web.WrapHttpServletResponse;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrameworkExtension extends Extension {
    public final static String NS = "php\\framework\\";

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PClassLoader.class);
        registerClass(scope, PFileClassLoader.class);
        registerClass(scope, PStandardClassLoader.class);

        registerWrapperClass(scope, HttpServletRequest.class, WrapHttpServletRequest.class);
        registerWrapperClass(scope, HttpServletResponse.class, WrapHttpServletResponse.class);
        registerWrapperClass(scope, Cookie.class, WrapCookie.class);
    }
}
