package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;

abstract class Node {
    abstract boolean matches(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer);
}
