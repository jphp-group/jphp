--TEST--
Environement sendMessage
--FILE--
<?php
use php\lang\Environment;

$env = new Environment();
try {
    $env->sendMessage('one', 'two', 'three');
} catch (Exception $e) {
    var_dump($e->getMessage());
}

$env->onMessage(function($arg1, $arg2, $arg3){
    ob_implicit_flush(true);

    var_dump($arg1, $arg2, $arg3);
});
$env->sendMessage('one', 'two', 'three');

?>
--EXPECTF--
string(3) "one"
string(3) "two"
string(5) "three"
string(63) "Environment cannot receive messages, onMessage callback is NULL"