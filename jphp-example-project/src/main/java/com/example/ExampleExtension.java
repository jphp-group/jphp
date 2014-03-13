package com.example;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.ext.swing.SwingExtension;

public class ExampleExtension extends Extension {
    @Override
    public String getName() {
        return "Example";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String[] getRequiredExtensions() {
        return new String[]{
                SwingExtension.class.getName()
        };
    }

    @Override
    public void onRegister(CompileScope scope) {
        // ...
    }
}
