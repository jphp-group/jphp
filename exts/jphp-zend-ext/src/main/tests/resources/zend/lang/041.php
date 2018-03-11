--TEST--
Testing for loop with multiple expressions in init/cond/incr parts.
--FILE--
<?php
for ($i=1,$j=10; print("$i\n"),$i<5; $i++,$j++) {
    print("$j\n");
}
?>
--EXPECT--
1
10
2
11
3
12
4
13
5
