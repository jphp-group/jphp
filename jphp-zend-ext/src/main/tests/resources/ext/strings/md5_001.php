--TEST--
MD5 File
--FILE--
<?php

echo md5('apple'), "\n";
echo bin2hex(md5('apple', true)), "\n";

--EXPECT--
1f3870be274f6c49b3e31a0c6728957f
1f3870be274f6c49b3e31a0c6728957f
