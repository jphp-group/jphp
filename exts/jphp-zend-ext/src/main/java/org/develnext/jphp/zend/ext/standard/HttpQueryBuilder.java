package org.develnext.jphp.zend.ext.standard;

import static org.develnext.jphp.zend.ext.standard.StringConstants.PHP_QUERY_RFC3986;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Function;

import php.runtime.Memory;
import php.runtime.common.Modifier;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.StdClass;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.PropertyEntity;

final class HttpQueryBuilder {
    /**
     * The {@code ArrayDeque} has the default initial capacity of {@literal 16}, for this case we dont expect such level
     * of nesting. This is the minimal initial capacity accepted by {@code ArrayDeque}.
     */
    private static final int MIN_DEQUE_CAPACITY = 8;
    /**
     * The string to append before numeric values.
     */
    private final String numericPrefix;
    /**
     * Whether we should append {@code numericPrefix} when occurring the numeric value. This is the precomputed value.
     */
    private final boolean hasNumericPrefix;
    /**
     * The query parts separator.
     */
    private final String argSeparator;

    /**
     * The URL encoder function.
     */
    private final Function<Memory, String> encoder;

    /**
     * We should keep trace when traversing arrays.
     */
    private Deque<Memory> nesting;

    private HttpQueryBuilder(String numericPrefix, String argSeparator, int encType) {
        this.numericPrefix = numericPrefix;
        this.hasNumericPrefix = !StringUtils.isEmpty(numericPrefix);
        this.argSeparator = argSeparator;
        Function<String, String> realEncoder = encType == PHP_QUERY_RFC3986 ? StringFunctions::rawurlencode : StringFunctions::urlencode;
        this.encoder = memory -> {
            if (memory.isNumber()) {
                return memory.toString();
            }

            return realEncoder.apply(memory.toString());
        };
    }

    public static String buildFrom(Environment env, Memory queryData, String numericPrefix, String argSeparator, int encType) {
        if (StringUtils.isEmpty(argSeparator)) {
            argSeparator = env.getConfigValue("arg_separator.output", StringMemory.valueOf('&')).toString();
        }

        ArrayMemory array = toArray(env, queryData);
        // Fail fast
        if (array == null) {
            return "";
        }

        return new HttpQueryBuilder(numericPrefix, argSeparator, encType)
                .build(queryData.getNewIterator(env));
    }

    private static ArrayMemory toArray(Environment env, Memory queryData) {
        ArrayMemory array = null;

        if (queryData.isArray()) {
            array = queryData.toValue(ArrayMemory.class);
        } else if (queryData.isObject()) {
            ObjectMemory queryDataObj = queryData.toValue(ObjectMemory.class);
            if (queryDataObj.value instanceof StdClass) {
                array = queryDataObj.toArray().toValue(ArrayMemory.class);
            } else {
                Collection<PropertyEntity> publicProperties = queryDataObj.getReflection().getProperties(Modifier.PUBLIC);
                if (!publicProperties.isEmpty()) {
                    array = ArrayMemory.createHashed(publicProperties.size());

                    for (PropertyEntity prop : publicProperties) {
                        try {
                            Memory value = prop.getValue(env, TraceInfo.UNKNOWN, queryDataObj.value);
                            array.putAsKeyString(prop.getSpecificName(), value);
                        } catch (Throwable th) {
                            throw new RuntimeException(th);
                        }
                    }
                }
            }
        } else {
            // TODO: warning or notice
            throw new RuntimeException();
        }

        return array;
    }

    private static boolean isNumeric(Memory k) {
        if (k.isNumber()) {
            return true;
        }

        if (k.isString()) {
            try {
                Double.parseDouble(k.toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    private String build(ForeachIterator it) {
        StringBuilder buff = new StringBuilder();
        return build(buff, it).deleteCharAt(buff.length() - 1).toString();
    }

    private StringBuilder build(StringBuilder buff, ForeachIterator it) {
        final Deque<Memory> nesting = this.nesting;
        final boolean hasNesting = nesting != null && !nesting.isEmpty();
        boolean pollNested = false;

        while (it.next()) {
            Memory k = it.getMemoryKey();
            Memory v = it.getValue();

            if (v.isArray()) {
                lazyInitNesting().add(k);

                build(buff, v.toValue(ArrayMemory.class).foreachIterator(false, false));
                continue;
            }

            if (hasNumericPrefix) {
                // nesting.peek() can't throw NPE, due guard variable hasNesting.
                //noinspection ConstantConditions
                if ((hasNesting && isNumeric(nesting.peek())) || (!hasNesting && isNumeric(k))) {
                    buff.append(numericPrefix);
                }
            }

            if (hasNesting) {
                // setting this flag to poll it after loop
                pollNested = true;
                // add current key
                nesting.offer(k);

                Iterator<Memory> nestedIt = nesting.iterator();
                buff.append(nestedIt.next());

                while (nestedIt.hasNext()) {
                    appendArrayKey(buff, nestedIt.next());
                }

                // remove current key
                nesting.pollLast();
            } else {
                buff.append(k.toString());
            }

            buff.append('=').append(v.isBoolean() ? v.toInteger() : encoder.apply(v)).append(argSeparator);
        }

        if (pollNested) {
            nesting.pollLast();
        }

        return buff;
    }

    private Deque<Memory> lazyInitNesting() {
        if (nesting == null)
            return (nesting = new ArrayDeque<>(MIN_DEQUE_CAPACITY));

        return nesting;
    }

    private void appendArrayKey(StringBuilder buff, Memory k) {
        buff.append("%5B") // [
                .append(encoder.apply(k))
                .append("%5D"); // ]
    }
}
