--TEST--
Reflection Class -> is ?
--FILE--
<?php

interface IA { }
abstract class A { }
final class B {}

$r = new ReflectionClass('IA');
echo "IA is interface: " . ($r->isInterface() ? "true" : "false"), "\n";
echo "IA is abstract: " . ($r->isAbstract() ? "true" : "false"), "\n";
echo "IA is internal: " . ($r->isInternal() ? "true" : "false"), "\n";
echo "IA is user defined: " . ($r->isUserDefined() ? "true" : "false"), "\n";

echo "\n";

$r = new ReflectionClass('A');
echo "A is interface: " . ($r->isInterface() ? "true" : "false"), "\n";
echo "A is abstract: " . ($r->isAbstract() ? "true" : "false"), "\n";
echo "A is internal: " . ($r->isInternal() ? "true" : "false"), "\n";
echo "A is user defined: " . ($r->isUserDefined() ? "true" : "false"), "\n";
echo "A is final: " . ($r->isFinal() ? "true" : "false"), "\n";

echo "\n";

$r = new ReflectionClass('stdClass');
echo "stdClass is internal: " . ($r->isInternal() ? "true" : "false"), "\n";
echo "stdClass is user defined: " . ($r->isUserDefined() ? "true" : "false"), "\n";
echo "stdClass is final: " . ($r->isFinal() ? "true" : "false"), "\n";

echo "\n";

$r = new ReflectionClass('B');
echo "B is final: " . ($r->isFinal() ? "true" : "false"), "\n";

--EXPECT--
IA is interface: true
IA is abstract: false
IA is internal: false
IA is user defined: true

A is interface: false
A is abstract: true
A is internal: false
A is user defined: true
A is final: false

stdClass is internal: true
stdClass is user defined: false
stdClass is final: false

B is final: true
