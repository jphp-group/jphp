package org.develnext.jphp.http.classes.entity;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.reflection.ClassEntity;

import java.io.File;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\HttpFileEntity")
public class WrapHttpFileEntity extends WrapHttpEntity {
    public WrapHttpFileEntity(Environment env, HttpEntity entity) {
        super(env, entity);
    }

    public WrapHttpFileEntity(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg("file"),
            @Arg(value = "contentType", optional = @Optional("null"))
    })
    public Memory __construct(Environment env, Memory... args) {
        File file = FileObject.valueOf(args[0]);
        if (args[1].isNull())
            entity = new FileEntity(file);
        else
            entity = new FileEntity(file, ContentType.create(args[1].toString()));

        return Memory.NULL;
    }
}
