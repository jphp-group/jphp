--TEST--
Reflection Parameter -> for internal
--FILE--
<?php

try {
    $r = new ReflectionFunction('cos');
    $r->getParameters();
} catch (ReflectionException $e){
    echo "Done: " . $e->getMessage();
}

--EXPECTF--
Done: Cannot get parameters for internal function cos()
