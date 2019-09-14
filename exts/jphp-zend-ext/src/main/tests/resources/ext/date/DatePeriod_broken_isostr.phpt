--TEST--
Test error messages with broken iso strings.
--CREDITS--
Edgar Asatryan <nstdio@gmail.com>
--FILE--
<?php
$bad = [
    "",
    "abc",
    "R4/",
    "R4",
    "R1/20",
    "R4/2012-07-01T00:00:00Z",
    "R-1/2012-07-01T00:00:00Z",
    "R1/2012-07-01T00:00:00Z/",
    "R1/2012-07-01T00:00:00Z/abc",
    ];

foreach($bad as $iso) {
    try {
        new DatePeriod($iso);
    } catch (Exception $e) {
        echo $e->getMessage() , PHP_EOL;
    }
}
?>
--EXPECT--
DatePeriod::__construct(): Unknown or bad format ()
DatePeriod::__construct(): Unknown or bad format (abc)
DatePeriod::__construct(): The ISO interval 'R4/' did not contain a start date.
DatePeriod::__construct(): The ISO interval 'R4' did not contain a start date.
DatePeriod::__construct(): Unknown or bad format (R1/20)
DatePeriod::__construct(): The ISO interval 'R4/2012-07-01T00:00:00Z' did not contain an interval.
DatePeriod::__construct(): Unknown or bad format (R-1/2012-07-01T00:00:00Z)
DatePeriod::__construct(): The ISO interval 'R1/2012-07-01T00:00:00Z/' did not contain an interval.
DatePeriod::__construct(): Unknown or bad format (R1/2012-07-01T00:00:00Z/abc)
