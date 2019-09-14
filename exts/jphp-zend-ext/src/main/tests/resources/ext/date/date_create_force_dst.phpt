--TEST--
RFC: DateTime and Daylight Saving Time Transitions (zone type 3, ba)
--CREDITS--
Daniel Convissor <danielc@php.net>
--FILE--
<?php
date_default_timezone_set('America/New_York');
$date = date_create('2010-11-07 01:30:00 EDT');
echo $date->format('Y-m-d H:i:s T') . "\n";
?>
--EXPECT--
2010-11-07 01:30:00 EDT
