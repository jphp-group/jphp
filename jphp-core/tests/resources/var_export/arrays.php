--FILE--
<?php

$a = array (1, 2, array ("a", "b", "c"));
var_export($a);

echo "\n";

$x['x'] = &$x;
var_export($x);
--EXPECTF--
array (
  0 => 1,
  1 => 2,
  2 =>
  array (
    0 => 'a',
    1 => 'b',
    2 => 'c',
  ),
)
Warning: var_export does not handle circular references in %s on line %d at pos %d
array (
  'x' => NULL,
)
