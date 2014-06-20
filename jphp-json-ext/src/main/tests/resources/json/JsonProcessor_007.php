--TEST--
JsonProcessor test #7: Serialize
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
$json->onSerialize('int', function($val){
    return "int:" . $val;
});

$json->onSerialize('float', function($val){
    return "float:" . $val;
});

$json->onSerialize('bool', function($val){
    return "bool:" . (int)$val;
});

$json->onSerialize('null', function($val){
    return "empty:" . $val;
});

$json->onSerialize('string', function($val){
    return "str:'" . $val . "'";
});

var_dump($json->format(1));
var_dump($json->format(1.1));
var_dump($json->format(true));
var_dump($json->format(false));
var_dump($json->format(null));
var_dump($json->format('foobar'));

?>
--EXPECTF--
string(7) ""int:1""
string(11) ""float:1.1""
string(8) ""bool:1""
string(8) ""bool:0""
string(8) ""empty:""
string(14) ""str:'foobar'""
