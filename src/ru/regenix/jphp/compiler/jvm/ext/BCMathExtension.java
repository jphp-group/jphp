package ru.regenix.jphp.compiler.jvm.ext;

public class BCMathExtension extends JvmExtension {
    @Override
    public String getName() {
        return "bcmath";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
