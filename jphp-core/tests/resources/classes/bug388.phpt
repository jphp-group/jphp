--TEST--
Cannot use the `class` magic constant in traits
--FILE--
<?php
trait a
{
    public static function test() { var_dump(self::class); }
}
a::test();
?>
--EXPECT--
string(1) "a"