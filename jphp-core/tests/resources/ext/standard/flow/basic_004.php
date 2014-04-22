--TEST--
Basic cursor test eachSlice
--FILE--
<?php

use php\util\Flow;

$cursor = Flow::of(['a' => 10, 'b' => 20, 'c' => 30, 'd' => 40, 'e' => 50]);
$cursor->eachSlice(2, function($array){
    var_dump($array);
});

echo "--with keys\n";

$cursor = Flow::of(['a' => 10, 'b' => 20, 'c' => 30, 'd' => 40, 'e' => 50]);
$cursor->withKeys()->eachSlice(2, function($array){
    var_dump($array);
});

?>
--EXPECT--
array(2) {
  [0]=>
  int(10)
  [1]=>
  int(20)
}
array(2) {
  [0]=>
  int(30)
  [1]=>
  int(40)
}
array(1) {
  [0]=>
  int(50)
}
--with keys
array(2) {
  ["a"]=>
  int(10)
  ["b"]=>
  int(20)
}
array(2) {
  ["c"]=>
  int(30)
  ["d"]=>
  int(40)
}
array(1) {
  ["e"]=>
  int(50)
}
