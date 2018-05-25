--TEST--
Bug #260
--FILE--
<?php

class a
{
    public $data = [];
}

$a = new a;
unset($a->data); // после unset $a->data будет null и не станет массивом, пока явно не объявишь $a->data = [];
$a->data[] = 1;

var_dump($a->data); // null ???
?>
--EXPECT--
array(1) {
  [0]=>
  int(1)
}
