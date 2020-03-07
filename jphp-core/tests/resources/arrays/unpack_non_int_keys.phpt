--TEST--
Array unpacking does not work with non-integer keys
--FILE--
<?php
function gen() {
	yield [] => 1;
	yield 1.23 => 123;
}

try {
	[...gen()];
} catch (Throwable $ex) {
	echo "Exception: " . $ex->getMessage() . "\n";
}

--EXPECTF--

Fatal error: Cannot unpack Traversable with non-integer keys in %s on line 8, position %d