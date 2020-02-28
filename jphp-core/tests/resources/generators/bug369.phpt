--TEST--
yield can be used without a value
--FILE--
<?php

$a = function() { var_dump((yield)['a']); };
$a()->send(['a' => 'pony']);

function a() {
    yield 77;
    return ['a' => 'pony'];
}
$a = function() { var_dump((yield from a())['a']); };

foreach ($a() as $v) {
    var_dump($v);
}

?>
--EXPECT--
string(4) "pony"
int(77)
string(4) "pony"