--TEST--
Test possible function naming regression on procedural scope
--FILE--
<?php

class Obj
{
    function echo(){} // valid
function return(){} // valid
}

function echo(){} // not valid

?>
--EXPECTF--

Parse error: Syntax error, unexpected 'echo' in %s on line 9, position 10