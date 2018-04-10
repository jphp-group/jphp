package org.develnext.jphp.ext.text;

import org.apache.commons.text.WordUtils;
import org.develnext.jphp.ext.text.classes.PTextWord;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class TextExtension extends Extension {
    public static final String NS = "text";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        // register classes ...
        registerClass(scope, PTextWord.class);
    }
}
