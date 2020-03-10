--TEST--
Crash when using abstract class extending abstract class via interface
--FILE--
<?php
interface a
{
    public function test();
}

abstract class b implements a
{
}

abstract class c extends b
{
}

class d extends c
{
    public function test() { var_dump("ok"); }
}

(new d)->test();
?>
--EXPECT--
string(2) "ok"