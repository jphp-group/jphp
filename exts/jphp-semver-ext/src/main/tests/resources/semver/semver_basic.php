--TEST--
SemVer basic
--FILE--
<?php

use semver\SemVersion;

$ver = new SemVersion('1.2.3-rc1+dev');

var_dump(
        $ver->getMajorNum(), $ver->getMinorNum(), $ver->getPatchNum(), $ver->getBuildString(), $ver->getPreReleaseString()
);

?>
--EXPECT--
int(1)
int(2)
int(3)
string(3) "dev"
string(3) "rc1"