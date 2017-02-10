<?php
namespace php\lang;


final class JavaMethod extends JavaReflection
{


    /**
     * Invoke method
     * @param JavaObject $object
     * @param ...
     */
    public function invoke(JavaObject $object = null) { }


    /**
     * @param JavaObject $object
     * @param array $arguments
     */
    public function invokeArgs(JavaObject $object = null, array $arguments) { }


    /**
     * @return string
     */
    public function getName() { }

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
    public function isPublic() { }

    /**
     * @return bool
     */
    public function isProtected() { }

    /**
     * @return bool
     */
    public function isPrivate() { }

    /**
     * @return bool
     */
    public function isNative() { }

    /**
     * @return bool
     */
    public function isSynchronized() { }

    /**
     * @return bool
     */
    public function isVarArgs() { }

    /**
     * @return JavaClass
     */
    public function getDeclaringClass() { }

    /**
     * @return JavaClass
     */
    public function getReturnedType() { }

    /**
     * @param string $annotationClassName
     * @return bool
     * @throws JavaException
     */
    public function isAnnotationPresent($annotationClassName) { }

    /**
     * @return JavaClass[]
     */
    public function getParameterTypes() { }

    /**
     * @return int
     */
    public function getParameterCount() { }
}