--TEST--
DateInterval::__set_state()
--CREDITS--
Edgar Asatryan <nstdio@gmail.com>
--FILE--
<?php
$states = [
    "Empty Array" => [],
    "All string" => ["y" => "a", "m" => "a", "d" => "a", "h" => "a", "i" => "a", "s" => "a", "f" => "a", "days" => "a", "invert" => "a"],
];

foreach ($states as $key => $value) {
    echo "==$key==\n";
    var_dump(DateInterval::__set_state($value));
}
?>
--EXPECTF--
==Empty Array==
object(DateInterval)#%d (9) {
  ["y"]=>
  int(-1)
  ["m"]=>
  int(-1)
  ["d"]=>
  int(-1)
  ["h"]=>
  int(-1)
  ["i"]=>
  int(-1)
  ["s"]=>
  int(-1)
  ["f"]=>
  float(-1)
  ["invert"]=>
  int(0)
  ["days"]=>
  int(-1)
}
==All string==
object(DateInterval)#%d (9) {
  ["y"]=>
  int(0)
  ["m"]=>
  int(0)
  ["d"]=>
  int(0)
  ["h"]=>
  int(0)
  ["i"]=>
  int(0)
  ["s"]=>
  int(0)
  ["f"]=>
  float(0)
  ["invert"]=>
  int(0)
  ["days"]=>
  int(0)
}