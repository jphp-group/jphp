--TEST--
Edge case: T_STRING<as> as T_STRING<?>
--FILE--
<?php

trait TraitA
{
    public static function as(){ echo __METHOD__, "\n"; }
}

class Foo
{
    use TraitA  {
        as as try;
    }
}

Foo::try();

echo "\n", "Done", "\n";

?>
--EXPECTF--
TraitA::as

Done