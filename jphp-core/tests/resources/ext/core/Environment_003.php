--TEST--
Environement parent
--FILE--
<?php
use php\lang\Environment;

class X {
    static $foo = 'bar';
}

function test() {
    return 'foo';
}

$env = new Environment(Environment::current());
$env->execute(function(){
    ob_implicit_flush(true);

    var_dump(test());
    var_dump(X::$foo);
});

$env = new Environment(null);
$env->execute(function(){
    ob_implicit_flush(true);

    echo "class 'X' exists - " . (class_exists('X') ? 'true' : 'false'), "\n";
    echo "function 'test' exists - " . (function_exists('test') ? 'true' : 'false'), "\n";
});

?>
--EXPECT--
string(3) "foo"
string(3) "bar"
class 'X' exists - false
function 'test' exists - false