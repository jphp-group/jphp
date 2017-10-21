--TEST--
Only public and protected class constants should be inherited
--FILE--
<?php
class A {
    public const X = 1;
    protected const Y = 2;
    private const Z = 3;
}
class B extends A {
    static public function checkConstants() {
        var_dump(self::X);
        var_dump(self::Y);
        var_dump(self::Z);
    }
}
B::checkConstants();
?>
--EXPECTF--
int(1)
int(2)

Fatal error: Undefined class constant 'Z' in %s on line 11, position %d