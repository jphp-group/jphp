--TEST--
Modification of typed property during assignment must not leak
--FILE--
<?php

class A {
    public string $prop = "";
}

class B {
    public function __toString() {
        global $a;
        $a->prop = "dont ";
        $a->prop .= "leak ";
        $a->prop .= "me!";
        return "test";
    }
}

$a = new A;
$a->prop = new B;
var_dump($a);

?>
--EXPECTF--
object(A)#%d (1) {
  ["prop"]=>
  string(4) "test"
}