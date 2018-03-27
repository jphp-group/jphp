--TEST--
SemVer compare
--FILE--
<?php

use semver\SemVersion;

$ver1 = new SemVersion('1.2.3');
$ver2 = new SemVersion('1.2.4');
$ver3 = new SemVersion('1.2.3');

var_dump("ver1 < ver2:", $ver1 < $ver2);
var_dump("ver1 <= ver2:",$ver1 <= $ver2);
var_dump("ver1 != ver2:",$ver1 != $ver2);
var_dump("ver1 >= ver1:",$ver1 >= $ver1);
var_dump("ver1 === ver1:",$ver1 === $ver1);
var_dump("ver1 === ver3:",$ver1 === $ver3);
var_dump("ver1 > ver3:",$ver1 > $ver3);
var_dump("ver1 >= ver3:",$ver1 >= $ver3);

?>
--EXPECT--
string(12) "ver1 < ver2:"
bool(true)
string(13) "ver1 <= ver2:"
bool(true)
string(13) "ver1 != ver2:"
bool(true)
string(13) "ver1 >= ver1:"
bool(true)
string(14) "ver1 === ver1:"
bool(true)
string(14) "ver1 === ver3:"
bool(false)
string(12) "ver1 > ver3:"
bool(false)
string(13) "ver1 >= ver3:"
bool(true)