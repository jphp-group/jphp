--FILE--
<?php
$o1 = new stdClass();
$o1->prop1 = 'c';
$o1->prop2 = 25;
$o1->prop3 = 201;
$o1->prop4 = 1000;

$o2 = new stdClass();
$o2->prop1 = 'c';
$o2->prop2 = 25;
$o2->prop3 = 200;
$o2->prop4 = 9999;

echo (int)($o1 < $o2) . "\n"; // 0
echo (int)($o1 > $o2) . "\n"; // 1

$o1->prop3 = 200;

echo (int)($o1 < $o2) . "\n"; // 1
echo (int)($o1 > $o2) . "\n"; // 0

$std = new stdClass();
$std->x = $std;

var_dump($std == $std);

--EXPECT--
0
1
1
0
bool(true)