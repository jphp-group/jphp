<?php
namespace php\lang;

final class JavaField extends JavaReflection
{


    /**
     * @param JavaObject $object
     * @throws JavaException
     * @return mixed
     */
    public function get(JavaObject $object = null) { }

    /**
     * @param JavaObject $object
     * @param $value
     * @throws JavaException
     */
    public function set(JavaObject $object = null, $value) { }

    /**
     * @return bool
     */
    public function isStatic(){ }

    /**
     * @return bool
     */
    public function isFinal(){ }

    /**
     * @return bool
     */
    public function isPublic(){ }

    /**
     * @return bool
     */
    public function isProtected(){ }

    /**
     * @return bool
     */
    public function isPrivate(){ }

    /**
     * @return bool
     */
    public function isTransient() { }

    /**
     * @return bool
     */
    public function isVolatile() { }

    /**
     * @return int
     */
    public function getModifiers() { }

    /**
     * @return string
     */
    public function getName() { }

    /**
     * @return JavaClass
     */
    public function getDeclaringClass() { }
}