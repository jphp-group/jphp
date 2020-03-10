--TEST--
Cannot concatenate in arrays inside of class constants
--FILE--
<?php

class a
{
    private const SENSITIVE_HEADERS = ['a' . 'b' . PHP_INT_SIZE];
    private const OTHER = [self::SENSITIVE_HEADERS, [1, 2]];

    function test() {
        print_r(self::SENSITIVE_HEADERS);
        print_r(self::OTHER);
    }
}

(new a)->test();
?>
--EXPECT--
Array
(
    [0] => ab8
)
Array
(
    [0] => Array
        (
            [0] => ab8
        )

    [1] => Array
        (
            [0] => 1
            [1] => 2
        )

)