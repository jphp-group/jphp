--TEST--
Test date_create() function : basic functionality
--FILE--
<?php
/* Prototype  : DateTime date_create  ([ string $time  [, DateTimeZone $timezone  ]] )
 * Description: Returns new DateTime object
 * Source code: ext/date/php_date.c
 * Alias to functions: DateTime::__construct
 */

//Set the default time zone
date_default_timezone_set("Europe/London");

echo "*** Testing date_create() : basic functionality ***\n";

print_r( date_create() );

print_r( date_create("GMT") );
print_r( date_create("2005-07-14 22:30:41") );
print_r( date_create("2005-07-14 22:30:41 GMT") );

?>
===DONE===
--EXPECTF--
*** Testing date_create() : basic functionality ***
DateTime Object
(
    [date] => %s %s
    [timezone_type] => 3
    [timezone] => Europe/London
)
DateTime Object
(
    [date] => %s %s
    [timezone_type] => 2
    [timezone] => GMT
)
DateTime Object
(
    [date] => 2005-07-14 22:30:41.000000
    [timezone_type] => 3
    [timezone] => Europe/London
)
DateTime Object
(
    [date] => 2005-07-14 22:30:41.000000
    [timezone_type] => 2
    [timezone] => GMT
)
===DONE===
