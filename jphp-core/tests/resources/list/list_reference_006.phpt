--TEST--
"Reference Unpacking - Class ArrayAccess No Reference" list()
--FILE--
<?php
class StorageNoRef implements ArrayAccess {
    private $s = [];
    function __construct(array $a) { $this->s = $a; }
    function offsetSet ($k, $v) { $this->s[$k] = $v; }
    function offsetGet ($k) { return $this->s[$k]; }
    function offsetExists ($k) { return isset($this->s[$k]); }
    function offsetUnset ($k) { unset($this->s[$k]); }
}

$a = new StorageNoRef([1, 2]);
list(&$one, $two) = $a;
var_dump($a);

$a = new StorageNoRef([1, 2]);
list(,,list($var)) = $a;
var_dump($a);

$a = new StorageNoRef(['one' => 1, 'two' => 2]);
['one' => &$one, 'two' => $two] = $a;
var_dump($a);
?>
--EXPECTF--
object(StorageNoRef)#%d (1) {
  ["s":"StorageNoRef":private]=>
  array(2) {
    [0]=>
    int(1)
    [1]=>
    int(2)
  }
}
object(StorageNoRef)#%d (1) {
  ["s":"StorageNoRef":private]=>
  array(2) {
    [0]=>
    int(1)
    [1]=>
    int(2)
  }
}
object(StorageNoRef)#%d (1) {
  ["s":"StorageNoRef":private]=>
  array(2) {
    ["one"]=>
    int(1)
    ["two"]=>
    int(2)
  }
}