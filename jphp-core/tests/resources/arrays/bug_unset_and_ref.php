--FILE--
<?php

function arr($array){
    var_dump($array);
    isArray($array);
    var_dump($array);
}

function isArray($array){
    $array = is_array($array);
    // $array = isset($array[0]); // то же самое и с isset
}

arr($a = ['abc' => 'def', 123 => 456]);
?>
--EXPECT--
array(2) {
  ["abc"]=>
  string(3) "def"
  [123]=>
  int(456)
}
array(2) {
  ["abc"]=>
  string(3) "def"
  [123]=>
  int(456)
}