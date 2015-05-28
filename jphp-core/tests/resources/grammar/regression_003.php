--TEST--
Test to ensure ::class is still reserved in obj scope
--FILE--
<?php

class Obj
{
    const CLASS = 'class';
}

?>
--EXPECTF--

Parse error: Syntax error, unexpected 'CLASS', expecting 'T_STRING' in %s on line 5, position 11