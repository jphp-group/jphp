--TEST--
SemVer snapshots
--FILE--
<?php

use semver\SemVersion;

$ver1 = new SemVersion('1.2.3-SNAPSHOT');

var_dump($ver1->toNormal());
var_dump($ver1->getPreReleaseString());

?>
--EXPECT--
string(5) "1.2.3"
string(8) "SNAPSHOT"