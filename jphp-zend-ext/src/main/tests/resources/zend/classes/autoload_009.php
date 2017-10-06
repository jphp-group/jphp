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

Fatal error: Uncaught TypeError: Argument 1 passed to f() must be an instance of UndefClass, instance of stdClass given, called in %s on line 11, position %d and defined in %s on line 8, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 8