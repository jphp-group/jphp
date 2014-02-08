--TEST--
Environement test
--FILE--
<?php
use php\lang\Environment;

$env = new Environment();
var_dump($env->execute(function(){
    var_dump("success1");
    return 'success2';
}));

?>
--EXPECT--
string(8) "success1"
string(8) "success2"