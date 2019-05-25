package org.develnext.jphp.zend.ext.standard.date;

import java.time.ZoneId;

import org.junit.Test;

public class ZoneIdFactoryTest {
    @Test
    public void listIdentifiers() {
        ZoneId of = ZoneIdFactory.of("Africa/Dar_es_Salaam");
    }
}