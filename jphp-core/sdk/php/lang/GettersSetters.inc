<?php
namespace php\lang;

/**
 * To implement a property with a setter and getter, define the following methods::
 *
 *      protected __set[prop_name]($value) // for setter (or not if you want to implement a read-only property)
 *      protected __get[prop_name]() // for getter
 *
 * Class Properties
 * @package php\lang
 */
trait GettersSetters {

    /**
     * @param $name
     * @param $value
     */
    public function __set($name, $value) { }

    /**
     * @param $name
     * @return mixed
     */
    public function __get($name) { }

    /**
     * @param $name
     */
    public function __isset($name) { }

    /**
     * @param $name
     */
    public function __unset($name) { }
}