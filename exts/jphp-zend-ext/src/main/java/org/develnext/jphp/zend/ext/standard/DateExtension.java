package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ext.standard.date.DateInterval;
import org.develnext.jphp.zend.ext.standard.date.DatePeriod;
import org.develnext.jphp.zend.ext.standard.date.DateTime;
import org.develnext.jphp.zend.ext.standard.date.DateTimeImmutable;
import org.develnext.jphp.zend.ext.standard.date.DateTimeInterface;
import org.develnext.jphp.zend.ext.standard.date.DateTimeZone;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class DateExtension extends Extension {
    @Override
    public String getName() {
        return "date";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public Status getStatus() {
        return Status.ZEND_LEGACY;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new DateFunctions());
        registerConstants(new DateConstants());

        registerClass(scope, DateTimeInterface.class);
        registerClass(scope, DateTime.class);
        registerClass(scope, DateTimeImmutable.class);
        registerClass(scope, DateInterval.class);
        registerClass(scope, DateTimeZone.class);
        registerClass(scope, DatePeriod.class);
    }
}
