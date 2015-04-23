--TEST--
php\lib\str Test - searching
--FILE--
<?php

use php\lib\String as str;

echo "pos(Ob_foobar, ob) == " . str::pos('Ob_foobar', 'ob'), "\n";
echo "pos(Ob_foobar, ob, 4) == " . str::pos('Ob_foobar', 'ob', 7), "\n";
echo "pos(Ob_foobar, ob, 100) == " . str::pos('Ob_foobar', 'ob', 100), "\n";
echo "pos(Ob_foobar, ob, -1) == " . str::pos('Ob_foobar', 'ob', -1), "\n";
echo "pos(Ob_foobar, ob, -1000) == " . str::pos('Ob_foobar', 'ob', -1000), "\n";

echo "--ignore-case\n";

echo "posIgnoreCase(Ob_foobar, ob) == " . str::posIgnoreCase('Ob_foobar', 'ob'), "\n";
echo "posIgnoreCase(Ob_foobar, ob, 4) == " . str::posIgnoreCase('Ob_foobar', 'ob', 7), "\n";
echo "posIgnoreCase(Ob_foobar, ob, 100) == " . str::posIgnoreCase('Ob_foobar', 'ob', 100), "\n";
echo "posIgnoreCase(Ob_foobar, ob, -1) == " . str::posIgnoreCase('Ob_foobar', 'ob', -1), "\n";
echo "posIgnoreCase(Ob_foobar, ob, -1000) == " . str::posIgnoreCase('Ob_foobar', 'ob', -1000), "\n";

echo "--last-pos\n";

echo "lastPos(ob_foobar, ob) == " . str::lastPos('ob_foobar', 'ob'), "\n";
echo "lastPos(ob_foobar, ob, 4) == " . str::lastPos('ob_foobar', 'ob', 7), "\n";
echo "lastPos(ob_foobar, ob, 100) == " . str::lastPos('ob_foobar', 'ob', 100), "\n";
echo "lastPos(ob_foobar, ob, -1) == " . str::lastPos('ob_foobar', 'ob', -1), "\n";
echo "lastPos(ob_foobar, ob, -1000) == " . str::lastPos('ob_foobar', 'ob', -1000), "\n";

echo "--last-pos-ignore-case\n";

echo "lastPosIgnoreCase(ob_foObar, ob) == " . str::lastPosIgnoreCase('ob_foObar', 'ob'), "\n";
echo "lastPosIgnoreCase(ob_foObar, ob, 4) == " . str::lastPosIgnoreCase('ob_foObar', 'ob', 7), "\n";
echo "lastPosIgnoreCase(ob_foObar, ob, 100) == " . str::lastPosIgnoreCase('ob_foObar', 'ob', 100), "\n";
echo "lastPosIgnoreCase(ob_foObar, ob, -1) == " . str::lastPosIgnoreCase('ob_foObar', 'ob', -1), "\n";
echo "lastPosIgnoreCase(ob_foObar, ob, -1000) == " . str::lastPosIgnoreCase('ob_foObar', 'ob', -1000), "\n";

?>
--EXPECT--
pos(Ob_foobar, ob) == 5
pos(Ob_foobar, ob, 4) == -1
pos(Ob_foobar, ob, 100) == -1
pos(Ob_foobar, ob, -1) == 5
pos(Ob_foobar, ob, -1000) == 5
--ignore-case
posIgnoreCase(Ob_foobar, ob) == 0
posIgnoreCase(Ob_foobar, ob, 4) == -1
posIgnoreCase(Ob_foobar, ob, 100) == -1
posIgnoreCase(Ob_foobar, ob, -1) == 0
posIgnoreCase(Ob_foobar, ob, -1000) == 0
--last-pos
lastPos(ob_foobar, ob) == 5
lastPos(ob_foobar, ob, 4) == 5
lastPos(ob_foobar, ob, 100) == 5
lastPos(ob_foobar, ob, -1) == -1
lastPos(ob_foobar, ob, -1000) == -1
--last-pos-ignore-case
lastPosIgnoreCase(ob_foObar, ob) == 5
lastPosIgnoreCase(ob_foObar, ob, 4) == 5
lastPosIgnoreCase(ob_foObar, ob, 100) == 5
lastPosIgnoreCase(ob_foObar, ob, -1) == -1
lastPosIgnoreCase(ob_foObar, ob, -1000) == -1
