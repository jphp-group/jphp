--TEST--
array unpacking with undefinded variable
--FILE--
<?php

var_dump([...$arr]);

--EXPECTF--

Fatal error: Only arrays and Traversables can be unpacked in %s on line 3, position 11