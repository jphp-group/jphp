package org.develnext.jphp.ext.mail.bind;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

import javax.mail.internet.InternetAddress;

public class InternetAddressMemoryOperation extends MemoryOperation<InternetAddress> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { InternetAddress.class };
    }

    @Override
    public InternetAddress convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return new InternetAddress(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, InternetAddress arg) throws Throwable {
        return StringMemory.valueOf(arg.toUnicodeString());
    }
}
