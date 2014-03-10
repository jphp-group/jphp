--TEST--
Environement current
--FILE--
<?php
use php\lang\Environment;

$global = 'foobar';

$env = Environment::current();
$env->execute(function(){
    ob_implicit_flush(true);

    global $global;
    var_dump($global);
});

$env = new Environment();
$env->execute(function(){
    ob_implicit_flush(true);

    global $global;
    var_dump($global);
})

?>
--EXPECT--
string(6) "foobar"
NULL