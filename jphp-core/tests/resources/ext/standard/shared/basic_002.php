--TEST--
Shared creator test
--FILE--
<?php

use php\lang\Environment;
use php\util\Shared;

ob_implicit_flush(1);

Shared::resetAll();

$env1 = new Environment();
$env2 = new Environment();

function foobar ($value) {
    return Shared::value('foobar', function () use ($value) {
        return $value;
    });
}

$env1->importFunction('foobar');
$env2->importFunction('foobar');

$env1->execute(function () {
    ob_implicit_flush(1);
    var_dump(foobar('x')->get());
});

$env2->execute(function () {
    ob_implicit_flush(1);
    var_dump(foobar('y')->get());
});

var_dump(Shared::reset('foobar'));

$env2->execute(function () {
    ob_implicit_flush(1);
    var_dump(foobar('y')->get());
});

?>
--EXPECTF--
string(1) "x"
string(1) "x"
object(php\util\SharedValue)#%d (1) {
  ["*value"]=>
  string(1) "x"
}
string(1) "y"