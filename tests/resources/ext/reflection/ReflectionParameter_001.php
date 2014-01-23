--TEST--
Reflection Parameter -> for internal
--FILE--
<?php

try {
    $r = new ReflectionParameter('cos', 0);
} catch (ReflectionException $e){
    echo "Done: " . $e->getMessage();
}

--EXPECTF--
Done: cos(): ReflectionParameter does not support internal functions
