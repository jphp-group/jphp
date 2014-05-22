--TEST--
Bug #138
--FILE--
<?php

class Foo {

    var $bla = 'fail';

    function test() {
        //against dead code optimization (=, call func, return and etc.)
        $var = function(){
            if(true) { //or other code blocks (if, switch-case, while, for)
                //against dead code optimization
                $var = function(){
                    $this->bla = 'fooo';
                };
                $var();
            }
        };
        $var();
    }
}

$foo = new Foo();
$foo->test();
var_dump($foo->bla);
?>
--EXPECT--
string(4) "fooo"
