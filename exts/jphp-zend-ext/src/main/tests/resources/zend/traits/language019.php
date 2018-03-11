--TEST--
final alias
--FILE--
<?php
trait T1 {
    function foo() {}
}
class C1 {
    use T1 {
        T1::foo as final;
    }
}
?>
--EXPECTF--

Parse error: Syntax error, unexpected 'final' in %s on line %d, position %d