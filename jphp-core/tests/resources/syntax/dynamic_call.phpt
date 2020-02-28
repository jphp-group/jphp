--TEST--
Test dynamic call syntax
--FILE--
<?php
function test($x) {
    var_dump($x);
}

('test')("foo");

$v = 'st';
("te$v")("bar");

(fn() => var_dump(222))();
(function() { var_dump(333); })();
([fn() => var_dump(444)][0])();

$b = function ($a) { var_dump($a); };
($a ?? $b)('magic');

$b = function ($a) { var_dump($a); };
($a ?? $b)('magic', 'param');

(function () { echo 'a'; })();
?>
--EXPECT--
string(3) "foo"
string(3) "bar"
int(222)
int(333)
int(444)
string(5) "magic"
string(5) "magic"
a