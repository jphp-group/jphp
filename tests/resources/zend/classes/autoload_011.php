--TEST--
Ensure extends does trigger autoload.
--FILE--
<?php
  function __autoload($name)
  {
      echo "In autoload: ";
      var_dump($name);
  }

  include __DIR__ . '/autoload_011.inc';
?>
--EXPECTF--
In autoload: string(9) "UndefBase"

Fatal error: Class 'UndefBase' not found in %s on line %d, position %d