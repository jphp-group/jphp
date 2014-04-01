--TEST--
SHA1 File
--FILE--
<?php

echo sha1_file(__dir__ . '/sha1_file_001.inc'), "\n";
echo bin2hex(sha1_file(__dir__ . '/sha1_file_001.inc', true)), "\n";
--EXPECT--
c8d92306267cae95f60160e25dc6669958354744
c8d92306267cae95f60160e25dc6669958354744