package ru.regenix.jphp.ext;

import ru.regenix.jphp.compiler.common.Extension;

public class CalendarExtension extends Extension {
    @Override
    public String getName() {
        return "calendar";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
