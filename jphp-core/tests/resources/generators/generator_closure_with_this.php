--TEST--
Non-static closures can be generators
--FILE--
<?php
class Test {
    public function getGenFactory() {
        return function() {
            yield $this;
        };
    }
}
$genFactory = (new Test)->getGenFactory();
var_dump($genFactory()->current());
?>
--EXPECTF--
object(Test)#%d (0) {
}