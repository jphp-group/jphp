--TEST--
Introducing new private variables of the same name in a subclass is ok, and does not lead to any output. That is consitent with normal inheritance handling.
--FILE--
<?php

class Base {
    private $hello;
}

trait THello1 {
    private $hello;
}

class SameNameInSubClassNoNotice extends Base {
    use THello1;
}

class Notice extends Base {
    use THello1;
    private $hello;
}

echo "ok";
?>
--EXPECTF--
ok