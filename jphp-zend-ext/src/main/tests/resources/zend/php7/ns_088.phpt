--TEST--
Nested group use statements syntax error
--FILE--
<?php
namespace Fiz\Biz\Buz {
    use Foo\Bar\Baz\{
        A,
        B {
        C
            D,
            E
        }
    };
}
?>
--EXPECTF--

Parse error: Syntax error, unexpected '{' in %s on line 5, position 11