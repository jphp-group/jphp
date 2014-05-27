--TEST--
Bug #132
--FILE--
<?php

class null {
    const NULL = "null";
}

class false {
    const false = "false";
}

class true {
    const true = "true";
}

var_dump(null::NULL);
var_dump(false::false);
var_dump(true::true);
?>
--EXPECT--
string(4) "null"
string(5) "false"
string(4) "true"
