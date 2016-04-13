--TEST--
Shift negative 1
--FILE--
<?php

$a = 10;
$b = -2;

try {
  $c = $a >> $b;
  echo "fail >>\n";
} catch (ArithmeticError $e) {
  echo "success >>\n";
}

try {
  $c = $a << $b;
  echo "fail <<\n";
} catch (ArithmeticError $e) {
  echo "success <<\n";
}

?>
--EXPECTF--
success >>
success <<