package org.develnext.jphp.zend.ext.standard.date;

abstract class Node {
    abstract boolean matches(DateTimeParserContext ctx);

    abstract void apply(DateTimeParserContext ctx);

}
