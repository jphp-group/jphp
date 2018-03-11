--TEST--
ZE2 A static abstract methods
--FILE--
<?php

interface showable
{
	static function show();
}

class pass implements showable
{
	static function show() {
		echo "Call to function show()\n";
	}
}

pass::show();

eval('
class fail
{
	abstract static function func();
}
');

echo "Done\n"; // shouldn't be displayed
?>
--EXPECTF--
Call to function show()

Fatal error: Class fail contains 1 abstract method(s) and must therefore be declared abstract or implement the remaining methods (fail::func), eval()'s code on line %d, position %d in %s on line %d, position %d
