--TEST--
Test dirname() function : basic functionality
--FILE--
<?php

$paths = array(
			'usr',
			'usr/',
			'usr/bin/'
			);

foreach ($paths as $path) {
    echo $path . '=';
	echo dirname($path) . "\n";
}
?>
--EXPECTF--
usr=
usr/=
usr/bin/=usr
