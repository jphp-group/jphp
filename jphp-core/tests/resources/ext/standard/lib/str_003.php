--TEST--
php\lib\str Test - misc
--FILE--
<?php

use php\lib\str;

echo "length(foobar) == " . str::length('foobar'), "\n";
echo "lower(FOObar) == " . str::lower('FOObar'), "\n";
echo "upper(FOObar) == " . str::upper('FOObar'), "\n";
echo "repeat(foo, 3) == " . str::repeat('foo', 3), "\n";
echo "repeat(o, 6) == " . str::repeat('o', 6), "\n";
echo "repeat(o, -1) == " . str::repeat('o', -1), "*nothing*\n";
echo "reverse(foobar) == " . str::reverse('foobar'), "\n";
echo "sub(foobar, 3) == " . str::sub('foobar', 3), "\n";
echo "sub(foobar, 3, 5) == " . str::sub('foobar', 3, 5), "\n";
echo "trim(  foobar  ) == " . str::trim('  foobar  '), "\n";


?>
--EXPECT--
length(foobar) == 6
lower(FOObar) == foobar
upper(FOObar) == FOOBAR
repeat(foo, 3) == foofoofoo
repeat(o, 6) == oooooo
repeat(o, -1) == *nothing*
reverse(foobar) == raboof
sub(foobar, 3) == bar
sub(foobar, 3, 5) == ba
trim(  foobar  ) == foobar
