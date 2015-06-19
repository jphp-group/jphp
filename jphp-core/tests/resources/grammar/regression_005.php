--TEST--
Test possible constant naming regression on procedural scope
--FILE--
<?php

class Obj
{
    const return = 'yep';
}

const return = 'nope';

?>
--EXPECTF--

Parse error: Syntax error, unexpected 'return', expecting 'T_STRING' in %s on line 8, position 7