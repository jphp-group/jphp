--TEST--
Bug #60236 (TLA timezone dates are not converted properly from timestamp)
--INI--
date.timezone=America/New_York
--FILE--
<?php
$t = new DateTime('2010-07-06 18:38:28 EDT');
$ts = $t->format('U');
var_dump($ts);
$t->setTimestamp($ts);
var_dump($t);
?>
--EXPECTF--
string(10) "1278455908"
object(DateTime)#%d (3) {
  ["date"]=>
  string(26) "2010-07-06 18:38:28.000000"
  ["timezone_type"]=>
  int(2)
  ["timezone"]=>
  string(3) "EDT"
}
