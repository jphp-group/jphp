<?php

function test($className){
    include __DIR__ . '/' . strtolower($className) . ".php";
}
var_dump(spl_autoload_register('test'));

Test::call();
