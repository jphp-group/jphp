--TEST--
SHA1 File
--FILE--
<?php

echo sha1_file(__dir__ . '/sha1_001.php'), "\n";
echo bin2hex(sha1_file(__dir__ . '/sha1_001.php', true)), "\n";
--EXPECT--
3f33f22634c946f81e6b9879caaa3947fb98563a
3f33f22634c946f81e6b9879caaa3947fb98563a
