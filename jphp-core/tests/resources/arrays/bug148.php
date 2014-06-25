--TEST--
Bug #148
--FILE--
<?php
$files = array('foo', 'bar');
$result = [];
foreach ($files as $el) {
    $item = [$el];
    $result[] = $item;
}
print_r($result);
?>
--EXPECT--
Array
(
    [0] => Array
        (
            [0] => foo
        )

    [1] => Array
        (
            [0] => bar
        )

)
