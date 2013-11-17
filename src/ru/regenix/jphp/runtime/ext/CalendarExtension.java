package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.common.Extension;

public class CalendarExtension extends Extension {
    @Override
    public String getName() {
        return "calendar";
    }

    @Override
    public String getVersion() {
        return "~";
    }
}
