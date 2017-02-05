package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.net.WrapNetAddress;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.net.WrapNetStream;
import php.runtime.ext.net.WrapProxy;
import php.runtime.ext.net.WrapURL;
import php.runtime.ext.net.WrapURLConnection;
import php.runtime.ext.support.Extension;

import java.net.*;

public class NetExtension extends Extension {
    public final static String NAMESPACE = "php\\net\\";

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public Status getStatus() {
        return Status.STABLE;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "std", "net" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, WrapNetStream.class);

        registerWrapperClass(scope, Proxy.class, WrapProxy.class);
        registerWrapperClass(scope, URLConnection.class, WrapURLConnection.class);
        registerWrapperClass(scope, URL.class, WrapURL.class);
        registerWrapperClass(scope, InetAddress.class, WrapNetAddress.class);
    }

    @Override
    public void onLoad(Environment env) {
        Stream.registerProtocol(env, "http", WrapNetStream.class);
        Stream.registerProtocol(env, "https", WrapNetStream.class);
        Stream.registerProtocol(env, "ftp", WrapNetStream.class);
    }
}
