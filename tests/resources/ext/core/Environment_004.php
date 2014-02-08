--TEST--
Environement defineConstant
--FILE--
<?php
use php\lang\Environment;

class X {
    static $foo = FOOBAR;
}

define('FOOBAR', 'fail');

$env = new Environment();
$env->execute(function(){
    var_dump(FOOBAR);
});

$env->defineConstant('FOOBAR', 'success');
$env->importClass('X');
$env->execute(function(){
    var_dump(FOOBAR);
    var_dump(X::$foo);
});


?>
--EXPECTF--
string(6) "FOOBAR"
string(7) "success"
string(7) "success"
Notice: Use of undefined constant FOOBAR - assumed 'FOOBAR' in %s on line %d at pos %d