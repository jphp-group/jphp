<?php

use php\lib\mirror;
use php\lib\str;

$t = microtime(1);
$std = 'stdClass';

for ($i = 0; $i < 10000000; $i++) {
    $obj = new
    $obj = mirror::newInstance('stdClass');
    //$class = mirror::typeOf($std, true);
    //$class = str::lower(get_class($std));
}

var_dump(microtime(1) - $t);