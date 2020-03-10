--TEST--
Allow making parameters nullable
--FILE--
<?php
interface a
{
    public function test($a);
}

class b implements a
{
    public function test($a = null) {}
}

var_dump(new b);
?>
--EXPECTF--
object(b)#%d (0) {
}