--TEST--
Check type hint compatibility in overrides with array hints.
--FILE--
<?php
Class C { function f(array $a) {} }

echo "No hint, should be array.\n";
Class D extends C { function f($a) {} }
?>
==DONE==
--EXPECTF--
Strict Standards: Declaration of D::f() must be compatible with C::f(array $a) in %s on line 5 at pos %d
No hint, should be array.
==DONE==
