--TEST--
Test gmstrftime() function : usage variation - Checking time related formats which was not supported on Windows before VC14.
--FILE--
<?php
/* Prototype  : string gmstrftime(string format [, int timestamp])
 * Description: Format a GMT/UCT time/date according to locale settings
 * Source code: ext/date/php_date.c
 * Alias to functions:
 */

echo "*** Testing gmstrftime() : usage variation ***\n";

// Initialise function arguments not being substituted (if any)
$timestamp = gmmktime(8, 8, 8, 8, 8, 2008);
setlocale(LC_ALL, "C");
date_default_timezone_set("Asia/Calcutta");

//array of values to iterate over
$inputs = array(
	  'Time in a.m/p.m notation' => "%r",
	  'Time in 24 hour notation' => "%R",
	  'Current time %H:%M:%S format' => "%T",
);

// loop through each element of the array for timestamp

foreach($inputs as $key =>$value) {
      echo "\n--$key--\n";
      echo gmstrftime($value) . "\n";
      var_dump( gmstrftime($value, $timestamp) );
};

?>
--EXPECTF--
*** Testing gmstrftime() : usage variation ***

--Time in a.m/p.m notation--
%02d:%02d:%02d %s
string(11) "08:08:08 AM"

--Time in 24 hour notation--
%02d:%02d
string(5) "08:08"

--Current time %H:%M:%S format--
%02d:%02d:%02d
string(8) "08:08:08"
