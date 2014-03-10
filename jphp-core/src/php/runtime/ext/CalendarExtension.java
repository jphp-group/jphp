package php.runtime.ext;

import php.runtime.ext.support.Extension;

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
