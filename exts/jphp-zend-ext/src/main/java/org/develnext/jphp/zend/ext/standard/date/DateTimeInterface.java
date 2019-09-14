package org.develnext.jphp.zend.ext.standard.date;

import org.develnext.jphp.zend.ext.standard.DateConstants;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Final;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Optional;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;

@Name("DateTimeInterface")
@Final
@SuppressWarnings("unused")
public interface DateTimeInterface extends IObject {
    Memory ATOM = DateConstants.DATE_ATOM;
    Memory COOKIE = DateConstants.DATE_COOKIE;
    Memory ISO8601 = DateConstants.DATE_ISO8601;
    Memory RFC822 = DateConstants.DATE_RFC822;
    Memory RFC850 = DateConstants.DATE_RFC850;
    Memory RFC1036 = DateConstants.DATE_RFC1036;
    Memory RFC1123 = DateConstants.DATE_RFC1123;
    Memory RFC2822 = DateConstants.DATE_RFC2822;
    Memory RFC3339 = DateConstants.DATE_RFC3339;
    Memory RFC3339_EXTENDED = DateConstants.DATE_RFC3339_EXTENDED;
    Memory RFC7231 = DateConstants.DATE_RFC7231;
    Memory RSS = DateConstants.DATE_RSS;
    Memory W3C = DateConstants.DATE_W3C;

    @Signature(value = {
            @Arg(value = "datetime2", type = HintType.OBJECT, typeClass = "DateTimeInterface"),
            @Arg(value = "absolute", type = HintType.BOOLEAN, optional = @Optional("FALSE"))
    }, result = @Arg(type = HintType.OBJECT, typeClass = "DateInterval"))
    Memory diff(Environment env, TraceInfo traceInfo, Memory dateTimeInterface);

    @Signature(value = {@Arg(value = "format", type = HintType.STRING)}, result = @Arg(type = HintType.STRING))
    Memory format(Environment env, TraceInfo traceInfo, String format);

    @Signature(result = @Arg(type = HintType.INT))
    Memory getOffset(Environment env, TraceInfo traceInfo);

    @Signature(result = @Arg(type = HintType.INT))
    Memory getTimestamp(Environment env, TraceInfo traceInfo);

    @Signature(result = @Arg(type = HintType.OBJECT))
    Memory getTimezone(Environment env, TraceInfo traceInfo);

    @Signature(result = @Arg(type = HintType.VOID))
    Memory __wakeup(Environment env, TraceInfo traceInfo);
}
