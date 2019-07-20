package org.develnext.jphp.zend.ext.standard.date;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import php.runtime.Memory;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;

public final class DateTimeParseResult {
    private static final String[] RELATIVE_CONTRIBUTOR_NAMES = {"year", "month", "day", "hour", "minute", "second"};
    private static final String[] RELATIVE_BOOL_CONTRIBUTOR_NAMES = {"first_day_of_month", "last_day_of_month"};

    private final String zone;
    private final Set<TemporalField> modified;
    private final Map<String, Long> relativeContributors;
    private ZonedDateTime dateTime;

    public DateTimeParseResult(ZonedDateTime dateTime, Set<TemporalField> modified, String zone,
                               Map<String, Long> relativeContributors) {
        this.dateTime = dateTime;
        this.modified = Collections.unmodifiableSet(modified);
        this.zone = zone;
        this.relativeContributors = relativeContributors != null ?
                Collections.unmodifiableMap(relativeContributors) : Collections.emptyMap();
    }

    public ZonedDateTime dateTime() {
        return dateTime;
    }

    public DateTimeParseResult dateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public String parsedZone() {
        return zone;
    }

    public boolean hasParsedZone() {
        return zone != null;
    }

    public Set<TemporalField> modified() {
        return modified;
    }

    public ArrayMemory toArrayMemory() {
        ArrayMemory array = ArrayMemory.createHashed(10);

        array.refOfIndex("year").assign(getFieldMemory(YEAR));
        array.refOfIndex("month").assign(getFieldMemory(MONTH_OF_YEAR));
        array.refOfIndex("day").assign((modified.contains(DAY_OF_MONTH) || modified.contains(DAY_OF_YEAR)) ?
                LongMemory.valueOf(dateTime.getDayOfMonth()) :
                Memory.FALSE);
        array.refOfIndex("hour").assign(getFieldMemory(HOUR_OF_DAY));
        array.refOfIndex("minute").assign(getFieldMemory(MINUTE_OF_HOUR));
        array.refOfIndex("second").assign(getFieldMemory(SECOND_OF_MINUTE));

        Memory fraction = getFieldMemory(MICRO_OF_SECOND);
        Memory fractionRef = array.refOfIndex("fraction");
        if (fraction.isBoolean()) {
            fractionRef.assign(fraction);
        } else {
            fractionRef.assign(fraction.mul(0.000001));
        }

        ForeachIterator memories = DateTimeMessageContainer.getMessages().toValue(ArrayMemory.class)
                .foreachIterator(false, false);

        while (memories.next()) {
            array.refOfIndex(memories.getMemoryKey()).assign(memories.getValue());
        }

        array.refOfIndex("is_localtime").assign(Memory.FALSE);

        // the parsed string has a relative part
        if (!relativeContributors.isEmpty()) {
            ArrayMemory relative = ArrayMemory.createHashed(RELATIVE_CONTRIBUTOR_NAMES.length);
            array.refOfIndex("relative").assign(relative);

            Arrays.stream(RELATIVE_CONTRIBUTOR_NAMES)
                    .forEach(name -> {
                        Long value = relativeContributors.getOrDefault(name, 0L);
                        relative.refOfIndex(name).assign(value);
                    });

            Arrays.stream(RELATIVE_BOOL_CONTRIBUTOR_NAMES)
                    .filter(relativeContributors::containsKey)
                    .forEach(name -> relative.refOfIndex(name).assign(Memory.TRUE));
        }

        return array;
    }

    private Memory getFieldMemory(TemporalField field) {
        return modified.contains(field) ? LongMemory.valueOf(dateTime.get(field)) : Memory.FALSE;
    }
}
