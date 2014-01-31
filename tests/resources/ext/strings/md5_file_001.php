--TEST--
MD5 File
--FILE--
<?php

echo md5_file(__dir__ . '/md5_001.php'), "\n";
echo bin2hex(md5_file(__dir__ . '/md5_001.php', true)), "\n";
--EXPECT--
83365a86a43ba5fc216456a47c23cad9
83365a86a43ba5fc216456a47c23cad9
