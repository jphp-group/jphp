package org.develnext.jphp.ext.crypto.classes;

import org.develnext.jphp.ext.crypto.CryptoExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Name("Key")
@Namespace(CryptoExtension.NS)
public class WrapKey extends BaseWrapper<Key> {
    public WrapKey(Environment env, Key wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapKey(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, Memory data, String algorithm) {
        __wrappedObject = new SecretKeySpec(
                data.getBinaryBytes(env.getDefaultCharset()), algorithm
        );
    }

    @Signature
    public String getFormat() {
        return getWrappedObject().getFormat();
    }

    @Signature
    public byte[] getEncoded() {
        return getWrappedObject().getEncoded();
    }

    @Signature
    public String getAlgorithm() {
        return getWrappedObject().getAlgorithm();
    }
}
