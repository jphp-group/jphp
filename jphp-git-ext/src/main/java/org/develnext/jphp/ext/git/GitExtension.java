package org.develnext.jphp.ext.git;

import org.develnext.jphp.ext.git.classes.WrapGit;
import org.develnext.jphp.ext.git.classes.WrapGitAPIException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class GitExtension extends Extension {
    public static final String NS = "git";
    
    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "git "};
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, Git.class, WrapGit.class);
        registerJavaException(scope, WrapGitAPIException.class, GitAPIException.class);
    }
}
