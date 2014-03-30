--TEST--
Conflicting properties with different visibility modifiers should result in a fatal error, since this indicates that the code is incompatible.
--FILE--
<?php
error_reporting(E_ALL);

trait THello1 {
    public $hello;
}

trait THello2 {
    private $hello;
}

class TraitsTest {
    use THello1;
    use THello2;
}

$t = new TraitsTest;
$t->hello = "foo";
?>
--EXPECTF--

Fatal error: 'THello1' and 'THello2' define the same property ($hello) in the composition of TraitsTest in %s on line %d, position %d