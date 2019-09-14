--TEST--
Create DateInterval object using __construct() throws Exception when interval spec is invalid.
--CREDITS--
Edgar Asatryan <nstdio@gmail.com>
--FILE--
<?php
$specs = [
    "Only Numers" => "123",
    "Without Leading P" => "2Y5M",
    "Illegal Chars" => "P2Y5V",
    "Lowecase" => "p2y5v",
    "Year Float" => "P0.5Y5V",
    "Year Negative" => "P-1Y5V",
];

foreach($specs as $key => $value) {
    try {
        new DateInterval($value);
    } catch(Exception $e) {
        echo "$key - " . $e->getMessage() . "\n";
    }
}
?>
--EXPECT--
Only Numers - DateInterval::__construct(): Unknown or bad format (123)
Without Leading P - DateInterval::__construct(): Unknown or bad format (2Y5M)
Illegal Chars - DateInterval::__construct(): Unknown or bad format (P2Y5V)
Lowecase - DateInterval::__construct(): Unknown or bad format (p2y5v)
Year Float - DateInterval::__construct(): Unknown or bad format (P0.5Y5V)
Year Negative - DateInterval::__construct(): Unknown or bad format (P-1Y5V)