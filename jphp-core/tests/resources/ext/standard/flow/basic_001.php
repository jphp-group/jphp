--TEST--
Basic cursor test
--FILE--
<?php

use php\util\Flow;

echo "--test array\n";
$cursor = Flow::of([1,2,3]);
foreach($cursor as $el) {
    var_dump($el);
}

echo "--test range\n";
$cursor = Flow::ofRange(5, 7);
foreach($cursor as $el) {
    var_dump($el);
}

echo "--test range with step\n";
$cursor = Flow::ofRange(5, 10, 2);
foreach($cursor as $el) {
    var_dump($el);
}

echo "--test string\n";
$cursor = Flow::ofString('foo');
foreach($cursor as $el) {
    var_dump($el);
}

echo "--test string with chunk size\n";
$cursor = Flow::ofString('foobar', 2);
foreach($cursor as $el) {
    var_dump($el);
}
?>
--EXPECT--
--test array
int(1)
int(2)
int(3)
--test range
int(5)
int(6)
int(7)
--test range with step
int(5)
int(7)
int(9)
--test string
string(1) "f"
string(1) "o"
string(1) "o"
--test string with chunk size
string(2) "fo"
string(2) "ob"
string(2) "ar"
