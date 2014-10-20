--TEST--
Non-ref generators cannot be iterated by-ref
--FILE--
<?php
function gen() { yield; }
$gen = gen();
foreach ($gen as &$value) { }
?>
--EXPECTF--

Fatal error: Uncaught exception 'Exception' with message 'You can only iterate a generator by-reference if it declared that it yields by-reference' in %s on line 4, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 4
