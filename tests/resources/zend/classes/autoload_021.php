--TEST--
Validation of class names in the autoload process
--FILE--
<?php
function __autoload($name) {
	echo "$name\n";
}
$a = "../BUG";
$x = new $a;
echo "BUG\n";
?>
--EXPECTF--

Fatal error: Class '../BUG' not found in %s on line 6, position %d