--TEST--
Test Debug Info Magic Method
--FILE--
<?php

class Foo {
    private $args = [1, 2, 3];

    public function __debugInfo() {
        return $this->args;
    }
}

var_dump(new Foo());
print_r(new Foo());

?>
--EXPECTF--
object(Foo)#%d (3) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
}
Foo Object
(
    [0] => 1
    [1] => 2
    [2] => 3
)