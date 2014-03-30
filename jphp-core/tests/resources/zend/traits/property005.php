--TEST--
The same rules are applied for properties that are defined in the class hierarchy. Thus, if the properties are compatible, a notice is issued, if not a fatal error occures.
--FILE--
<?php
class Base {
    private $hello;
}

trait THello1 {
    private $hello;
}

class Notice extends Base {
    use THello1;
    private $hello;
}

// now we do the test for a fatal error

class TraitsTest {
    use THello1;
    public $hello;
}

echo "POST-CLASS-GUARD2\n";

$t = new TraitsTest;
$t->hello = "foo";
?>
--EXPECTF--
Strict Standards: 'Notice' and 'THello1' define the same property ($hello) in the composition of Notice. This might be incompatible, to improve maintainability consider using accessor methods in traits instead. Class was composed in %s on line %d at pos %d

Fatal error: 'TraitsTest' and 'THello1' define the same property ($hello) in the composition of TraitsTest in %s on line %d, position %d
