--TEST--
Check type hint compatibility in overrides with array hints.
--FILE--
<?php
error_reporting(E_ALL);
Class C { function f(array $a) {} }

echo "Compatible hint.\n";
Class D1 extends C { function f(array $a) {} }

echo "Class hint, should be array.\n";
Class D2 extends C { function f(SomeClass $a) {} }
?>
==DONE==
--EXPECTF--
Warning: Declaration of D2::f(SomeClass $a) should be compatible with C::f(array $a) in %s on line 9 at pos %d
Compatible hint.
Class hint, should be array.
==DONE==
