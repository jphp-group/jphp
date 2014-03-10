--TEST--
output buffering - fatalism
JPHP - Fix, it can use print_r with 2 argument as true
--FILE--
<?php
function obh($s)
{
	print_r($s, 1);
}
ob_start("obh");
echo "foo\n";
?>
--EXPECTF--
