--TEST--
Type group use declarations should not allow override on inner itens
--FILE--
<?php

// should throw syntax errors

use const Foo\Bar\{
    A,
    const B,
    function C
};

--EXPECTF--

Parse error: Syntax error, unexpected 'const' in %s on line 7, position 5