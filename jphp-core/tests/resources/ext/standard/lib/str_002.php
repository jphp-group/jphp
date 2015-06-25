--TEST--
php\lib\str Test - comparing
--FILE--
<?php

use php\lib\Str as str;


echo "equalsIgnoreCase(foo, foo) == " . str::equalsIgnoreCase('foo', 'foo'), "\n";
echo "equalsIgnoreCase(foo, fOO) == " . str::equalsIgnoreCase('foo', 'fOO'), "\n";
echo "equalsIgnoreCase(Foo, foo) == " . str::equalsIgnoreCase('Foo', 'foo'), "\n";

echo "--compare\n";

echo "compare(foo, foo) == " . str::compare('foo', 'foo'), "\n";
echo "compare(Foo, foo) == " . str::compare('Foo', 'foo'), "\n";
echo "compare(foo, Foo) == " . str::compare('foo', 'Foo'), "\n";

echo "--compare-ignore-case\n";

echo "compareIgnoreCase(foo, foo) == " . str::compareIgnoreCase('foo', 'foo'), "\n";
echo "compareIgnoreCase(Foo, foo) == " . str::compareIgnoreCase('Foo', 'foo'), "\n";
echo "compareIgnoreCase(foo, Foo) == " . str::compareIgnoreCase('foo', 'Foo'), "\n";


?>
--EXPECT--
equalsIgnoreCase(foo, foo) == 1
equalsIgnoreCase(foo, fOO) == 1
equalsIgnoreCase(Foo, foo) == 1
--compare
compare(foo, foo) == 0
compare(Foo, foo) == -32
compare(foo, Foo) == 32
--compare-ignore-case
compareIgnoreCase(foo, foo) == 0
compareIgnoreCase(Foo, foo) == 0
compareIgnoreCase(foo, Foo) == 0
