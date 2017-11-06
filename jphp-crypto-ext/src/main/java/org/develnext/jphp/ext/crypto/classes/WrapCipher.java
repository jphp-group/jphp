package org.develnext.jphp.ext.crypto.classes;

import org.develnext.jphp.ext.crypto.CryptoExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Name("Cipher")
@Namespace(CryptoExtension.NS)
public class WrapCipher extends BaseWrapper<Cipher> {
    private Charset charset = StandardCharsets.UTF_8;

    public WrapCipher(Environment env, Cipher wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapCipher(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        __construct(algorithm, "UTF-8");
    }

    @Signature
    public void __construct(String algorithm, String charset) throws NoSuchPaddingException, NoSuchAlgorithmException {
        __wrappedObject = Cipher.getInstance(algorithm);
        this.charset = Charset.forName(charset);
    }

    @Signature
    public Memory encode(Environment env, Memory data, Key key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        getWrappedObject().init(Cipher.ENCRYPT_MODE, key);
        byte[] result = getWrappedObject().doFinal(data.getBinaryBytes(charset));
        return new BinaryMemory(result);
    }

    @Signature
    public Memory decode(Environment env, Memory data, Key key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        getWrappedObject().init(Cipher.DECRYPT_MODE, key);
        byte[] result = getWrappedObject().doFinal(data.getBinaryBytes(charset));
        return StringMemory.valueOf(new String(result, charset));
    }
}
