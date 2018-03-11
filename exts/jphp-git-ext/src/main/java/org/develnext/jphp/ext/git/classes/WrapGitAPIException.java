package org.develnext.jphp.ext.git.classes;

import org.develnext.jphp.ext.git.GitExtension;
import org.eclipse.jgit.api.errors.GitAPIException;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("GitAPIException")
@Reflection.Namespace(GitExtension.NS)
public class WrapGitAPIException extends JavaException {
    public WrapGitAPIException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public WrapGitAPIException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
