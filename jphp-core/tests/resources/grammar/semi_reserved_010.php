--TEST--
Edge case: T_STRING<insteadof> insteadof T_STRING<?>
--FILE--
<?php

trait TraitA
{
    public static function insteadof(){ echo __METHOD__, "\n"; }
}

trait TraitB
{
    public static function insteadof(){ echo __METHOD__, "\n"; }
}

class Foo
{
    use TraitA , TraitB {
        TraitB::insteadof
            insteadof TraitA;
    }
}

Foo::insteadof();

echo "\n", "Done", "\n";

?>
--EXPECTF--
TraitB::insteadof

Done
