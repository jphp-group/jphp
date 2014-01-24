--TEST--
Reflection Class -> isIterable ?
--FILE--
<?php
class Bar { }
interface A extends Iterator { }
class myIterator implements Iterator {
    function rewind() {
    }

    function current() {
    }

    function key() {
    }

    function next() {
    }

    function valid() {
    }
}

var_dump(new ReflectionClass('myIterator')->isIterable());
var_dump(new ReflectionClass('A')->isIterable());
var_dump(new ReflectionClass('Bar')->isIterable());

--EXPECT--
bool(true)
bool(true)
bool(false)
