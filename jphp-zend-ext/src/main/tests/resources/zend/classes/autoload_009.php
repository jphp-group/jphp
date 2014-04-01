--TEST--
Ensure type hints for unknown types do not trigger autoload. 
--FILE--
<?php
  function __autoload($name)
  {
      echo "In autoload: ";
      var_dump($name);
  }
  
  function f(UndefClass $x)
  {
  }
  f(new stdClass);
?>
--EXPECTF--

Recoverable error: Argument 1 passed to f() must be an instance of UndefClass, instance of stdClass given, called in %s on line %d, position %d and defined in %s on line %d, position %d