--TEST--
Test annon classes
--FILE--
<?
trait Foo {
    public function someMethod() {
      return "bar";
    }
}

$anonClass = new class {
    use Foo;
};

var_dump($anonClass->someMethod()); // string(3) "bar"
?>
--EXPECTF--
string(3) "bar"