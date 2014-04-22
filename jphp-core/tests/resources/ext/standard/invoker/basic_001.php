--TEST--
Invoker basic test
--FILE--
<?php

use php\lang\Invoker;

$invoker = new Invoker('var_dump');

echo "--call()\n";
$invoker->call('foo', 'bar');

echo "--__invoke()\n";
$invoker('foo', 'bar');

echo "--callArray()\n";
$invoker->callArray(['foo', 'bar']);

?>
--EXPECT--
--call()
string(3) "foo"
string(3) "bar"
--__invoke()
string(3) "foo"
string(3) "bar"
--callArray()
string(3) "foo"
string(3) "bar"