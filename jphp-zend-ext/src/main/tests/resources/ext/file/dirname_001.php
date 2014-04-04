--TEST--
Test dirname() function : basic functionality
--FILE--
<?php

$paths = array(
			'/usr',
			'/usr/',
			'/usr/bin/',
			'/usr/bin/env'
			);

foreach ($paths as $path) {
	echo dirname($path) . "\n";
}

--EXPECTF--
/
/
/usr
/usr/bin
