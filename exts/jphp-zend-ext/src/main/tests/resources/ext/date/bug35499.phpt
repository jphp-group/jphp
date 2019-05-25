--TEST--
Bug #35499 (strtotime() does not handle whitespace around the date string)
--FILE--
<?php
date_default_timezone_set("UTC");

echo date(DATE_ISO8601, strtotime("11/20/2005 8:00 AM \r\n")) . "\n";
echo date(DATE_ISO8601, strtotime("  11/20/2005 8:00 AM \r\n")) . "\n";

// removed date_parse call
?>
--EXPECT--
2005-11-20T08:00:00+0000
2005-11-20T08:00:00+0000
