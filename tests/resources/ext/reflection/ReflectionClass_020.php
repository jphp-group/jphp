--TEST--
Reflection Class -> isSubclassOf ?
--FILE--
<?php
interface A { }
class Foo { }
class Bar extends Foo implements A { }

$r = new ReflectionClass('Bar');

echo "Bar isSubclassOf Bar: " . ($r->isSubclassOf("Bar") ? "true": "false"), "\n";
echo "Bar isSubclassOf A: " . ($r->isSubclassOf("A") ? "true": "false"), "\n";
echo "Bar isSubclassOf Foo: " . ($r->isSubclassOf("Foo") ? "true": "false"), "\n";

--EXPECT--
Bar isSubclassOf Bar: false
Bar isSubclassOf A: true
Bar isSubclassOf Foo: true
