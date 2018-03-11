--TEST--
SHA1
--FILE--
<?php

echo sha1('apple'), "\n";
echo bin2hex(sha1('apple', true)), "\n";

--EXPECT--
d0be2dc421be4fcd0172e5afceea3970e2f3d940
d0be2dc421be4fcd0172e5afceea3970e2f3d940
