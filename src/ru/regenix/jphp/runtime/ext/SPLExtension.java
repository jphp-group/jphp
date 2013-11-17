package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.common.Extension;

public class SPLExtension extends Extension {
    @Override
    public String getName() {
        return "SPL";
    }

    @Override
    public String getVersion() {
        return "~";
    }
}
