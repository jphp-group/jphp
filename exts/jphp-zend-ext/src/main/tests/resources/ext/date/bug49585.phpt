--TEST--
Bug #49585 (date_format buffer not long enough for >4 digit years)
--FILE--
<?php
date_default_timezone_set('UTC');

$date = new DateTime('-1500-01-01');
var_dump($date->format('r'));

$date->setDate(-214748364, 1, 1);
var_dump($date->format('r'));
var_dump($date->format('c'));
?>
--EXPECT--
string(32) "Fri, 01 Jan -1500 00:00:00 +0000"
string(37) "Tue, 01 Jan -214748364 00:00:00 +0000"
string(30) "-214748364-01-01T00:00:00+0000"
