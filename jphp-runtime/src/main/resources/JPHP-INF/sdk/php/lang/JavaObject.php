<?php
namespace php\lang;

/**
 * Class JavaObject
 * @package php\lang
 */
final class JavaObject
{
    const __PACKAGE__ = 'std, core';

    /**
     * Get class of object
     * @return JavaClass
     */
    public function getClass() { }

    /**
     * Get name of class of object
     * @return string
     */
    public function getClassName() { }
}