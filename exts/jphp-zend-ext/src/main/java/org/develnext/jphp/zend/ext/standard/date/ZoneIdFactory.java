package org.develnext.jphp.zend.ext.standard.date;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

public final class ZoneIdFactory {
    // TODO break region base timezones on continents or countries.
    private static final Map<String, List<TimezoneWithAlias>> idToAliases;
    private static final Map<String, List<Timezone>> ABBREVIATION;

    private static final String TZ_ABBREVIATIONS_JSON = "tzdb.json";
    private static final Type TYPE = new TypeToken<CaseInsensitiveMap<List<Timezone>>>() {
    }.getType();

    private static final Set<String>[] array = new Set[11];

    static {
        for (int i = 0; i < array.length; i++) {
            array[i] = new TreeSet<>();
        }

        ABBREVIATION = readTZAbbreviations();

        CaseInsensitiveMap<List<TimezoneWithAlias>> tmp = new CaseInsensitiveMap<>();
        ABBREVIATION.forEach((alias, value) -> value.stream()
                .filter(Timezone::hasTimezoneId)
                .forEach(timezone -> {
                    String timezoneId = timezone.getTimezoneId();
                    addToArray(timezoneId);

                    tmp.compute(timezoneId, remapped(alias, timezone));
                }));

        tmp.put("US/Eastern", Arrays.asList(
                new Timezone(true, -14400, "US/Eastern").withAlias("EDT"),
                new Timezone(false, -18000, "US/Eastern").withAlias("EST")
        ));

        idToAliases = Collections.unmodifiableMap(tmp);

        array[10].add("UTC");
    }

    private static void addToArray(String timezoneId) {
        int slashIdx = timezoneId.indexOf('/');
        if (slashIdx == -1) return;

        String fistPart = timezoneId.substring(0, slashIdx);
        int idx = zoneAreaToIndex(fistPart);
        if (idx == -1) return;

        array[idx].add(timezoneId);
    }

    private static int zoneAreaToIndex(String area) {
        switch (area) {
            case "Africa":
                return 0;
            case "America":
            case "Canada":
            case "Mexico":
            case "Chile":
                return 1;
            case "Antarctica":
                return 2;
            case "Arctic":
                return 3;
            case "Asia":
                return 4;
            case "Atlantic":
                return 5;
            case "Australia":
                return 6;
            case "Europe":
                return 7;
            case "Indian":
                return 8;
            case "Pacific":
                return 9;
            case "Etc":
                return 10;
            default:
                return -1;
        }
    }

    private static BiFunction<String, List<TimezoneWithAlias>, List<TimezoneWithAlias>> remapped(String alias,
                                                                                                 Timezone timezone) {
        return (k, twa) -> {
            twa = twa == null ? new ArrayList<>() : twa;
            twa.add(timezone.withAlias(alias));
            twa.sort(Comparator.comparing(o -> o.alias));
            return twa;
        };
    }

    private static Map<String, List<Timezone>> readTZAbbreviations() {
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

    public static String aliasFor(ZonedDateTime dateTime) {
        String id = dateTime.getZone().getId();
        switch (id) {
            case "GMT":
            case "UTC":
                return id;
        }

        List<TimezoneWithAlias> aliases = idToAliases.get(id);
        if (aliases == null) {
            return null;
        }

        ZoneId zone = dateTime.getZone();
        ZoneRules rules = zone.getRules();
        boolean dst = zone.getRules().isDaylightSavings(dateTime.toInstant());
        ZoneOffset zoneOffset = rules.getOffset(dateTime.toInstant());

        int offset = zoneOffset.getTotalSeconds();

        for (TimezoneWithAlias alias : aliases) {
            if (alias.timezone.getOffset() == offset && alias.timezone.isDst() == dst) {
                return alias.alias.toUpperCase();
            }
        }

        return null;
    }

    public static boolean isAbbreviation(String tzAbbr) {
        return ABBREVIATION.get(tzAbbr) != null;
    }

    public static ZoneId of(String id) {
        try {
            if (id.startsWith("GMT") && id.length() > 4) {
                // the PHP considers timezones like "GMT+0100" offset based, Java does not.
                // we should strip away GMT prefix to get offset based zone.
                // Although timezone "GMT0" is region based
                id = id.substring(3);
            }
            return ZoneId.of(id);
        } catch (DateTimeException e) {
            int offset = Optional.ofNullable(ABBREVIATION.get(id))
                    .map(Collection::iterator)
                    .map(Iterator::next)
                    .map(Timezone::getOffset)
                    .orElse(Integer.MIN_VALUE);

            // id is not abbreviation
            if (offset == Integer.MIN_VALUE) {
                List<TimezoneWithAlias> timezoneWithAliases = idToAliases.get(id);
                if (timezoneWithAliases != null && !timezoneWithAliases.isEmpty()) {
                    return ZoneOffset.ofTotalSeconds(timezoneWithAliases.get(0).timezone.getOffset());
                }
                throw e;
            }

            return ZoneOffset.ofTotalSeconds(offset);
        }
    }

    public static String abbrToRegion(String abbreviation, int offset, int isDst) {
        switch (abbreviation) {
            case "GMT":
            case "UTC":
                return "UTC";
            default: {
                if (StringUtils.isBlank(abbreviation)) {
                    if (offset == -1 && isDst == -1) {
                        return null;
                    }

                    // find only with offset
                    if (isDst == -1) {
                        return abbreviationByOffset(offset);
                    }

                    return abbreviationByOffsetAndDst(offset, isDst == 1);
                }

                List<Timezone> timezoneItems = ABBREVIATION.get(abbreviation);

                if (timezoneItems == null) {
                    return null;
                }

                Predicate<Timezone> filter = timezone -> true;

                if (offset != -1) {
                    filter = filter.and(timezone -> timezone.getOffset() == offset);
                }

                if (isDst != -1) {
                    filter = filter.and(timezone -> (isDst == 0) != timezone.isDst());
                }

                if (offset == -1 && isDst == -1) {
                    return timezoneItems.iterator().next().timezoneId;
                }

                return timezoneItems.stream()
                        .filter(filter)
                        .map(Timezone::getTimezoneId)
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    private static String abbreviationByOffsetAndDst(int offset, boolean dst) {
        for (List<Timezone> value : ABBREVIATION.values()) {
            for (Timezone timezone : value) {
                if (timezone.getOffset() == offset && timezone.isDst() == dst) {
                    return timezone.getTimezoneId();
                }
            }
        }

        return null;
    }

    private static String abbreviationByOffset(int offset) {
        return ABBREVIATION.values().stream()
                .flatMap(Collection::stream)
                .filter(timezone -> timezone.getOffset() == offset)
                .map(Timezone::getTimezoneId)
                .findFirst()
                .orElse(null);
    }

    public static ArrayMemory listAbbreviations() {
        ArrayMemory array = ArrayMemory.createHashed(ABBREVIATION.size());

        ABBREVIATION.forEach((s, timezones) -> {
            List<ArrayMemory> collect = timezones.stream().map(Timezone::toArrayMemory)
                    .collect(Collectors.toList());

            ArrayMemory item = ArrayMemory.ofCollection(collect).toConstant();
            array.putAsKeyString(s, item);
        });

        return array;
    }

    public static String[] listIdentifiers(int what, String country) {
        if ((what | DateTimeZone.ALL) == DateTimeZone.ALL) {
            return Stream.of(array).flatMap(Collection::stream).toArray(String[]::new);
        }

        List<String> result = new ArrayList<>();

        if ((what | DateTimeZone.AFRICA) == DateTimeZone.AFRICA) {
            result.addAll(array[0]);
        }

        if ((what | DateTimeZone.AMERICA) == DateTimeZone.AMERICA) {
            result.addAll(array[1]);
        }

        return result.toArray(new String[0]);
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

        TimezoneWithAlias withAlias(String alias) {
            return new TimezoneWithAlias(alias, this);
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

    private static final class TimezoneWithAlias {
        private final String alias;
        private final Timezone timezone;

        private TimezoneWithAlias(String alias, Timezone timezone) {
            this.alias = alias;
            this.timezone = timezone;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", TimezoneWithAlias.class.getSimpleName() + "[", "]")
                    .add("alias='" + alias + "'")
                    .add("timezone=" + timezone)
                    .toString();
        }
    }
}
