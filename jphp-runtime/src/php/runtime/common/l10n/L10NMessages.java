package php.runtime.common.l10n;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import php.runtime.common.Messages.Item;

abstract public class L10NMessages {
    abstract public String getLang();

    private static final Map<String, L10NMessages> langs = new HashMap<>();

    synchronized public final static void register(L10NMessages messages) {
        langs.put(messages.getLang(), messages);
    }

    public static final L10NMessages get(String lang) {
        return langs.get(lang);
    }

    public static final Set<String> getLangs() {
        return langs.keySet();
    }

    public Map<String, String> getMessages() {
        Map<String, String> result = new HashMap<>();

        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            Object o = null;
            try {
                o = field.get(this);
                if (o instanceof String) {
                    result.put(field.getName(), ((String) o));
                }
            } catch (IllegalAccessException e) {
                // nop.
            }
        }

        return result;
    }
}
