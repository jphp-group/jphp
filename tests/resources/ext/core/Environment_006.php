--TEST--
Environement import
--FILE--
<?php
ob_implicit_flush(true);

use php\lang\Environment;

class Foo { }
function bar() { }

$env = new Environment();
$env->execute(function(){
    ob_implicit_flush(true);

    var_dump('class Foo exits: ', class_exists('Foo'));
    var_dump('function bar exits: ', function_exists('bar'));
});

echo "\nImport ...", "\n";

$env->importClass('Foo');
$env->importFunction('bar');

$env->execute(function(){
    ob_implicit_flush(true);

    var_dump('class Foo exits: ', class_exists('Foo'));
    var_dump('function bar exits: ', function_exists('bar'));
});
?>
--EXPECT--
string(17) "class Foo exits: "
bool(false)
string(20) "function bar exits: "
bool(false)

Import ...
string(17) "class Foo exits: "
bool(true)
string(20) "function bar exits: "
bool(true)