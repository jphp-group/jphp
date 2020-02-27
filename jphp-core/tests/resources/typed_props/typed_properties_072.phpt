--TEST--
Typed property must cast when used with __get()
--FILE--
<?php

class Test {
    public int $val;

    public function __get($name) {
        return "42";
    }
}

$test = new Test;
unset($test->val);
var_dump($test);
var_dump($test->val);

?>
--EXPECTF--
object(Test)#%d (0) {
  ["val"]=>
  uninitialized(int)
}
int(42)
