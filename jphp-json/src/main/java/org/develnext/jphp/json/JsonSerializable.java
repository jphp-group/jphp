package org.develnext.jphp.json;

import php.runtime.Memory;
import php.runtime.env.Environment;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("JsonSerializable")
public interface JsonSerializable {

    @Signature
    Memory jsonSerialize(Environment env, Memory... args);
}
