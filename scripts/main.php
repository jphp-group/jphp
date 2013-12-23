<?php

function test($className){
    include strtolower($className) . ".php";
}
var_dump(spl_autoload_register('test'));

Test::call();
