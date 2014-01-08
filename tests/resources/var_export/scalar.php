--FILE--
<?php

var_export(NULL);
echo "\n";

var_export(123);
echo "\n";

var_export(3.4);
echo "\n";

var_export("'foobar'");
echo "\n";

var_export(true);
echo "\n";

var_export(false);
echo "\n";

--EXPECT--
NULL
123
3.4
'\'foobar\''
true
false
