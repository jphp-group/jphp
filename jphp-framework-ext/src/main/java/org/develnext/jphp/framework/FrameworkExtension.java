package org.develnext.jphp.framework;

import org.develnext.jphp.framework.classes.loader.PClassLoader;
import org.develnext.jphp.framework.classes.loader.PFileClassLoader;
import org.develnext.jphp.framework.classes.loader.PStandardClassLoader;
import org.develnext.jphp.framework.classes.web.*;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

import javax.servlet.http.*;

public class FrameworkExtension extends Extension {
    public final static String NS = "php\\framework\\";

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PClassLoader.class);
        registerClass(scope, PFileClassLoader.class);
        registerClass(scope, PStandardClassLoader.class);

        registerWrapperClass(scope, Cookie.class, WrapHttpCookie.class);
        registerWrapperClass(scope, Part.class, WrapHttpPart.class);
        registerWrapperClass(scope, HttpSession.class, WrapHttpSession.class);

        registerWrapperClass(scope, HttpServletResponse.class, WrapHttpServletResponse.class);
        registerWrapperClass(scope, HttpServletRequest.class, WrapHttpServletRequest.class);
    }
}
