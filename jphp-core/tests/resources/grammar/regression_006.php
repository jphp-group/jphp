--TEST--
Test to ensure const list syntax declaration works
--FILE--
<?php

class Obj
{
    const DECLARE = 'declare',
          RETURN = 'return',
          FUNCTION = 'function',
          USE = 'use';
}

echo Obj::DECLARE, "\n";
echo Obj::RETURN, "\n";
echo Obj::FUNCTION, "\n";
echo Obj::USE, "\n";
echo Obj::

    USE, "\n";
echo "\nDone\n";

?>
--EXPECTF--
declare
return
function
use
use

Done