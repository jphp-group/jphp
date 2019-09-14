--TEST--
Bug #45554 (Inconsistent behavior of the u format char)
--INI--
date.timezone=UTC
--FILE--
<?php
$format = "m-d-Y H:i:s.u";
$d = date_create_from_format($format, "03-15-2005 12:22:29.000000");
echo $d->format($format), "\n";

$d = date_create_from_format($format, "03-15-2005 12:22:29.001001");
echo $d->format($format), "\n";

$d = date_create_from_format($format, "03-15-2005 12:22:29.0010");
echo $d->format($format), "\n";
?>
--EXPECT--
03-15-2005 12:22:29.000000
03-15-2005 12:22:29.001001
03-15-2005 12:22:29.001000
