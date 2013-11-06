package ru.regenix.jphp.ext;

import ru.regenix.jphp.compiler.common.Extension;

public class BCMathExtension extends Extension {
    @Override
    public String getName() {
        return "bcmath";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
