package org.develnext.jphp.ext.crypto.classes;


import org.develnext.jphp.ext.crypto.CryptoExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Reflection.Name("KeyPairGenerator")
@Reflection.Namespace(CryptoExtension.NS)
public class WrapKeyPairGenerator extends BaseWrapper<KeyPairGenerator> {
    public WrapKeyPairGenerator(Environment env, KeyPairGenerator wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapKeyPairGenerator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public void __construct(String algorithm, int keySize) throws NoSuchAlgorithmException {
        __wrappedObject = KeyPairGenerator.getInstance(algorithm);
        getWrappedObject().initialize(keySize);
    }

    @Reflection.Signature
    public List<Key> newKeys() throws NoSuchAlgorithmException {
        KeyPair keyPair = getWrappedObject().generateKeyPair();

        List<Key> keys = new ArrayList<>();
        keys.add(keyPair.getPrivate());
        keys.add(keyPair.getPublic());

        return keys;
    }
}
