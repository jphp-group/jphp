package org.develnext.jphp.zend.ext.standard.date;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.common.collections.map.CaseInsensitiveMap;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

public class ZoneIdFactory {
    private static final Map<String, String> idToAliases;
    private static final Map<String, Set<Timezone>> abbr;

    private static final String TZ_ABBREVIATIONS_JSON = "tz_abbreviations.json";
    private static final Type TYPE = new TypeToken<CaseInsensitiveMap<LinkedHashSet<Timezone>>>() {
    }.getType();

    static {
        abbr = readTZAbbreviations();

        CaseInsensitiveMap<String> tmpMap = new CaseInsensitiveMap<>();
        abbr.forEach((key, value) -> value.stream()
                .filter(Timezone::hasTimezoneId)
                .map(Timezone::getTimezoneId)
                .forEach(id -> tmpMap.put(id, key)));

        idToAliases = Collections.unmodifiableMap(tmpMap);
    }

    private static Map<String, Set<Timezone>> readTZAbbreviations() {
        InputStream stream = ZoneIdFactory.class.getClassLoader().getResourceAsStream(TZ_ABBREVIATIONS_JSON);
        if (stream == null) {
            return Collections.emptyMap();
        }

        try (Reader r = new InputStreamReader(stream)) {
            return new GsonBuilder()
                    .registerTypeAdapter(Timezone.class, new TimezoneDeserializer())
                    .create()
                    .fromJson(r, TYPE);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static String aliasFor(ZoneId zoneId) {
        String id = zoneId.getId();
        switch (id) {
            case "GMT":
                return id;
            default:
                String alias = idToAliases.get(id);

                return alias == null ? null : alias.toUpperCase();
        }
    }

    public static ZoneId of(String id) {
        try {
            return ZoneId.of(id);
        } catch (DateTimeException e) {
            int offset = Optional.ofNullable(abbr.get(id))
                    .map(Collection::iterator)
                    .map(Iterator::next)
                    .map(Timezone::getOffset)
                    .orElse(0);

            if (offset == 0)
                throw e;

            ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(offset);
            return zoneOffset;
        }
    }

    public static String abbrToRegion(String abbreviation, int offset, int isDst) {
        if (StringUtils.isBlank(abbreviation)) {
            if (offset == -1 && isDst == -1)
                return null;

            if (isDst == -1)
                return abbreviationByOffset(offset);

            return abbreviationByOffsetAndDst(offset, isDst == 1);
        }

        Set<Timezone> timezoneItems = abbr.get(abbreviation);

        if (timezoneItems != null && offset == -1 && isDst == -1) {
            return timezoneItems.iterator().next().timezoneId;
        }

        return null;
    }

    private static String abbreviationByOffsetAndDst(int offset, boolean dst) {
        for (Set<Timezone> value : abbr.values()) {
            for (Timezone timezone : value) {
                if (timezone.getOffset() == offset && timezone.isDst() == dst) {
                    return timezone.getTimezoneId();
                }
            }
        }

        return null;
    }

    private static String abbreviationByOffset(int offset) {
        String tz = abbr.values().stream()
                .flatMap(Collection::stream)
                .filter(timezone -> {
                    boolean b = timezone.getOffset() == offset;
                    return b;
                })
                .map(Timezone::getTimezoneId)
                .findFirst()
                .orElse(null);

        return tz;
    }

    public static ArrayMemory listAbbreviations() {
        ArrayMemory array = ArrayMemory.createHashed(abbr.size());

        abbr.forEach((s, timezones) -> {
            List<ArrayMemory> collect = timezones.stream().map(Timezone::toArrayMemory)
                    .collect(Collectors.toList());

            ArrayMemory item = ArrayMemory.ofCollection(collect).toConstant();
            array.putAsKeyString(s, item);
        });

        return array;
    }

    private static class TimezoneDeserializer implements JsonDeserializer<Timezone> {

        @Override
        public Timezone deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            return new Timezone(
                    jsonObject.get("dst").getAsBoolean(),
                    jsonObject.get("offset").getAsInt(),
                    jsonObject.get("timezone_id").isJsonNull() ? null : jsonObject.get("timezone_id").getAsString()
            );
        }
    }

    private static final class Timezone {
        private final boolean dst;
        private final int offset;
        private final String timezoneId;

        private Timezone(boolean dst, int offset, String timezoneId) {
            this.dst = dst;
            this.offset = offset;
            this.timezoneId = timezoneId;
        }

        boolean isDst() {
            return dst;
        }

        public int getOffset() {
            return offset;
        }

        String getTimezoneId() {
            return timezoneId;
        }

        boolean hasTimezoneId() {
            return getTimezoneId() != null;
        }

        ArrayMemory toArrayMemory() {
            ArrayMemory memory = ArrayMemory.createHashed(3);
            memory.putAsKeyString("dst", dst ? Memory.TRUE : Memory.FALSE);
            memory.putAsKeyString("offset", LongMemory.valueOf(offset));
            memory.putAsKeyString("timezone_id", StringMemory.valueOf(timezoneId));

            return memory.toConstant();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Timezone)) return false;
            Timezone that = (Timezone) o;
            return dst == that.dst &&
                    offset == that.offset &&
                    Objects.equals(timezoneId, that.timezoneId);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Timezone.class.getSimpleName() + "[", "]")
                    .add("dst=" + dst)
                    .add("offset=" + offset)
                    .add("timezoneId='" + timezoneId + "'")
                    .toString();
        }

        @Override
        public int hashCode() {
            return Objects.hash(dst, offset, timezoneId);
        }
    }
}
