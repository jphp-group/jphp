--TEST--
Reflection Class -> getDefaultProperties
--FILE--
<?php
define('DYN', 333);

class Bar {
    protected $inheritedProperty = 'inheritedDefault';
}

class Foo extends Bar {
    public $property = 'propertyDefault';
    private $privateProperty = 'privatePropertyDefault';
    public static $staticProperty = 'staticProperty';
    public $defaultlessProperty;
    public $dyn = DYN;
}

$reflectionClass = new ReflectionClass('Foo');
var_dump($reflectionClass->getDefaultProperties());
?>
--EXPECT--
array(6) {
  ["staticProperty"]=>
  string(14) "staticProperty"
  ["inheritedProperty"]=>
  string(16) "inheritedDefault"
  ["property"]=>
  string(15) "propertyDefault"
  ["privateProperty"]=>
  string(22) "privatePropertyDefault"
  ["defaultlessProperty"]=>
  NULL
  ["dyn"]=>
  int(333)
}
