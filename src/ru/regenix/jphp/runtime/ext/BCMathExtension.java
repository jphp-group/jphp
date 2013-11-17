package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.common.Extension;

public class BCMathExtension extends Extension {
    @Override
    public String getName() {
        return "bcmath";
    }

    @Override
    public String getVersion() {
        return "~";
    }
}
