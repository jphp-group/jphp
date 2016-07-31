--TEST--
php\lib\str Test - join & split
--FILE--
<?php

use php\lib\Str as str;

echo "--test-split\n";
echo "split('f,o,o,b,a,r', ',') == "; var_dump(str::split('f,o,o,b,a,r', ','));
echo "split('f,o,o,b,a,r', ',', 3) == "; var_dump(str::split('f,o,o,b,a,r', ',', 3));
echo "split('foobar', ',', 3) == "; var_dump(str::split('foobar', ',', 3));
echo "split('foo,,bar', ',') == "; var_dump(str::split('foo,,bar', ','));
echo "split('foo,,bar', ',,') == "; var_dump(str::split('foo,,bar', ',,'));

echo "--test-join\n";
echo "join([foo,bar], ',') == " . str::join(['foo', 'bar'], ','), "\n";
echo "join([foo,bar], ',,') == " . str::join(['foo', 'bar'], ',,'), "\n";

?>
--EXPECT--
--test-split
split('f,o,o,b,a,r', ',') == array(6) {
  [0]=>
  string(1) "f"
  [1]=>
  string(1) "o"
  [2]=>
  string(1) "o"
  [3]=>
  string(1) "b"
  [4]=>
  string(1) "a"
  [5]=>
  string(1) "r"
}
split('f,o,o,b,a,r', ',', 3) == array(3) {
  [0]=>
  string(1) "f"
  [1]=>
  string(1) "o"
  [2]=>
  string(7) "o,b,a,r"
}
split('foobar', ',', 3) == array(1) {
  [0]=>
  string(6) "foobar"
}
split('foo,,bar', ',') == array(3) {
  [0]=>
  string(3) "foo"
  [1]=>
  string(0) ""
  [2]=>
  string(3) "bar"
}
split('foo,,bar', ',,') == array(2) {
  [0]=>
  string(3) "foo"
  [1]=>
  string(3) "bar"
}
--test-join
join([foo,bar], ',') == foo,bar
join([foo,bar], ',,') == foo,,bar
