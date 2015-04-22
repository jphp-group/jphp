--TEST--
Test autoload duplicate classes.
--FILE--
<?php


function myAutoload($class) {
    include __DIR__ . '/autoload_duplicate_classes.inc.php';
}

function myAutoload2($class) {
    include __DIR__ . '/autoload_duplicate_classes.inc.php';
}

spl_autoload_register('myAutoload');
spl_autoload_register('myAutoload2');

new Foobar();

?>
--EXPECTF--
