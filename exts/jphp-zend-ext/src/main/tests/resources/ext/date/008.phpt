--TEST--
getdate() tests
--FILE--
<?php
date_default_timezone_set('UTC');

$t = mktime(0,0,0, 6, 27, 2006);

print_r(getdate($t));
print_r(getdate());

echo "Done\n";
?>
--EXPECTF--
Array
(
    [seconds] => 0
    [minutes] => 0
    [hours] => 0
    [mday] => 27
    [wday] => 2
    [mon] => 6
    [year] => 2006
    [yday] => 177
    [weekday] => Tuesday
    [month] => June
    [0] => 1151366400
)
Array
(
    [seconds] => %d
    [minutes] => %d
    [hours] => %d
    [mday] => %d
    [wday] => %d
    [mon] => %d
    [year] => %d
    [yday] => %d
    [weekday] => %s
    [month] => %s
    [0] => %d
)
Done
