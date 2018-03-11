--TEST--
Testing for loop without condition
--FILE--
<?php
$i=0;
for (;;) {
    print("$i\n");
    if ($i>=3) break;
    $i++;
}
?>
--EXPECT--
0
1
2
3
