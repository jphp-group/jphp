--TEST--
Test strftime() function : usage variation - Passing date related format strings to format argument.
--FILE--
<?php
/* Prototype  : string strftime(string format [, int timestamp])
 * Description: Format a local time/date according to locale settings
 * Source code: ext/date/php_date.c
 * Alias to functions:
 */

echo "*** Testing strftime() : usage variation ***\n";

// Initialise function arguments not being substituted (if any)

date_default_timezone_set("Asia/Calcutta");
$timestamp = mktime(8, 8, 8, 8, 8, 2008);

//array of values to iterate over
$inputs = array(
	  'Year as decimal number without a century' => "%y",
	  'Year as decimal number including the century' => "%Y",
	  'Time zone offset' => "%Z",
	  'Time zone offset' => "%z",
);

// loop through each element of the array for timestamp

foreach($inputs as $key =>$value) {
      echo "\n--$key--\n";
      print_r( strftime($value) );
      echo PHP_EOL;
      print_r( strftime($value, $timestamp) );
      echo PHP_EOL;
};

?>
===DONE===
--EXPECTF--
*** Testing strftime() : usage variation ***

--Year as decimal number without a century--
%s
08

--Year as decimal number including the century--
%s
2008

--Time zone offset--
%s
%s
===DONE===
