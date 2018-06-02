--TEST--
Bug #264: Countable: Cannot access private property
--FILE--
<?php
class Test implements \Countable{
    private $items = [1, 2,];

    public function count(){
        return count($this->items);
    }
}
class SubOfTest extends Test{

}
$xd = new SubOfTest;
var_dump(count($xd));
?>
--EXPECTF--
int(2)
