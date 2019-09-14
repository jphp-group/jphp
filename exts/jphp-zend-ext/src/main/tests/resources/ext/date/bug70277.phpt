--TEST--
Bug #70277 (new DateTimeZone($foo) is ignoring text after null byte)
--FILE--
<?php
$timezone = "Europe/Zurich\0Foo";
var_dump(timezone_open($timezone));
try {
    new DateTimeZone($timezone);
} catch (Exception $e) {
    echo $e->getMessage();
}
?>
--EXPECTF--
Warning: timezone_open(): Timezone must not contain null bytes in %s on line %d at pos %d
bool(false)
DateTimeZone::__construct(): Timezone must not contain null bytes
