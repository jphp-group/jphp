package php.runtime.ext.net;

import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.NetExtension;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Name(NetExtension.NAMESPACE + "Proxy")
public class WrapProxy extends BaseWrapper<Proxy> {
    public WrapProxy(Environment env, Proxy wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapProxy(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Proxy.Type type, String host, int port) {
        __wrappedObject = new Proxy(type, new InetSocketAddress(host, port));
    }

    @Signature
    public String address() {
        return getWrappedObject().address().toString();
    }

    @Signature
    public Proxy.Type type() {
        return getWrappedObject().type();
    }
}
