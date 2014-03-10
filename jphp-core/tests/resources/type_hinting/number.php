--FILE--
<?php

// @@mode JPHP

function test(number $x){
}

test(2);
echo "Done.1\n";
test(3.4);
echo "Done.2\n";

test('abcd');
echo "Done.3\n";

--EXPECTF--
Done.1
Done.2

Recoverable error: Argument 1 passed to test() must be of the type number, string given, called in %s on line %d, position %d and defined in %s on line 5, position %d
