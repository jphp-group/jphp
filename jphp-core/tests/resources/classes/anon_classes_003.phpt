--TEST--
Test annon classes
--FILE--
<?
class Foo {}

$child = new class extends Foo {};

var_dump($child instanceof Foo); // true
?>
--EXPECTF--
bool(true)