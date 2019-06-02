--TEST--
date_default_timezone_get() function [4]
--INI--
date.timezone=Incorrect/Zone
--FILE--
<?php
	echo date_default_timezone_get(), "\n";
?>
--EXPECTF--
Warning: date_default_timezone_get(): Invalid date.timezone value 'Incorrect/Zone', we selected the timezone 'UTC' for now. in %s on line %d at pos %d
UTC
