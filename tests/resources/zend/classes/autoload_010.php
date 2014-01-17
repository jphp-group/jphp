--TEST--
Ensure implements does trigger autoload.
--FILE--
<?php
  function __autoload($name)
  {
      echo "In autoload: ";
      var_dump($name);
  }

  include __DIR__ . '/autoload_010.inc';
?>
Done.
--EXPECTF--
In autoload: string(6) "UndefI"

Fatal error: Interface 'UndefI' not found in %s on line %d, position %d