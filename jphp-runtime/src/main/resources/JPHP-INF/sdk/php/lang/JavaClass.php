<?php
namespace php\lang;

/**
 * Class JavaClass
 * @packages std, core
 */
final class JavaClass
{


    /**
     * @param string $className - full name of java class
     * @throws JavaException if not found class
     */
    public function __construct($className){ }

    /**
     * @return bool
     */
    public function isStatic() { }

    /**
     * @return bool
     */
    public function isFinal() { }

    /**
     * @return bool
     */
    public function isAbstract() { }

    /**
     * @return bool
     */
    public function isInterface() { }

    /**
     * @return bool
     */
    public function isEnum() { }

    /**
     * @return bool
     */
    public function isAnnotation() { }

    /**
     * @return bool
     */
    public function isArray() { }

    /**
     * @return bool
     */
    public function isPrimitive() { }

    /**
     * @return bool
     */
    public function isAnonymousClass() { }

    /**
     * @return bool
     */
    public function isMemberClass(){ }

    /**
     * @return string
     */
    public function getName() { }

    /**
     * @return string
     */
    public function getSimpleName() { }

    /**
     * @return string
     */
    public function getCanonicalName() { }

    /**
     * @return JavaClass|null
     */
    public function getSuperClass() { }

    /**
     * @return int
     */
    public function getModifiers() { }

    /**
     * @param string $annotationClassName
     * @return bool
     * @throws JavaException if class not found
     */
    public function isAnnotationPresent($annotationClassName){ }

    /**
     * @return JavaClass[]
     */
    public function getInterfaces() { }

    /**
     * @param string $name
     * @param array $types
     * @return JavaMethod
     * @throws JavaException
     */
    public function getDeclaredMethod($name, array $types) { }

    /**
     * @return JavaMethod[]
     */
    public function getDeclaredMethods() { }

    /**
     * @param $name
     * @throws JavaException
     * @return JavaField
     */
    public function getDeclaredField($name) { }

    /**
     * @return JavaField[]
     */
    public function getDeclaredFields() { }

    /**
     * @return JavaObject
     * @throws JavaException
     */
    public function newInstance() { }


    /**
     * @param array $types
     * @param array $arguments
     * @return JavaObject
     * @throws JavaException
     */
    public function newInstanceArgs(array $types, array $arguments) { }

    /**
     * @param JavaClass $class
     * @return bool
     */
    public function isAssignableFrom(JavaClass $class){ }

    /**
     * @param string $className
     * @return bool
     * @throws JavaException
     */
    public function isSubClass($className) { }

    /**
     * @return JavaObject[]
     */
    public function getEnumConstants() { }

    /**
     * @param string $name
     * @return string|null - filename
     */
    public function getResource($name){ }


    /**
     * @param string $name - [int, byte, short, char, float, double, boolean, long]
     */
    public static function primitive($name){ }
}