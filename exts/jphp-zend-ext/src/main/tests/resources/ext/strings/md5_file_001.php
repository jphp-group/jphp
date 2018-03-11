--TEST--
MD5 File
--FILE--
<?php

echo md5_file(__dir__ . '/md5_file_001.inc'), "\n";
echo bin2hex(md5_file(__dir__ . '/md5_file_001.inc', true)), "\n";
--EXPECT--
b632e5b2842d8a5f0bdeed7725e3a4ce
b632e5b2842d8a5f0bdeed7725e3a4ce