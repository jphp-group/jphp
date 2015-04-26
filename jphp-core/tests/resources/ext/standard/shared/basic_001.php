--TEST--
Shared basic test
--FILE--
<?php

use php\lang\Environment;
use php\util\Shared;

Shared::resetAll();

ob_implicit_flush(1);

$value = Shared::value('foobar');

var_dump($value->isEmpty());
var_dump($value->get());

$env1 = new Environment();
$env2 = new Environment();

$value->set('foobar');

$env1->execute(function () {
    var_dump(Shared::value('foobar')->get());
});

$env2->execute(function () {
    var_dump(Shared::value('foobar')->get());
});

?>
--EXPECT--
bool(true)
NULL
string(6) "foobar"
string(6) "foobar"