<?php
namespace php\swing;

final class ScopeValue {
    private function __construct() { }

    /**
     * @return mixed
     */
    public function getValue() {}

    /**
     * @param $value
     */
    public function setValue($value) { }

    /**
     * @param object $object
     * @param string $property
     */
    public function bind($object, $property) { }

    /**
     * @param string $name
     * @return ScopeValue
     */
    public static function of($name) { }
}